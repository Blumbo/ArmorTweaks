package net.blumbo.armortweaks.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract Iterable<ItemStack> getHandSlots();

    // If victim is in water check
    boolean inWater = false;
    @Inject(at = @At("HEAD"), method = "attack")
    protected void attack(Entity entity, CallbackInfo ci) {
        inWater = entity.isInWater();
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D"))
    protected double setAttackDamage(Player instance, Attribute attribute) {

        double value = this.getAttributes().getValue(attribute);

        ItemStack itemStack = getMainHandItem();
        if (itemStack == null) return value;

        Item item = itemStack.getItem();

        // Buff swords and axes to what they were in 1.16, I think picks and shovels worked differently but when asked
        if (item instanceof SwordItem || item instanceof AxeItem ||
                item instanceof PickaxeItem || item instanceof ShovelItem) {
            String type = item.toString().split("_")[0];
            if (!type.equals("wooden") && !type.equals("golden")) value += 1;
        }

        // Buff trident damage, nerf impaling
        if (item instanceof TridentItem) {
            value += 1;
            int impaling = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.IMPALING, itemStack);
            if (inWater && impaling > 0) {
                value += 1 - impaling * 1.5;
            }
        }

        return value;
    }


}
