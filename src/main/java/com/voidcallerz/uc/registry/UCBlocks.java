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

    public static final DeferredRegister.Blocks BLOCKS =
        DeferredRegister.createBlocks(ModConstants.MOD_ID);

    public static final Map<String, DeferredHolder<Block, Block>> ALL_BLOCKS = new LinkedHashMap<>();

    private static final Set<String> LOG_MATERIALS = Set.of(
        "oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log",
        "dark_oak_log", "mangrove_log", "cherry_log", "bamboo_block",
        "crimson_stem", "warped_stem", "basalt", "pale_oak_log"
    );

    private static final Set<String> FALLING_MATERIALS = Set.of(
        "sand", "gravel", "soul_sand", "black_concrete_powder", "blue_concrete_powder", "brown_concrete_powder",
        "cyan_concrete_powder", "gray_concrete_powder", "green_concrete_powder", "light_blue_concrete_powder", 
        "light_gray_concrete_powder", "lime_concrete_powder", "magenta_concrete_powder", "orange_concrete_powder", 
        "pink_concrete_powder", "red_concrete_powder", "white_concrete_powder", "yellow_concrete_powder", "purple_concrete_powder",
        "red_sand"
    );

    private static final Set<String> NO_TOOL_REQUIRED = Set.of(
        "dirt", "sand", "gravel", "soul_sand", "soul_soil",
        "oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log",
        "dark_oak_log", "mangrove_log", "cherry_log", "bamboo_block",
        "crimson_stem", "warped_stem",
        "oak_planks", "spruce_planks", "birch_planks", "jungle_planks",
        "acacia_planks", "dark_oak_planks", "mangrove_planks", "cherry_planks",
        "crimson_planks", "warped_planks",
        "white_wool", "orange_wool", "magenta_wool", "light_blue_wool", "yellow_wool",
        "lime_wool", "pink_wool", "gray_wool", "light_gray_wool", "cyan_wool",
        "purple_wool", "blue_wool", "brown_wool", "green_wool", "red_wool", "black_wool",
        "black_concrete_powder", "blue_concrete_powder", "brown_concrete_powder",
        "cyan_concrete_powder", "gray_concrete_powder", "green_concrete_powder", "light_blue_concrete_powder", 
        "light_gray_concrete_powder", "lime_concrete_powder", "magenta_concrete_powder", "orange_concrete_powder", 
        "pink_concrete_powder", "red_concrete_powder", "white_concrete_powder", "yellow_concrete_powder", 
        "purple_concrete_powder", "red_sand", "glowstone", "ice", "packed_ice", "blue_ice",
        "clay", "snow_block", "moss_block", "pale_moss_block", "pale_oak_log", "pale_oak_planks", "resin_block",
        "oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves", "acacia_leaves",
        "dark_oak_leaves", "mangrove_leaves", "cherry_leaves", "pale_oak_leaves", "azalea_leaves", "flowering_azalea_leaves"
    );

    public static final Set<String> LEAVES_MATERIALS = Set.of(
        "oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves", "acacia_leaves",
        "dark_oak_leaves", "mangrove_leaves", "cherry_leaves", "pale_oak_leaves",
        "azalea_leaves", "flowering_azalea_leaves"
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
        { "andesite",       MapColor.STONE,         SoundType.STONE,    1.5f,  6.0f },
        { "diorite",        MapColor.SNOW,          SoundType.STONE,    1.5f,  6.0f },
        { "granite",        MapColor.COLOR_ORANGE,  SoundType.STONE,    1.5f,  6.0f },
        { "red_sand",       MapColor.COLOR_ORANGE,  SoundType.SAND,     0.5f,  0.5f },
        { "cobbled_deepslate", MapColor.DEEPSLATE,     SoundType.DEEPSLATE, 2.0f, 6.0f },
        { "basalt",         MapColor.COLOR_BLACK,   SoundType.BASALT,   1.5f,  6.0f },
        { "glowstone",      MapColor.COLOR_YELLOW,  SoundType.GLASS,    0.3f,  1.0f },
        { "magma_block",    MapColor.COLOR_RED,     SoundType.STONE,    1.5f,  6.0f },
        { "quartz_block",   MapColor.QUARTZ,       SoundType.STONE,    0.8f,  4.0f },

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

        // --- Concrete ---
        { "black_concrete",   MapColor.COLOR_BLACK,   SoundType.STONE, 1.8f, 6.0f },
        { "blue_concrete",    MapColor.COLOR_BLUE,    SoundType.STONE, 1.8f, 6.0f },
        { "brown_concrete",   MapColor.COLOR_BROWN,   SoundType.STONE, 1.8f, 6.0f },
        { "cyan_concrete",    MapColor.COLOR_CYAN,    SoundType.STONE, 1.8f, 6.0f },
        { "gray_concrete",    MapColor.COLOR_GRAY,    SoundType.STONE, 1.8f, 6.0f },
        { "green_concrete",   MapColor.COLOR_GREEN,   SoundType.STONE, 1.8f, 6.0f },
        { "light_blue_concrete", MapColor.COLOR_LIGHT_BLUE, SoundType.STONE, 1.8f, 6.0f },
        { "light_gray_concrete", MapColor.COLOR_LIGHT_GRAY, SoundType.STONE, 1.8f, 6.0f },
        { "lime_concrete",    MapColor.COLOR_LIGHT_GREEN, SoundType.STONE, 1.8f, 6.0f },
        { "magenta_concrete", MapColor.COLOR_MAGENTA, SoundType.STONE, 1.8f, 6.0f },
        { "orange_concrete",  MapColor.COLOR_ORANGE,  SoundType.STONE, 1.8f, 6.0f },
        { "pink_concrete",    MapColor.COLOR_PINK,    SoundType.STONE, 1.8f, 6.0f },
        { "red_concrete",     MapColor.COLOR_RED,     SoundType.STONE, 1.8f, 6.0f },
        { "white_concrete",   MapColor.SNOW,         SoundType.STONE, 1.8f, 6.0f },
        { "yellow_concrete",  MapColor.COLOR_YELLOW, SoundType.STONE, 1.8f, 6.0f },
        { "purple_concrete",  MapColor.COLOR_PURPLE, SoundType.STONE, 1.8f, 6.0f },

        // --- Concrete Powder ---
        { "black_concrete_powder",   MapColor.COLOR_BLACK,   SoundType.GRAVEL, 0.5f, 0.5f },
        { "blue_concrete_powder",    MapColor.COLOR_BLUE,    SoundType.GRAVEL, 0.5f, 0.5f },
        { "brown_concrete_powder",   MapColor.COLOR_BROWN,   SoundType.GRAVEL, 0.5f, 0.5f },
        { "cyan_concrete_powder",    MapColor.COLOR_CYAN,    SoundType.GRAVEL, 0.5f, 0.5f },
        { "gray_concrete_powder",    MapColor.COLOR_GRAY,    SoundType.GRAVEL, 0.5f, 0.5f },
        { "green_concrete_powder",   MapColor.COLOR_GREEN,   SoundType.GRAVEL, 0.5f, 0.5f },
        { "light_blue_concrete_powder", MapColor.COLOR_LIGHT_BLUE, SoundType.GRAVEL, 0.5f, 0.5f },
        { "light_gray_concrete_powder", MapColor.COLOR_LIGHT_GRAY, SoundType.GRAVEL, 0.5f, 0.5f },
        { "lime_concrete_powder",    MapColor.COLOR_LIGHT_GREEN, SoundType.GRAVEL, 0.5f, 0.5f },
        { "magenta_concrete_powder", MapColor.COLOR_MAGENTA, SoundType.GRAVEL, 0.5f, 0.5f },
        { "orange_concrete_powder",  MapColor.COLOR_ORANGE,  SoundType.GRAVEL, 0.5f, 0.5f },
        { "pink_concrete_powder",    MapColor.COLOR_PINK,    SoundType.GRAVEL, 0.5f, 0.5f },
        { "red_concrete_powder",     MapColor.COLOR_RED,     SoundType.GRAVEL, 0.5f, 0.5f },
        { "white_concrete_powder",   MapColor.SNOW,         SoundType.GRAVEL, 0.5f, 0.5f },
        { "yellow_concrete_powder",  MapColor.COLOR_YELLOW, SoundType.GRAVEL, 0.5f, 0.5f },
        { "purple_concrete_powder",  MapColor.COLOR_PURPLE, SoundType.GRAVEL, 0.5f, 0.5f },

        // --- Natural Blocks ---
        { "sandstone",      MapColor.SAND,          SoundType.STONE,    0.8f,  4.0f },
        { "red_sandstone",  MapColor.COLOR_ORANGE,  SoundType.STONE,    0.8f,  4.0f },
        { "ice",            MapColor.ICE,           SoundType.GLASS,    0.5f,  2.5f },
        { "packed_ice",     MapColor.ICE,           SoundType.GLASS,    0.5f,  2.5f },
        { "blue_ice",       MapColor.ICE,           SoundType.GLASS,    0.5f,  2.5f },
        { "clay",          MapColor.COLOR_ORANGE,  SoundType.GRAVEL,   0.6f,  0.6f },
        { "snow_block",     MapColor.SNOW,         SoundType.SNOW,     0.2f,  0.5f },
        { "moss_block",     MapColor.COLOR_GREEN,      SoundType.GRASS,    0.8f,  4.0f },
        { "end_stone",      MapColor.SAND,          SoundType.STONE,    3.0f, 9.0f },

        // --- Leaves ---
        { "oak_leaves",     MapColor.COLOR_GREEN,   SoundType.GRASS,    0.2f, 0.2f },
        { "spruce_leaves",  MapColor.COLOR_GREEN,   SoundType.GRASS,    0.2f, 0.2f },
        { "birch_leaves",   MapColor.COLOR_GREEN,   SoundType.GRASS,    0.2f, 0.2f },
        { "jungle_leaves",  MapColor.COLOR_GREEN,   SoundType.GRASS,    0.2f, 0.2f },
        { "acacia_leaves",  MapColor.COLOR_GREEN,   SoundType.GRASS,    0.2f, 0.2f },
        { "dark_oak_leaves",MapColor.COLOR_GREEN,   SoundType.GRASS,    0.2f, 0.2f },
        { "mangrove_leaves",MapColor.COLOR_GREEN,   SoundType.GRASS,    0.2f, 0.2f },
        { "cherry_leaves",  MapColor.TERRACOTTA_WHITE, SoundType.CHERRY_LEAVES, 0.2f, 0.2f },
        { "pale_oak_leaves",  MapColor.COLOR_LIGHT_GREEN, SoundType.GRASS, 0.2f, 0.2f },
        { "azalea_leaves",  MapColor.COLOR_GREEN,   SoundType.GRASS,    0.2f, 0.2f },
        { "flowering_azalea_leaves", MapColor.COLOR_GREEN, SoundType.GRASS, 0.2f, 0.2f },

        // --- New in 1.21.4 ---
        { "pale_moss_block", MapColor.COLOR_LIGHT_GREEN, SoundType.GRASS, 0.8f, 4.0f },
        { "pale_oak_log", MapColor.COLOR_LIGHT_GREEN, SoundType.WOOD, 2.0f, 2.0f },
        { "pale_oak_planks", MapColor.COLOR_LIGHT_GREEN, SoundType.WOOD, 2.0f, 3.0f },
        { "resin_block", MapColor.COLOR_YELLOW, SoundType.STONE, 1.5f, 6.0f }
    };

    private static BlockBehaviour.Properties buildProps(
            MapColor color, SoundType sound,
            float hardness, float resistance, boolean needsTool) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
            .mapColor(color).sound(sound).strength(hardness, resistance);
        return needsTool ? props.requiresCorrectToolForDrops() : props;
    }

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
            boolean isLeaves  = LEAVES_MATERIALS.contains(baseName);

            for (int tier = 0; tier < ModConstants.TIER_COUNT; tier++) {
                String registryName  = ModConstants.TIER_PREFIXES[tier] + "_" + baseName;
                float tierMultiplier = (float) Math.pow(2, tier);

                // In 1.21.2, registerBlock() automatically calls setId() on properties
                final float      fMult       = tierMultiplier;
                final boolean    fNeedsTool  = needsTool;
                final boolean    fIsLog      = isLog;
                final boolean    fIsFalling  = isFalling;
                final boolean    fIsLeaves   = isLeaves;
                final MapColor   fColor      = color;
                final SoundType  fSound      = sound;
                final float      fHardness   = hardness;
                final float      fResistance = resistance;

                DeferredHolder<Block, Block> block;

                if (fIsLog) {
                    block = BLOCKS.registerBlock(registryName,
                        RotatedPillarBlock::new,
                        () -> buildProps(fColor, fSound, fHardness * fMult, fResistance * fMult, fNeedsTool));
                } else if (fIsFalling) {
                    block = BLOCKS.registerBlock(registryName,
                        UCFallingBlock::new,
                        () -> buildProps(fColor, fSound, fHardness * fMult, fResistance * fMult, fNeedsTool));
                } else if (fIsLeaves) {
                    block = BLOCKS.registerBlock(registryName,
                        Block::new,
                        () -> buildProps(fColor, fSound, fHardness * fMult, fResistance * fMult, fNeedsTool).noOcclusion());
                } else {
                    block = BLOCKS.registerBlock(registryName,
                        Block::new,
                        () -> buildProps(fColor, fSound, fHardness * fMult, fResistance * fMult, fNeedsTool));
                }

                ALL_BLOCKS.put(registryName, block);
            }
        }
    }
}