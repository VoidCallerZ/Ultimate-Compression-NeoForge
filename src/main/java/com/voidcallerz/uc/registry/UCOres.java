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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.Map;

public class UCOres {

    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.BLOCK, ModConstants.MOD_ID);

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.ITEM, ModConstants.MOD_ID);

    public static final Map<String, DeferredHolder<Block, Block>> ALL_ORE_BLOCKS = new LinkedHashMap<>();

    // -------------------------------------------------------------------------
    // Ore definitions
    // Each entry: registry_name, MapColor, xpMin, xpMax
    //
    // All ores use deepslate sound/hardness since they only spawn at
    // deepslate depth. Harder to mine than vanilla deepslate ores.
    //
    // xpMin/xpMax = 0 for raw-drop ores (iron, gold, copper) since
    // those give no XP in vanilla. Gem ores get 3x vanilla XP.
    // -------------------------------------------------------------------------
    private static final Object[][] ORES = {
        { "compressed_coal_ore",     MapColor.DEEPSLATE, 0,  2 },
        { "compressed_iron_ore",     MapColor.DEEPSLATE, 0,  0 },
        { "compressed_gold_ore",     MapColor.DEEPSLATE, 0,  0 },
        { "compressed_copper_ore",   MapColor.DEEPSLATE, 0,  0 },
        { "compressed_diamond_ore",  MapColor.DEEPSLATE, 9, 21 },  // 3x vanilla (3-7)
        { "compressed_emerald_ore",  MapColor.DEEPSLATE, 9, 21 },
        { "compressed_lapis_ore",    MapColor.DEEPSLATE, 6, 15 },  // 3x vanilla (2-5)
        { "compressed_redstone_ore", MapColor.DEEPSLATE, 3, 15 },  // 3x vanilla (1-5)
    };

    static {
        for (Object[] ore : ORES) {
            String   name  = (String)   ore[0];
            MapColor color = (MapColor) ore[1];
            int      xpMin = (int)      ore[2];
            int      xpMax = (int)      ore[3];

            BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(color)
                .sound(SoundType.DEEPSLATE)
                .strength(4.5f, 3.0f)
                .requiresCorrectToolForDrops();

            DeferredHolder<Block, Block> block;

            if (xpMin == 0 && xpMax == 0) {
                block = BLOCKS.register(name, () -> new Block(props));
            } else {
                final int finalMin = xpMin;
                final int finalMax = xpMax;
                block = BLOCKS.register(name,
                    () -> new DropExperienceBlock(UniformInt.of(finalMin, finalMax), props));
            }

            ALL_ORE_BLOCKS.put(name, block);

            // Register corresponding BlockItem
            DeferredHolder<Block, Block> finalBlock = block;
            ITEMS.register(name, () -> new BlockItem(finalBlock.get(), new Item.Properties()));
        }
    }
}