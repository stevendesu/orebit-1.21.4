package com.orebit.mod;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class BotManager {
    private static final Map<UUID, AllyBotEntity> botsByOwner = new HashMap<>();

    public static void spawnBotFor(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        ServerWorld world = (ServerWorld) player.getWorld();
        String baseName = player.getName().getString();
        if (baseName.length() > 12) {
            baseName = baseName.substring(0, 12);
        }
        String botName = baseName + "_bot";
        SyncedClientOptions options = player.getClientOptions();
        GameProfile profile = new GameProfile(UUID.randomUUID(), botName);

        AllyBotEntity bot = new AllyBotEntity(server, world, profile, options, player);
        BlockPos safeSpot = BotPositioning.findSafeSpotNear(player, 3);
        if (safeSpot != null) {
            bot.setPosition(safeSpot.getX() + 0.5, safeSpot.getY(), safeSpot.getZ() + 0.5);

            BotPositioning.faceEachOther(bot, player);
            BotPositioning.faceEachOther(player, bot);
        } else {
            bot.setPosition(player.getX(), player.getY(), player.getZ()); // fallback
        }
        bot.setCustomName(player.getDisplayName().copy().append("'s Bot"));
        bot.setCustomNameVisible(true);

        world.spawnEntity(bot);

        // Make visible to player
        player.networkHandler.sendPacket(new PlayerListS2CPacket(Action.ADD_PLAYER, bot));

        botsByOwner.put(player.getUuid(), bot);

        Orebit.LOGGER.info("[Orebit] Spawned bot for {}", player.getName().getString());
    }

    public static void removeBotFor(ServerPlayerEntity player) {
        AllyBotEntity bot = botsByOwner.remove(player.getUuid());
        if (bot != null && bot.isAlive()) {
            bot.kill((ServerWorld) bot.getWorld());
            bot.discard();
            Orebit.LOGGER.info("[Orebit] Removed bot for {}", player.getName().getString());
        }
    }

    public static void removeAllBots() {
        for (AllyBotEntity bot : botsByOwner.values()) {
            if (bot != null && bot.isAlive()) {
                bot.kill((ServerWorld) bot.getWorld());
                bot.discard();
            }
        }
        botsByOwner.clear();
    }
}
