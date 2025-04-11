package com.orebit.mod.mixin;

import com.orebit.mod.FakePlayerEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mixin(LivingEntity.class)
public class DebugLogging {
    private static final Logger LOGGER = LogManager.getLogger("DebugLogging");

    @Inject(at = @At("HEAD"), method = "travel")
    private void travel(Vec3d movementInput, CallbackInfo info) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof FakePlayerEntity) {
            LOGGER.debug("FakePlayerEntity traveling with input: " + movementInput);
        }
    }
}
