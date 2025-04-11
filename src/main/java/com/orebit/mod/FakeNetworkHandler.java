package com.orebit.mod;

import net.minecraft.SharedConstants;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
public class FakeNetworkHandler extends ServerPlayNetworkHandler {

    public FakeNetworkHandler(MinecraftServer server, ServerPlayerEntity player) {
        super(
                server,
                new FakeClientConnection(),
                player,
                new ConnectedClientData(
                        player.getGameProfile(),
                        SharedConstants.getProtocolVersion(),
                        player.getClientOptions(),
                        false
                )
        );
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        // Do nothing
    }
}
