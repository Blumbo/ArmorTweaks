package net.blumbo.armortweaks.mixin;

import net.blumbo.armortweaks.ArmorTweaks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
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

    private boolean useVanillaArmor() {
        return !((Object)this instanceof PlayerEntity) || ArmorTweaks.bools.get(ArmorTweaks.vanillaArmorKey);
    }
    private boolean useVanillaEnchantment() {
        return !((Object)this instanceof PlayerEntity) || ArmorTweaks.bools.get(ArmorTweaks.vanillaEnchKey);
    }

    // Small shortcut
    private int get(String key) {
        return ArmorTweaks.ints.get(key);
    }

    // Modify armor protection
    @Redirect(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    protected float applyArmorToDamage(float damage, float armor, float armorToughness) {
        if (!useVanillaArmor()) {
            armor = get(ArmorTweaks.nakedBuffKey) + armor * ArmorTweaks.armorMultiplier / get(ArmorTweaks.armorNerfKey);
        }

        float protection = armor - (4f * damage) / (8f + armorToughness);
        protection = MathHelper.clamp(protection, armor / 5f, 20f);
        return damage * (1f - protection / 25f);
    }

    // Modify armor enchantment protection
    @Redirect(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getInflictedDamage(FF)F"))
    protected float applyEnchantmentsToDamage(float damage, float protection) {
        if (useVanillaEnchantment()) {
            protection = MathHelper.clamp(protection, 0f, 20f);
            return damage * (1f - protection / 25f);
        }

        protection = Math.max(0f, protection);
        float nerf = (float)get(ArmorTweaks.eProtNerfKey);
        float llb = (float)get(ArmorTweaks.eProtLowBuffKey);
        float p = protection + llb;
        return damage * nerf / (nerf - llb * llb * llb + p * p * p);
    }

    // Code below sends players in combat a message every time they take damage.
    // Displays damage before armor calculations, before enchantment calculations and after enchantment calculations.

    float base;
    float armor;
    float enchantment;

    @Inject(at = @At("HEAD"), method = "applyArmorToDamage")
    protected void setBase(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.base = amount;
    }
    @Inject(at = @At("HEAD"), method = "modifyAppliedDamage")
    public void setAfterArmor(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.armor = amount;
    }
    @Inject(at = @At("RETURN"), method = "modifyAppliedDamage")
    public void setAfterEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        this.enchantment = amount;
    }
    @Inject(at = @At("RETURN"), method = "damage")
    protected void sendDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (!ArmorTweaks.bools.get(ArmorTweaks.sendFeedbackKey)) return;

        String message = "Base: " + base + " | Armor: " + armor + " | Enchantments: " + enchantment;
        message = "§7" + message.replaceAll("\\|", "§8|§7");

        if (getType() == EntityType.PLAYER) {
            ((PlayerEntity) (Object) this).sendMessage(Text.of("§c[\uD83D\uDEE1] " + message), false); //🛡
        }
        if (getAttacker() instanceof PlayerEntity) {
            ((PlayerEntity)getAttacker()).sendMessage(Text.of("§b[\uD83D\uDDE1] " + message), false); //🗡
        }
    }

}
