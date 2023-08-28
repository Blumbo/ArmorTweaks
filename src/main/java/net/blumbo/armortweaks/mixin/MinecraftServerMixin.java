package net.blumbo.armortweaks.mixin;

import net.blumbo.armortweaks.ArmorTweaks;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Unique
    boolean firstTickPassed = false;

    @Inject(at = @At("HEAD"), method = "tick")
    private void tickHead(CallbackInfo ci) {
        if (!firstTickPassed) {
            firstTickPassed = true;
            ArmorTweaks.firstTick((MinecraftServer)(Object)this);
        }
    }

    @Inject(at = @At("HEAD"), method = "save")
    private void save(CallbackInfoReturnable<Boolean> cir) {
        ArmorTweaks.saveValues();
    }
}
