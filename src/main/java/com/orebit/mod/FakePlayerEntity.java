package com.orebit.mod;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class FakePlayerEntity extends ServerPlayerEntity {

    public FakePlayerEntity(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions options) {
        super(server, world, profile, options);
        this.networkHandler = new FakeNetworkHandler(server, this);

        this.setNoGravity(false);
    }

    @Override
    public void tick() {
        super.baseTick();
        this.setNoGravity(false);
        this.tickMovement();
    }

}
