package com.orebit.mod;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class AllyBotEntity extends FakePlayerEntity {

    private final PlayerEntity owner;

    public AllyBotEntity(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions options, PlayerEntity owner) {
        super(server, world, profile, options);
        this.owner = owner;
    }

    public void lookAtPlayer(PlayerEntity player) {
        double dx = player.getX() - this.getX();
        double dy = (player.getEyeY()) - this.getEyeY();
        double dz = player.getZ() - this.getZ();

        double distXZ = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float)(Math.toDegrees(Math.atan2(-dx, dz)));
        float pitch = (float)(Math.toDegrees(-Math.atan2(dy, distXZ)));

        this.setHeadYaw(yaw);     // where the head turns
        this.setYaw(yaw);         // body rotation
        this.setBodyYaw(yaw);     // optional for full facing
        this.setPitch(pitch);     // up/down looking
    }

    @Override
    public void tick() {
        super.tick();

        lookAtPlayer(owner);

        if (owner == this) return;

        // Face the player
        double dx = owner.getX() - this.getX();
        double dz = owner.getZ() - this.getZ();
        float yaw = (float)(Math.toDegrees(Math.atan2(-dx, dz)));
        this.setYaw(yaw);
        this.setBodyYaw(yaw);
        this.setHeadYaw(yaw);

        this.sidewaysSpeed = 0.0f;

        // Forward motion toward target
        float distance = (float)Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
        if (distance > 3) {
            this.forwardSpeed = 1.0f;
        } else {
            this.forwardSpeed = 0.0f;
        }

        if (this.isTouchingWater()) {
            this.upwardSpeed = 1.0f;
        } else {
            this.upwardSpeed = 0.0f;
        }

        // Jump if stuck
        Vec3d velocity = this.getVelocity();
        boolean stuck = distance > 3 && velocity.horizontalLengthSquared() < 0.001;

        if (stuck && this.isOnGround()) {
            this.jump();
        }

        this.tickMovement();
    }
}
