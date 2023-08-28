package net.blumbo.armortweaks.misc;

import net.blumbo.armortweaks.ArmorTweaks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.text.Text;

public class PearlDamageSource extends DamageSource {

    public static final DamageSource PEARL = (new PearlDamageSource("pearl")); //.setBypassesArmor();

    public PearlDamageSource(String name) {
        super(new DamageSources(ArmorTweaks.server.getRegistryManager()).magic().getTypeRegistryEntry());
        // Setting it to magic instead of fall basically bypasses armor, also it crashes if using the name (string)
    }

    // Copied from DamageSource.class, using "fall" instead of "pearl" in death message
    public Text getDeathMessage(LivingEntity entity) {
        LivingEntity livingEntity = entity.getPrimeAdversary();
        String string = "death.attack.fall";
        String string2 = string + ".player";
        return livingEntity != null ? Text.translatable(string2, entity.getDisplayName(), livingEntity.getDisplayName()) : Text.translatable(string, entity.getDisplayName());
    }

}
