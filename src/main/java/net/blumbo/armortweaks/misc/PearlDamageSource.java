package net.blumbo.armortweaks.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;

public class PearlDamageSource extends DamageSource {

    public static final DamageSource PEARL = (new PearlDamageSource("pearl")).setBypassesArmor();

    public PearlDamageSource(String name) {
        super(name);
    }

    // Copied from DamageSource.class, using "fall" instead of "pearl" in death message
    public Text getDeathMessage(LivingEntity entity) {
        LivingEntity livingEntity = entity.getPrimeAdversary();
        String string = "death.attack.fall";
        String string2 = string + ".player";
        return livingEntity != null ? Text.translatable(string2, new Object[]{entity.getDisplayName(), livingEntity.getDisplayName()}) : Text.translatable(string, new Object[]{entity.getDisplayName()});
    }

}
