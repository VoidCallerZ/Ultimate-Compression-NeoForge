package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import com.voidcallerz.uc.UCFallingBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class UCBlocks {

    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.BLOCK, ModConstants.MOD_ID);

    public static final Map<String, DeferredHolder<Block, Block>> ALL_BLOCKS = new LinkedHashMap<>();

    private static final Set<String> LOG_MATERIALS = Set.of(
        "oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log",
        "dark_oak_log", "mangrove_log", "cherry_log", "bamboo_block",
        "crimson_stem", "warped_stem"
    );

    // -------------------------------------------------------------------------
    // Falling materials — registered as FallingBlock so they obey gravity.
    // Must match vanilla behaviour (sand, gravel, etc.)
    // -------------------------------------------------------------------------
    private static final Set<String> FALLING_MATERIALS = Set.of(
        "sand", "gravel", "soul_sand"
    );

    private static final Set<String> NO_TOOL_REQUIRED = Set.of(
        "dirt", "sand", "gravel", "soul_sand", "soul_soil", "netherrack",
        "oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log",
        "dark_oak_log", "mangrove_log", "cherry_log", "bamboo_block",
        "crimson_stem", "warped_stem",
        "oak_planks", "spruce_planks", "birch_planks", "jungle_planks",
        "acacia_planks", "dark_oak_planks", "mangrove_planks", "cherry_planks",
        "crimson_planks", "warped_planks",
        "white_wool", "orange_wool", "magenta_wool", "light_blue_wool", "yellow_wool",
        "lime_wool", "pink_wool", "gray_wool", "light_gray_wool", "cyan_wool",
        "purple_wool", "blue_wool", "brown_wool", "green_wool", "red_wool", "black_wool"
    );

    private static final Object[][] MATERIALS = {

        // --- Vanilla blocks ---
        { "stone",          MapColor.STONE,         SoundType.STONE,    1.5f,  6.0f },
        { "cobblestone",    MapColor.STONE,         SoundType.STONE,    2.0f,  6.0f },
        { "dirt",           MapColor.DIRT,          SoundType.GRAVEL,   0.5f,  0.5f },
        { "sand",           MapColor.SAND,          SoundType.SAND,     0.5f,  0.5f },
        { "gravel",         MapColor.STONE,         SoundType.GRAVEL,   0.6f,  0.6f },
        { "netherrack",     MapColor.NETHER,        SoundType.NETHERRACK, 0.4f, 0.4f },
        { "soul_sand",      MapColor.COLOR_BROWN,   SoundType.SOUL_SAND, 0.5f, 0.5f },
        { "soul_soil",      MapColor.COLOR_BROWN,   SoundType.SOUL_SOIL, 1.0f, 1.0f },
        { "blackstone",     MapColor.COLOR_BLACK,   SoundType.STONE,    1.5f,  6.0f },
        { "deepslate",      MapColor.DEEPSLATE,     SoundType.DEEPSLATE, 3.0f, 6.0f },
        { "calcite",        MapColor.TERRACOTTA_WHITE, SoundType.CALCITE, 0.75f, 0.75f },
        { "tuff",           MapColor.TERRACOTTA_GRAY,  SoundType.TUFF,   1.5f,  6.0f },
        { "obsidian",       MapColor.COLOR_BLACK,   SoundType.STONE,    50.0f, 1200.0f },

        // --- Ore blocks ---
        { "iron_block",     MapColor.METAL,         SoundType.METAL,    5.0f,  6.0f },
        { "gold_block",     MapColor.GOLD,          SoundType.METAL,    3.0f,  6.0f },
        { "diamond_block",  MapColor.DIAMOND,       SoundType.METAL,    5.0f,  6.0f },
        { "emerald_block",  MapColor.EMERALD,       SoundType.METAL,    5.0f,  6.0f },
        { "lapis_block",    MapColor.LAPIS,         SoundType.STONE,    3.0f,  3.0f },
        { "redstone_block", MapColor.FIRE,          SoundType.STONE,    5.0f,  6.0f },
        { "coal_block",     MapColor.COLOR_BLACK,   SoundType.STONE,    5.0f,  6.0f },
        { "copper_block",   MapColor.COLOR_ORANGE,  SoundType.COPPER,   3.0f,  6.0f },
        { "netherite_block",MapColor.COLOR_BLACK,   SoundType.NETHERITE_BLOCK, 50.0f, 1200.0f },
        { "amethyst_block", MapColor.COLOR_PURPLE,  SoundType.AMETHYST, 1.5f,  1.5f },
        { "raw_iron_block", MapColor.RAW_IRON,      SoundType.STONE,    5.0f,  6.0f },
        { "raw_gold_block", MapColor.GOLD,          SoundType.STONE,    5.0f,  6.0f },
        { "raw_copper_block",MapColor.COLOR_ORANGE, SoundType.STONE,    5.0f,  6.0f },

        // --- Wood / logs ---
        { "oak_log",        MapColor.WOOD,          SoundType.WOOD,     2.0f,  2.0f },
        { "spruce_log",     MapColor.PODZOL,        SoundType.WOOD,     2.0f,  2.0f },
        { "birch_log",      MapColor.SAND,          SoundType.WOOD,     2.0f,  2.0f },
        { "jungle_log",     MapColor.DIRT,          SoundType.WOOD,     2.0f,  2.0f },
        { "acacia_log",     MapColor.COLOR_ORANGE,  SoundType.WOOD,     2.0f,  2.0f },
        { "dark_oak_log",   MapColor.COLOR_BROWN,   SoundType.WOOD,     2.0f,  2.0f },
        { "mangrove_log",   MapColor.COLOR_RED,     SoundType.WOOD,     2.0f,  2.0f },
        { "cherry_log",     MapColor.TERRACOTTA_WHITE, SoundType.CHERRY_LEAVES, 2.0f, 2.0f },
        { "bamboo_block",   MapColor.COLOR_YELLOW,  SoundType.BAMBOO_WOOD, 2.0f, 2.0f },
        { "crimson_stem",   MapColor.COLOR_RED,     SoundType.WOOD,     2.0f,  2.0f },
        { "warped_stem",    MapColor.COLOR_BLUE,    SoundType.WOOD,     2.0f,  2.0f },

        // --- Planks ---
        { "oak_planks",     MapColor.WOOD,          SoundType.WOOD,     2.0f,  3.0f },
        { "spruce_planks",  MapColor.PODZOL,        SoundType.WOOD,     2.0f,  3.0f },
        { "birch_planks",   MapColor.SAND,          SoundType.WOOD,     2.0f,  3.0f },
        { "jungle_planks",  MapColor.DIRT,          SoundType.WOOD,     2.0f,  3.0f },
        { "acacia_planks",  MapColor.COLOR_ORANGE,  SoundType.WOOD,     2.0f,  3.0f },
        { "dark_oak_planks",MapColor.COLOR_BROWN,   SoundType.WOOD,     2.0f,  3.0f },
        { "mangrove_planks",MapColor.COLOR_RED,     SoundType.WOOD,     2.0f,  3.0f },
        { "cherry_planks",  MapColor.TERRACOTTA_WHITE, SoundType.WOOD,  2.0f,  3.0f },
        { "crimson_planks", MapColor.COLOR_RED,     SoundType.WOOD,     2.0f,  3.0f },
        { "warped_planks",  MapColor.COLOR_BLUE,    SoundType.WOOD,     2.0f,  3.0f },

        // --- Wool ---
        { "white_wool",      MapColor.SNOW,             SoundType.WOOL, 0.8f, 4.0f },
        { "orange_wool",     MapColor.COLOR_ORANGE,     SoundType.WOOL, 0.8f, 4.0f },
        { "magenta_wool",    MapColor.COLOR_MAGENTA,    SoundType.WOOL, 0.8f, 4.0f },
        { "light_blue_wool", MapColor.COLOR_LIGHT_BLUE, SoundType.WOOL, 0.8f, 4.0f },
        { "yellow_wool",     MapColor.COLOR_YELLOW,     SoundType.WOOL, 0.8f, 4.0f },
        { "lime_wool",       MapColor.COLOR_LIGHT_GREEN,SoundType.WOOL, 0.8f, 4.0f },
        { "pink_wool",       MapColor.COLOR_PINK,       SoundType.WOOL, 0.8f, 4.0f },
        { "gray_wool",       MapColor.COLOR_GRAY,       SoundType.WOOL, 0.8f, 4.0f },
        { "light_gray_wool", MapColor.COLOR_LIGHT_GRAY, SoundType.WOOL, 0.8f, 4.0f },
        { "cyan_wool",       MapColor.COLOR_CYAN,       SoundType.WOOL, 0.8f, 4.0f },
        { "purple_wool",     MapColor.COLOR_PURPLE,     SoundType.WOOL, 0.8f, 4.0f },
        { "blue_wool",       MapColor.COLOR_BLUE,       SoundType.WOOL, 0.8f, 4.0f },
        { "brown_wool",      MapColor.COLOR_BROWN,      SoundType.WOOL, 0.8f, 4.0f },
        { "green_wool",      MapColor.COLOR_GREEN,      SoundType.WOOL, 0.8f, 4.0f },
        { "red_wool",        MapColor.COLOR_RED,        SoundType.WOOL, 0.8f, 4.0f },
        { "black_wool",      MapColor.COLOR_BLACK,      SoundType.WOOL, 0.8f, 4.0f },
    };

    static {
        for (Object[] mat : MATERIALS) {
            String baseName  = (String)    mat[0];
            MapColor color   = (MapColor)  mat[1];
            SoundType sound  = (SoundType) mat[2];
            float hardness   = (float)     mat[3];
            float resistance = (float)     mat[4];

            boolean isLog     = LOG_MATERIALS.contains(baseName);
            boolean isFalling = FALLING_MATERIALS.contains(baseName);
            boolean needsTool = !NO_TOOL_REQUIRED.contains(baseName);

            for (int tier = 0; tier < ModConstants.TIER_COUNT; tier++) {
                String registryName  = ModConstants.TIER_PREFIXES[tier] + "_" + baseName;
                float tierMultiplier = (float) Math.pow(2, tier);

                // Build base properties first
                BlockBehaviour.Properties baseProps = BlockBehaviour.Properties.of()
                    .mapColor(color)
                    .sound(sound)
                    .strength(hardness * tierMultiplier, resistance * tierMultiplier);

                // Use a separate final variable for the lambda — Java requires
                // variables captured in lambdas to be effectively final,
                // so we can't reassign baseProps directly.
                final BlockBehaviour.Properties finalProps = needsTool
                    ? baseProps.requiresCorrectToolForDrops()
                    : baseProps;

                DeferredHolder<Block, Block> block;

                if (isLog) {
                    block = BLOCKS.register(registryName,
                        () -> new RotatedPillarBlock(finalProps));
                } else if (isFalling) {
                    block = BLOCKS.register(registryName,
                        () -> new UCFallingBlock(finalProps));
                } else {
                    block = BLOCKS.register(registryName,
                        () -> new Block(finalProps));
                }

                ALL_BLOCKS.put(registryName, block);
            }
        }
    }
}