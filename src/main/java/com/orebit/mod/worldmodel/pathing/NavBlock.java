package com.orebit.mod.worldmodel.pathing;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class NavBlock {

    /*
     * NavBlock methods
     */
    public boolean isDirectional() {
        return directional;
    }

    public boolean isWaterlogged() {
        return waterlogged;
    }

    /*
     * Must be defined before calling register() below
     */
    public static final int MAX_ENTRIES = 256;
    private static final Map<Block, Byte> blockToIndex = new HashMap<>();
    private static final Map<NavBlock, Byte> navBlockToIndex = new HashMap<>();
    private static final NavBlock[] indexToNavBlock = new NavBlock[MAX_ENTRIES];
    private static final Logger log = LoggerFactory.getLogger(NavBlock.class);
    private static byte nextNonDirectional = 0;
    private static byte nextDirectional = 0;

    /*
     * Main static registration block (run early)
     */
    // Natural > Soil
    public static final byte CLAY = register(
        NavBlock.builder()
            .hardness(0.6f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.CLAY
    );
    public static final byte DIRT = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT
    );
    public static final byte DIRT_PATH = register(
        NavBlock.builder()
            .hardness(0.6f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.DIRT_PATH
    );
    public static final byte GRASS_BLOCK = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.GRASS_BLOCK
    );
    public static final byte GRAVEL = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .gravity()
            .build(),
        Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL
    );
    public static final byte MUD = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.MUD
    );
    public static final byte MYCELIUM = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.MYCELIUM
    );
    public static final byte NYLIUM = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM
    );
    public static final byte PODZOL = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.PODZOL
    );
    public static final byte SAND = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .gravity()
            .build(),
        Blocks.SAND, Blocks.SUSPICIOUS_SAND
    );
    public static final byte SOUL_SAND = register(
        NavBlock.builder()
            .height(0.875f)
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .slowing()
            .build(),
        Blocks.SOUL_SAND
    );
    public static final byte SOUL_SOIL = register(
        NavBlock.builder()
            .hardness(0.5f)
            .tool(Tool.SHOVEL)
            .slowing()
            .build(),
        Blocks.SOUL_SOIL
    );

    // Natural > Stone
    public static final byte ANDESITE = register(
        NavBlock.builder()
            .build(),
        Blocks.ANDESITE, Blocks.POLISHED_ANDESITE
    );
    public static final byte BASALT = register(
        NavBlock.builder()
            .hardness(1.25f)
            .build(),
        Blocks.BASALT, Blocks.POLISHED_BASALT, Blocks.SMOOTH_BASALT
    );
    public static final byte BEDROCK = register(
        NavBlock.builder()
            .hardness(-1.0f)
            .build(),
        Blocks.BEDROCK
    );
    public static final byte BLACKSTONE = register(
        NavBlock.builder()
            .build(),
        Blocks.BLACKSTONE, Blocks.GILDED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE, Blocks.CHISELED_POLISHED_BLACKSTONE,
        Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
    );
    public static final byte CALCITE = register(
        NavBlock.builder()
            .hardness(0.75f)
            .build(),
        Blocks.CALCITE
    );
    public static final byte DEEPSLATE = register(
        NavBlock.builder()
            .hardness(3.5f)
            .build(),
        Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE, Blocks.CHISELED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE,
        Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_TILES
    );
    public static final byte DIORITE = register(
        NavBlock.builder()
            .build(),
        Blocks.DIORITE, Blocks.POLISHED_DIORITE
    );
    public static final byte POINTED_DRIPSTONE = register(
        NavBlock.builder()
            .height(0.6875f)
            .damaging()
            .build(),
        Blocks.POINTED_DRIPSTONE
    );
    public static final byte DRIPSTONE_BLOCK = register(
        NavBlock.builder()
            .build(),
        Blocks.DRIPSTONE_BLOCK
    );
    public static final byte END_STONE = register(
        NavBlock.builder()
            .hardness(3.0f)
            .build(),
        Blocks.END_STONE, Blocks.END_STONE_BRICKS
    );
    public static final byte GLOWSTONE = register(
        NavBlock.builder()
            .hardness(0.3f)
            .build(),
        Blocks.GLOWSTONE
    );
    public static final byte GRANITE = register(
        NavBlock.builder()
            .build(),
        Blocks.GRANITE, Blocks.POLISHED_GRANITE
    );
    public static final byte INFESTED_BLOCK = register(
        NavBlock.builder()
            .hardness(-1.0f) // Prevents bot from breaking infested blocks
            .build(),
        Blocks.INFESTED_STONE, Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS,
        Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.INFESTED_COBBLESTONE,
        Blocks.INFESTED_DEEPSLATE
    );
    public static final byte MAGMA_BLOCK = register(
        NavBlock.builder()
            .hardness(0.5f)
            .damaging()
            .build(),
        Blocks.MAGMA_BLOCK
    );
    public static final byte NETHERRACK = register(
        NavBlock.builder()
            .hardness(0.4f)
            .build(),
        Blocks.NETHERRACK
    );
    public static final byte OBSIDIAN = register(
        NavBlock.builder()
            .hardness(50.0f)
            .toolRequired()
            .build(),
        Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN
    );
    public static final byte SANDSTONE = register(
        NavBlock.builder()
            .hardness(0.8f)
            .build(),
        Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.RED_SANDSTONE,
        Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE
    );
    public static final byte SMOOTH_SANDSTONE = register(
        NavBlock.builder()
            .hardness(2.0f)
            .build(),
        Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE
    );
    public static final byte STONE = register(
        NavBlock.builder()
            .build(),
        Blocks.STONE, Blocks.STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS,
        Blocks.CRACKED_STONE_BRICKS
    );
    public static final byte COBBLESTONE = register(
        NavBlock.builder()
            .hardness(2.0f)
            .build(),
        Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE, Blocks.SMOOTH_STONE
    );
    public static final byte TERRACOTTA = register(
        NavBlock.builder()
            .hardness(1.25f)
            .build(),
        Blocks.TERRACOTTA,
        Blocks.BLACK_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.CYAN_TERRACOTTA,
        Blocks.GRAY_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
        Blocks.LIME_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.PINK_TERRACOTTA,
        Blocks.PURPLE_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
        Blocks.BLACK_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA,
        Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
        Blocks.LIME_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA,
        Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA
    );
    public static final byte TUFF = register(
        NavBlock.builder()
            .build(),
        Blocks.TUFF, Blocks.TUFF_BRICKS, Blocks.CHISELED_TUFF, Blocks.CHISELED_TUFF_BRICKS
    );

    // Natural > Ore/Mineral
    public static final byte SMALL_AMETHYST_BUD = register(
        NavBlock.builder()
            .height(0.1875f)
            .build(),
        Blocks.SMALL_AMETHYST_BUD
    );
    public static final byte MEDIUM_AMETHYST_BUD = register(
        NavBlock.builder()
            .height(0.25f)
            .build(),
        Blocks.MEDIUM_AMETHYST_BUD
    );
    public static final byte LARGE_AMETHYST_BUD = register(
        NavBlock.builder()
            .height(0.3125f)
            .build(),
        Blocks.LARGE_AMETHYST_BUD
    );
    public static final byte AMETHYST_CRYSTAL = register(
        NavBlock.builder()
            .height(0.4375f)
            .build(),
        Blocks.AMETHYST_CLUSTER
    );
    public static final byte AMETHYST_BLOCK = register(
        NavBlock.builder()
            .build(),
        Blocks.AMETHYST_BLOCK, Blocks.BUDDING_AMETHYST
    );
    public static final byte ANCIENT_DEBRIS = register(
        NavBlock.builder()
            .hardness(30.0f)
            .build(),
        Blocks.ANCIENT_DEBRIS
    );
    public static final byte ORE = register(
        NavBlock.builder()
            .hardness(3.0f)
            .toolRequired()
            .build(),
        Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE,
        Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE
    );
    public static final byte DEEPSLATE_ORE = register(
        NavBlock.builder()
            .hardness(4.5f)
            .toolRequired()
            .build(),
        Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
        Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
        Blocks.DEEPSLATE_REDSTONE_ORE
    );

    // Natural > Liquid/Related
    public static final byte BUBBLE_UP = register(
        NavBlock.builder()
            .fluid()
            .replaceable()
            .directional()
            .build(),
        Blocks.BUBBLE_COLUMN
    );
    public static final byte BUBBLE_DOWN = registerDirection(
        NavBlock.builder()
            .fluid()
            .replaceable()
            .directional()
            .build(),
        BUBBLE_UP,
        0b01
    );
    public static final byte ICE = register(
        NavBlock.builder()
            .hardness(0.5f)
            .toolRequired()
            .build(),
        Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE
    );
    public static final byte LAVA = register(
        NavBlock.builder()
            .fluid()
            .damaging()
            .replaceable()
            .build(),
        Blocks.LAVA
    );
    // Snow can have any height ranging from 0.125 to 1.000 in increments of 0.125
    // To account for this, we'll consider snow a "directional" block, with direction
    // mapping to height. Since we only allow 4 directions, we'll stick to 4 height
    // options: 0.25, 0.5, 0.75, and 1.0
    public static final byte SNOW_1 = register(
        NavBlock.builder()
            .height(0.25f)
            .hardness(0.1f)
            .tool(Tool.SHOVEL)
            .directional()
            .build(),
        Blocks.SNOW
    );
    public static final byte SNOW_2 = registerDirection(
        NavBlock.builder()
            .height(0.5f)
            .hardness(0.1f)
            .tool(Tool.SHOVEL)
            .directional()
            .build(),
        SNOW_1,
        0b01
    );
    public static final byte SNOW_3 = registerDirection(
        NavBlock.builder()
            .height(0.75f)
            .hardness(0.1f)
            .tool(Tool.SHOVEL)
            .directional()
            .build(),
        SNOW_1,
        0b10
    );
    public static final byte SNOW_4 = registerDirection(
        NavBlock.builder()
            .height(1.0f)
            .hardness(0.1f)
            .tool(Tool.SHOVEL)
            .directional()
            .build(),
        SNOW_1,
        0b11
    );
    public static final byte POWDERED_SNOW = register(
        NavBlock.builder()
            .damaging()
            .slowing()
            .intangible()
            .hardness(0.25f)
            .build(),
        Blocks.POWDER_SNOW
    );
    public static final byte SNOW_BLOCK = register(
        NavBlock.builder()
            .hardness(0.2f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.SNOW_BLOCK
    );
    public static final byte WATER = register(
        NavBlock.builder()
            .fluid()
            .replaceable()
            .build(),
        Blocks.WATER
    );
    public static final byte AIR = register(
        NavBlock.builder()
            .replaceable()
            .intangible()
            .build(),
        Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR
    );
    public static final byte FIRE = register(
        NavBlock.builder()
            .damaging()
            .intangible()
            .build(),
        Blocks.FIRE, Blocks.SOUL_FIRE
    );

    // Biota > Wood / Hypha
    public static final byte LOG = register(
        NavBlock.builder()
            .hardness(2.0f)
            .tool(Tool.AXE)
            .build(),
        Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG,
        Blocks.MANGROVE_LOG, Blocks.CHERRY_LOG,
        Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_JUNGLE_LOG,
        Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_MANGROVE_LOG,
        Blocks.STRIPPED_CHERRY_LOG
    );
    public static final byte WOOD = register(
        NavBlock.builder()
            .hardness(2.0f)
            .tool(Tool.AXE)
            .build(),
        Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD, Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD,
        Blocks.DARK_OAK_WOOD, Blocks.MANGROVE_WOOD, Blocks.CHERRY_WOOD,
        Blocks.STRIPPED_OAK_WOOD, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.STRIPPED_BIRCH_WOOD, Blocks.STRIPPED_ACACIA_WOOD,
        Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.STRIPPED_CHERRY_WOOD
    );
    public static final byte STEM = register(
        NavBlock.builder()
            .hardness(2.0f)
            .tool(Tool.AXE)
            .build(),
        Blocks.CRIMSON_STEM, Blocks.WARPED_STEM, Blocks.STRIPPED_CRIMSON_STEM, Blocks.STRIPPED_WARPED_STEM
    );
    public static final byte HYPHAE = register(
        NavBlock.builder()
            .hardness(2.0f)
            .tool(Tool.AXE)
            .build(),
        Blocks.CRIMSON_HYPHAE, Blocks.WARPED_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE
    );
    public static final byte AZALEA = register(
        NavBlock.builder()
            .hardness(0f)
            .build(),
        Blocks.AZALEA, Blocks.FLOWERING_AZALEA
    );
    public static final byte BAMBOO = register(
        NavBlock.builder()
            .hardness(1.0f)
            .tool(Tool.SWORD)
            .build(),
        Blocks.BAMBOO, Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK
    );
    public static final byte BAMBOO_SHOOT = register(
        NavBlock.builder()
            .hardness(1.0f)
            .tool(Tool.SWORD)
            .intangible()
            .build(),
        Blocks.BAMBOO_SAPLING
    );
    public static final byte BEETROOT = register(
        NavBlock.builder()
            .hardness(0.0f)
            .intangible()
            .build(),
        Blocks.BEETROOTS
    );
    public static final byte BIG_DRIPLEAF = register(
        NavBlock.builder()
            .hardness(0.1f)
            .tool(Tool.AXE)
            .build(),
        Blocks.BIG_DRIPLEAF
    );
    public static final byte BIG_DRIPLEAF_STEM = register(
        NavBlock.builder()
            .hardness(0.1f)
            .tool(Tool.AXE)
            .intangible()
            .build(),
        Blocks.BIG_DRIPLEAF_STEM
    );
    public static final byte SMALL_DRIPLEAF = register(
        NavBlock.builder()
            .hardness(0f)
            .tool(Tool.SHEARS)
            .intangible()
            .build(),
        Blocks.SMALL_DRIPLEAF
    );
    public static final byte CACTUS = register(
        NavBlock.builder()
            .hardness(0.4f)
            .damaging()
            .build(),
        Blocks.CACTUS
    );
    public static final byte CARROTS = register(
        NavBlock.builder()
            .hardness(0.0f)
            .intangible()
            .build(),
        Blocks.CARROTS
    );
    public static final byte GLOW_BERRIES = register(
        NavBlock.builder()
            .hardness(0.0f)
            .intangible()
            .build(),
        Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT
    );
    public static final byte CHORUS_PLANT = register(
        NavBlock.builder()
            .hardness(0.4f)
            .tool(Tool.AXE)
            .build(),
        Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT
    );
    public static final byte COCOA = register(
        NavBlock.builder()
            .hardness(0.2f)
            .tool(Tool.AXE)
            .build(),
        Blocks.COCOA
    );
    public static final byte DEAD_BUSH = register(
        NavBlock.builder()
            .hardness(0f)
            .replaceable()
            .intangible()
            .build(),
        Blocks.DEAD_BUSH
    );
    public static final byte FERN = register(
        NavBlock.builder()
            .replaceable()
            .intangible()
            .build(),
        Blocks.FERN
    );
    public static final byte LARGE_FERN = register(
        NavBlock.builder()
            .height(2.0f)
            .replaceable()
            .intangible()
            .build(),
        Blocks.LARGE_FERN
    );
    public static final byte FLOWER = register(
        NavBlock.builder()
            .intangible()
            .build(),
        Blocks.POPPY, Blocks.DANDELION, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP,
        Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER,
        Blocks.LILY_OF_THE_VALLEY
    );
    public static final byte TALL_FLOWER = register(
        NavBlock.builder()
            .height(2.0f)
            .intangible()
            .build(),
        Blocks.SUNFLOWER, Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY
    );
    public static final byte TORCHFLOWER = register(
        NavBlock.builder()
            .intangible()
            .build(),
        Blocks.TORCHFLOWER, Blocks.TORCHFLOWER_CROP
    );
    public static final byte GRASS = register(
        NavBlock.builder()
            .replaceable()
            .intangible()
            .build(),
        Blocks.SHORT_GRASS
    );
    public static final byte TALL_GRASS = register(
        NavBlock.builder()
            .height(2.0f)
            .replaceable()
            .intangible()
            .build(),
        Blocks.TALL_GRASS
    );
    public static final byte HANGING_ROOTS = register(
        NavBlock.builder()
            .hardness(0.0f)
            .intangible()
            .tool(Tool.SHEARS)
            .build(),
        Blocks.HANGING_ROOTS
    );
    public static final byte LEAVES = register(
        NavBlock.builder()
            .hardness(0.2f)
            .tool(Tool.HOE)
            .build(),
        Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES,
        Blocks.DARK_OAK_LEAVES, Blocks.MANGROVE_LEAVES, Blocks.CHERRY_LEAVES
    );
    public static final byte LILY_PAD = register(
        NavBlock.builder()
            .height(0.09375f)
            .build(),
        Blocks.LILY_PAD
    );
    public static final byte MANGROVE_ROOTS = register(
        NavBlock.builder()
            .hardness(0.7f)
            .tool(Tool.AXE)
            .build(),
        Blocks.MANGROVE_ROOTS
    );
    public static final byte MUDDY_MANGROVE_ROOTS = register(
        NavBlock.builder()
            .hardness(0.7f)
            .tool(Tool.SHOVEL)
            .build(),
        Blocks.MUDDY_MANGROVE_ROOTS
    );
    public static final byte MELON = register(
        NavBlock.builder()
            .hardness(1.0f)
            .tool(Tool.AXE)
            .build(),
        Blocks.MELON
    );
    public static final byte MELON_STEM = register(
        NavBlock.builder()
            .hardness(0f)
            .intangible()
            .build(),
        Blocks.MELON_STEM, Blocks.ATTACHED_MELON_STEM
    );
    public static final byte MOSS_BLOCK = register(
        NavBlock.builder()
            .hardness(0.1f)
            .tool(Tool.HOE)
            .build(),
        Blocks.MOSS_BLOCK
    );
    public static final byte MOSS_CARPET = register(
        NavBlock.builder()
            .height(0.0625f)
            .hardness(0.1f)
            .tool(Tool.HOE)
            .build(),
        Blocks.MOSS_CARPET
    );
    public static final byte PINK_PETALS = register(
        NavBlock.builder()
            .height(0.125f)
            .hardness(0.0f)
            .intangible()
            .build(),
        Blocks.PINK_PETALS
    );
    public static final byte PITCHER_PLANT = register(
        NavBlock.builder()
            .height(2.0f)
            .intangible()
            .build(),
        Blocks.PITCHER_PLANT
    );
    public static final byte PITCHER_CROP = register(
        NavBlock.builder()
            .height(0.1875f)
            .hardness(0.0f)
            .build(),
        Blocks.PITCHER_CROP
    );
    public static final byte POTATOES = register(
        NavBlock.builder()
            .hardness(0.0f)
            .intangible()
            .build(),
        Blocks.POTATOES
    );
    public static final byte PUMPKIN = register(
        NavBlock.builder()
            .hardness(1.0f)
            .tool(Tool.AXE)
            .build(),
        Blocks.PUMPKIN, Blocks.CARVED_PUMPKIN
    );
    public static final byte PUMPKIN_STEM = register(
        NavBlock.builder()
            .hardness(0f)
            .intangible()
            .build(),
        Blocks.PUMPKIN_STEM, Blocks.ATTACHED_PUMPKIN_STEM
    );
    public static final byte SAPLING = register(
        NavBlock.builder()
            .hardness(0f)
            .intangible()
            .build(),
        Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING,
        Blocks.DARK_OAK_SAPLING, Blocks.MANGROVE_PROPAGULE, Blocks.CHERRY_SAPLING
    );

    // Directional blocks
    // Doors are defined by the corner of their hinge
    public static final byte WOOD_DOOR_NE = register(
        NavBlock.builder()
            .build()
    );

    /*
     * NavBlock class
     */
    private enum Tool {
        NONE, PICKAXE, AXE, SHOVEL, HOE, SWORD, SHEARS;
    }

    private final float height;
    private final EnumSet<Direction> solidFaces;
    private final EnumSet<Direction> closedFaces;
    private final boolean climbable;
    private final boolean fluid;
    private final boolean gravity;
    private final boolean damaging;
    private final boolean slowing;
    private final boolean replaceable;
    private final byte hardnessLog;
    private final Tool tool;
    private final boolean toolRequired;
    private final boolean directional;
    private final boolean waterlogged;

    private NavBlock(Builder builder) {
        // Fluids don't have solid faces
        if (builder.fluid) {
            builder.solidFaces = EnumSet.noneOf(Direction.class);
        }
        // If it's intangible, height is irrelevant
        this.height = builder.solidFaces.isEmpty() ? 1.0f : builder.height;
        this.solidFaces = builder.solidFaces;
        this.closedFaces = builder.closedFaces;
        this.climbable = builder.climbable;
        this.fluid = builder.fluid;
        this.gravity = builder.gravity;
        this.damaging = builder.damaging;
        this.slowing = builder.slowing;
        this.replaceable = builder.replaceable;
        this.hardnessLog = (byte) Math.max(0, Math.floor(Math.log(builder.hardness)/Math.log(2))+5);
        this.tool = hardnessLog > 1 ? builder.tool : Tool.NONE; // Weak blocks should just use hands
        this.toolRequired = hardnessLog > 1 && builder.toolRequired;
        this.directional = builder.directional;
        this.waterlogged = builder.waterlogged;
    }

    /*
     * NavBlock index
     */
    public static byte register(NavBlock navBlock, Block... blocks) {
        if (navBlockToIndex.containsKey(navBlock)) {
            return navBlockToIndex.get(navBlock);
        }

        int index, mode, local;
        if (navBlock.isDirectional()) {
            if (nextDirectional >= 16) throw new IllegalStateException("Too many directional blocks registered");
            mode = (nextDirectional / 8) << 5;
            local = nextDirectional % 8;
            index = (0b1000000 | mode | local);
            nextDirectional++;
        } else {
            if (nextNonDirectional >= 64) throw new IllegalStateException("Too many non-directional blocks registered");
            mode = (nextNonDirectional / 32) << 5;
            local = nextNonDirectional % 32;
            index = (mode | local);
            nextNonDirectional++;
        }

        if (navBlock.isWaterlogged()) {
            index |= 0b10000000;
        }

        navBlockToIndex.put(navBlock, (byte) index);
        indexToNavBlock[index & 0xFF] = navBlock;
        bulkMap((byte) index, blocks);
        return (byte) index;
    }

    public static byte registerDirection(NavBlock navBlock, byte baseIndex, int directionIndex) {
        if (baseIndex == 0) throw new IllegalStateException("Base block must be registered before direction");
        if ((baseIndex & 0b1000000) == 0) throw new IllegalStateException("Base block must be directional");
        int index = baseIndex | (directionIndex << 3);
        navBlockToIndex.put(navBlock, (byte) index);
        indexToNavBlock[index & 0xFF] = navBlock;
        return (byte) index;
    }

    private static void map(Block block, byte index) {
        blockToIndex.put(block, index);
    }

    private static void bulkMap(byte index, Block... blocks) {
        for (Block block : blocks) {
            map(block, index);
        }
    }

    public static byte getIndexForBlock(Block block) {
        return blockToIndex.getOrDefault(block, AIR);
    }

    public static NavBlock getNavBlockForIndex(byte index) {
        return indexToNavBlock[index & 0xFF];
    }

    public static Set<Map.Entry<Block, Byte>> blockMappings() {
        return blockToIndex.entrySet();
    }

    public static Set<NavBlock> getAllNavBlocks() {
        return navBlockToIndex.keySet();
    }

    /*
     * NavBlock builder
     */
    private static Builder builder() {
        return new Builder();
    }

    private static final class Builder {
        private float height = 1.0f;
        private EnumSet<Direction> solidFaces = EnumSet.allOf(Direction.class);
        private EnumSet<Direction> closedFaces = EnumSet.noneOf(Direction.class);
        private boolean climbable = false;
        private boolean fluid = false;
        private boolean gravity = false;
        private boolean damaging = false;
        private boolean slowing = false;
        private boolean replaceable = false;
        private float hardness = 1.5f;
        private Tool tool = Tool.PICKAXE;
        private boolean toolRequired = false;
        private boolean directional = false;
        private boolean waterlogged = false;

        public Builder height(double value) {
            this.height = (float) value;
            return this;
        }

        public Builder solidFaces(EnumSet<Direction> faces) {
            this.solidFaces = faces;
            return this;
        }

        public Builder closedFaces(EnumSet<Direction> faces) {
            this.closedFaces = faces;
            return this;
        }

        public Builder intangible() {
            return this.solidFaces(EnumSet.noneOf(Direction.class));
        }

        public Builder climbable(boolean value) {
            this.climbable = value;
            return this;
        }

        public Builder climbable() {
            return this.climbable(true);
        }

        public Builder fluid(boolean value) {
            this.fluid = value;
            return this;
        }

        public Builder fluid() {
            return this.fluid(true);
        }

        public Builder gravity(boolean value) {
            this.gravity = value;
            return this;
        }

        public Builder gravity() {
            return this.gravity(true);
        }

        public Builder damaging(boolean value) {
            this.damaging = value;
            return this;
        }

        public Builder damaging() {
            return this.damaging(true);
        }

        public Builder slowing(boolean value) {
            this.slowing = value;
            return this;
        }

        public Builder slowing() {
            return this.slowing(true);
        }

        public Builder replaceable(boolean value) {
            this.replaceable = value;
            return this;
        }

        public Builder replaceable() {
            return this.replaceable(true);
        }

        public Builder hardness(float value) {
            if (value < 0) this.hardness = Float.MAX_VALUE;
            else this.hardness = value;
            return this;
        }

        public Builder tool(Tool tool, boolean required) {
            this.tool = tool;
            this.toolRequired = required;
            return this;
        }

        public Builder tool(Tool tool) {
            return this.tool(tool, false);
        }

        public Builder toolRequired() {
            return this.tool(this.tool, true);
        }

        public Builder directional(boolean value) {
            this.directional = value;
            return this;
        }

        public Builder directional() {
            return this.directional(true);
        }

        public Builder waterlogged(boolean value) {
            this.waterlogged = value;
            return this;
        }

        public Builder waterlogged() {
            return this.waterlogged(true);
        }

        public NavBlock build() {
            return new NavBlock(this);
        }
    }

    /*
     * Specialized equality check for HashMaps
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NavBlock other)) return false;
        return Float.compare(height, other.height) == 0 &&
            Objects.equals(solidFaces, other.solidFaces) &&
            Objects.equals(closedFaces, other.closedFaces) &&
            fluid == other.fluid &&
            gravity == other.gravity &&
            damaging == other.damaging &&
            slowing == other.slowing &&
            replaceable == other.replaceable &&
            hardnessLog == other.hardnessLog &&
            tool == other.tool &&
            toolRequired == other.toolRequired &&
            directional == other.directional &&
            waterlogged == other.waterlogged;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, solidFaces, closedFaces, climbable, fluid, gravity, damaging, slowing, replaceable, hardnessLog, tool, toolRequired, waterlogged);
    }
}
