package com.orebit.mod.mixin;

import com.orebit.mod.FakePlayerEntity;
import com.orebit.mod.Orebit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class DebugLogging {
    @Inject(at = @At("HEAD"), method = "travel")
    private void travel(Vec3d movementInput, CallbackInfo info) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof FakePlayerEntity fakePlayer) {
//            Orebit.LOGGER.info("travel() called with input={}, inWater={}, onGround={}, velocity={}, isLogicalSideForUpdatingMovement={}",
//                movementInput,
//                fakePlayer.isTouchingWater(),
//                fakePlayer.isOnGround(),
//                fakePlayer.getVelocity(),
//                fakePlayer.isLogicalSideForUpdatingMovement()
//            );
        }
    }
}
