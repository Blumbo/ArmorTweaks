package net.blumbo.armortweaks.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow @Nullable public abstract LivingEntity getAttacker();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // Modify armor protection
    @Redirect(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    protected float applyArmorToDamage(float damage, float armor, float armorToughness) {

        if (useVanillaArmor()) {
            float mainFormula = armor - (4.0F * damage) / (8.0F + armorToughness);
            float usedFormula = MathHelper.clamp(mainFormula, armor * 0.2F, 20.0F);
            return damage * (1.0F - usedFormula / 25.0F);
        } else {
            return damage * (1.0F - armor / getArmorDivisor());
        }

    }

    // Modify armor enchantment protection
    @Redirect(method = "applyEnchantmentsToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getInflictedDamage(FF)F"))
    protected float applyEnchantmentsToDamage(float damage, float protection) {

        if (useVanillaEnchantment()) {
            protection = MathHelper.clamp(protection, 0.0F, 20.0F);
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

    @Inject(at = @At("HEAD"), method = "applyArmorToDamage")
    protected void baseDamageDebug(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.base = amount;
    }
    @Inject(at = @At("HEAD"), method = "applyEnchantmentsToDamage")
    public void armorDamageDebug(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.armor = amount;
    }
    @Inject(at = @At("RETURN"), method = "applyEnchantmentsToDamage")
    public void enchantmentDamageDebug(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.enchantment = amount;
    }
    @Inject(at = @At("RETURN"), method = "damage")
    protected void sendDebug(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (!sendDamageFeedback()) return;

        String message = "Base: " + base + " | Armor: " + armor + " | Enchantments: " + enchantment;
        System.out.println(message);

        if (sendDamageToChat()) {
            message = "\2477" + message.replaceAll("\\|", "\2478|\2477");

            if (getType() == EntityType.PLAYER) {
                ((PlayerEntity)(Entity)this).sendMessage(Text.of("\247c[\uD83D\uDEE1] " + message), false);
            }
            if (getAttacker() instanceof PlayerEntity) {
                ((PlayerEntity)getAttacker()).sendMessage(Text.of("\247a[\uD83D\uDDE1] " + message), false);
            }

        }

    }

    // Scoreboards ----------------------------------------------------------------------------

    // Returns the specified scoreboard value from armor.tweaks objective
    protected int getArmorTweakValue(String playerName) {
        Scoreboard scoreboard = getEntityWorld().getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjective("armor.tweaks");
        return scoreboard.getPlayerScore(playerName, objective).getScore();
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
