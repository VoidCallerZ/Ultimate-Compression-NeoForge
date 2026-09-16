package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.util.valueproviders.UniformInt;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;
import java.util.Map;

public class UCOres {

    public static final DeferredRegister.Blocks BLOCKS =
        DeferredRegister.createBlocks(ModConstants.MOD_ID);

    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(ModConstants.MOD_ID);

    public static final Map<String, DeferredHolder<Block, Block>> ALL_ORE_BLOCKS = new LinkedHashMap<>();

    private static final Object[][] ORES = {
        { "compressed_coal_ore",          MapColor.DEEPSLATE, 0,  2 },
        { "compressed_iron_ore",          MapColor.DEEPSLATE, 0,  0 },
        { "compressed_gold_ore",          MapColor.DEEPSLATE, 0,  0 },
        { "compressed_copper_ore",        MapColor.DEEPSLATE, 0,  0 },
        { "compressed_diamond_ore",       MapColor.DEEPSLATE, 9, 21 },
        { "compressed_emerald_ore",       MapColor.DEEPSLATE, 9, 21 },
        { "compressed_lapis_ore",         MapColor.DEEPSLATE, 6, 15 },
        { "compressed_redstone_ore",      MapColor.DEEPSLATE, 3, 15 },
        { "compressed_nether_quartz_ore", MapColor.NETHER,    2,  5 },
        { "compressed_nether_gold_ore",   MapColor.NETHER,    1,  2 },
    };

    static {
        for (Object[] ore : ORES) {
            String   name  = (String)   ore[0];
            MapColor color = (MapColor) ore[1];
            int      xpMin = (int)      ore[2];
            int      xpMax = (int)      ore[3];

            boolean isNether = name.contains("nether");
            // Build props used as the base — registerBlock passes setId'd props into factory
            BlockBehaviour.Properties baseProps = isNether
                ? BlockBehaviour.Properties.of()
                    .mapColor(color).sound(SoundType.NETHERRACK)
                    .strength(3.0f, 3.0f).requiresCorrectToolForDrops()
                : BlockBehaviour.Properties.of()
                    .mapColor(color).sound(SoundType.DEEPSLATE)
                    .strength(4.5f, 3.0f).requiresCorrectToolForDrops();

            DeferredHolder<Block, Block> block;
            if (xpMin == 0 && xpMax == 0) {
                block = BLOCKS.registerBlock(name, Block::new, () -> baseProps);
            } else {
                final int fMin = xpMin, fMax = xpMax;
                block = BLOCKS.registerBlock(name,
                    props -> new DropExperienceBlock(UniformInt.of(fMin, fMax), props),
                    () -> baseProps);
            }

            ALL_ORE_BLOCKS.put(name, block);

            // registerSimpleBlockItem handles setId automatically for the item
            ITEMS.registerSimpleBlockItem(name, block);
        }
    }
}