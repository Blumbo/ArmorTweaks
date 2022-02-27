package net.blumbo.armortweaks.mixin;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow private DamageSource lastDamageSource;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    // Armor and damage modifications  -----------------------------------------------------------

    // Check for explosion
    boolean isExplosion = false;
    @Inject(method = "hurt", at = @At("HEAD"))
    protected void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        isExplosion = damageSource.isExplosion();
    }

    // Modify armor protection and crystal damage
    @Redirect(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/CombatRules;getDamageAfterAbsorb(FFF)F"))
    protected float applyArmorToDamage(float damage, float armor, float armorToughness) {

        if (this.isExplosion) damage *= 0.5625F; // 9 / 16

        if (useVanillaArmor()) {
            float mainFormula = armor - (4.0F * damage) / (8.0F + armorToughness);
            float usedFormula = Mth.clamp(mainFormula, armor * 0.2F, 20.0F);
            return damage * (1.0F - usedFormula / 25.0F);
        } else {
            return damage * (1.0F - armor / getArmorDivisor());
        }

    }

    // Modify armor enchantment protection
    @Redirect(method = "getDamageAfterMagicAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/CombatRules;getDamageAfterMagicAbsorb(FF)F"))
    protected float applyEnchantmentsToDamage(float damage, float protection) {

        if (useVanillaEnchantment()) {
            protection = Mth.clamp(protection, 0.0F, 20.0F);
            return damage * (1.0F - protection / 25.0F);
        } else {
            float number = getEnchantmentNerfValue();
            protection = Math.max(0.0F, protection);
            return damage * number / (number + protection);
        }

    }

    // Debug messages ----------------------------------------------------------------------------

    // Code below sends a console message every time a living entity takes damage.
    // Displays damage before armor calculations, between armor and enchantment calculations and after all calculations.

    float base;
    float armor;
    float enchantment;

    @Inject(at = @At("HEAD"), method = "getDamageAfterArmorAbsorb")
    protected void baseDamageDebug(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.base = amount;
    }
    @Inject(at = @At("HEAD"), method = "getDamageAfterMagicAbsorb")
    public void armorDamageDebug(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.armor = amount;
    }
    @Inject(at = @At("RETURN"), method = "getDamageAfterMagicAbsorb")
    public void enchantmentDamageDebug(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.enchantment = amount;
    }
    @Inject(at = @At("RETURN"), method = "hurt")
    protected void sendDebug(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (!sendDamageFeedback()) return;

        String message = "Base: " + base + " | Armor: " + armor + " | Enchantments: " + enchantment;
        System.out.println(message);

        if (sendDamageToChat()) {
            message = "\2477" + message.replaceAll("\\|", "\2478|\2477");

            UUID uuid = getUUID();
            if (getType() == EntityType.PLAYER) {
                this.sendMessage(new TextComponent("\247c[\uD83D\uDEE1] " + message), uuid);
            }
            if (lastDamageSource != null && lastDamageSource.getEntity() instanceof Player) {
                lastDamageSource.getEntity().sendMessage(new TextComponent("\247b[\uD83D\uDDE1] " + message), uuid);
            }

        }

    }

    // Scoreboards ----------------------------------------------------------------------------

    // Returns the specified scoreboard value from armor.tweaks objective
    protected int getArmorTweakValue(String playerName) {
        Scoreboard scoreboard = level.getScoreboard();
        Objective objective = scoreboard.getObjective("armor.tweaks");
        return scoreboard.getOrCreatePlayerScore(playerName, objective).getScore();
    }

    // Scoreboard check for if damage values should be sent anywhere
    protected boolean sendDamageFeedback() {
        return !(getArmorTweakValue("send.damage") < 0);
    }
    // Scoreboard check for if damage values should be sent to the chat
    protected boolean sendDamageToChat() {
        return getArmorTweakValue("send.damage") > 0;
    }

    // Scoreboard check for if vanilla armor calculations should be used
    protected boolean useVanillaArmor() {
        return getArmorTweakValue("vanilla.armor") == 1;
    }
    // Scoreboard check for if vanilla enchantment protection calculations should be used
    protected boolean useVanillaEnchantment() {
        return getArmorTweakValue("vanilla.enchantment") == 1;
    }

    // Scoreboard check for the number divided from armor protection value
    protected float getArmorDivisor() {
        int score = getArmorTweakValue("armor.divisor");
        if (score > 0) return (float)score;
        else return 30.0F;
    }
    // Scoreboard check for the number used in enchantment protection
    protected float getEnchantmentNerfValue() {
        int score = getArmorTweakValue("enchantment.nerf");
        if (score > 0) return (float)score;
        else return 15.0F;
    }

}
