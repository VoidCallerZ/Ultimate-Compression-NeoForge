package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;
import java.util.Map;

public class UCItems {

    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(ModConstants.MOD_ID);

    public static final Map<String, DeferredItem<BlockItem>> ALL_ITEMS = new LinkedHashMap<>();

    // -------------------------------------------------------------------------
    // Burnable block materials -> VANILLA burn time in ticks.
    // Compressed is 9x this value, double compressed 81x, so the vanilla
    // number is listed once and both tiers derive from it.
    // -------------------------------------------------------------------------
    private static final Map<String, Integer> FUEL_BLOCKS = new LinkedHashMap<>();

    static {
        // Logs (vanilla 300)
        FUEL_BLOCKS.put("oak_log",         300);
        FUEL_BLOCKS.put("spruce_log",      300);
        FUEL_BLOCKS.put("birch_log",       300);
        FUEL_BLOCKS.put("jungle_log",      300);
        FUEL_BLOCKS.put("acacia_log",      300);
        FUEL_BLOCKS.put("dark_oak_log",    300);
        FUEL_BLOCKS.put("mangrove_log",    300);
        FUEL_BLOCKS.put("cherry_log",      300);
        FUEL_BLOCKS.put("bamboo_block",    300);
        FUEL_BLOCKS.put("poplar_log",      300);

        // Planks (vanilla 300)
        FUEL_BLOCKS.put("oak_planks",      300);
        FUEL_BLOCKS.put("spruce_planks",   300);
        FUEL_BLOCKS.put("birch_planks",    300);
        FUEL_BLOCKS.put("jungle_planks",   300);
        FUEL_BLOCKS.put("acacia_planks",   300);
        FUEL_BLOCKS.put("dark_oak_planks", 300);
        FUEL_BLOCKS.put("mangrove_planks", 300);
        FUEL_BLOCKS.put("cherry_planks",   300);
        FUEL_BLOCKS.put("poplar_planks",   300);

        // Coal block (vanilla 16000)
        FUEL_BLOCKS.put("coal_block",    16000);

        // Wool (vanilla 100)
        FUEL_BLOCKS.put("white_wool",      100);
        FUEL_BLOCKS.put("orange_wool",     100);
        FUEL_BLOCKS.put("magenta_wool",    100);
        FUEL_BLOCKS.put("light_blue_wool", 100);
        FUEL_BLOCKS.put("yellow_wool",     100);
        FUEL_BLOCKS.put("lime_wool",       100);
        FUEL_BLOCKS.put("pink_wool",       100);
        FUEL_BLOCKS.put("gray_wool",       100);
        FUEL_BLOCKS.put("light_gray_wool", 100);
        FUEL_BLOCKS.put("cyan_wool",       100);
        FUEL_BLOCKS.put("purple_wool",     100);
        FUEL_BLOCKS.put("blue_wool",       100);
        FUEL_BLOCKS.put("brown_wool",      100);
        FUEL_BLOCKS.put("green_wool",      100);
        FUEL_BLOCKS.put("red_wool",        100);
        FUEL_BLOCKS.put("black_wool",      100);
    }

    /**
     * Burn time for a compressed block item, or 0 if it isn't a fuel.
     * Reads the tier off the registry name so the map only lists materials.
     */
    private static int burnTimeFor(String registryName) {
        for (Map.Entry<String, Integer> e : FUEL_BLOCKS.entrySet()) {
            if (registryName.equals("compressed_" + e.getKey())) {
                return e.getValue() * 9;
            }
            if (registryName.equals("double_compressed_" + e.getKey())) {
                return e.getValue() * 81;
            }
        }
        return 0;
    }

    static {
        for (Map.Entry<String, DeferredHolder<Block, Block>> entry
                : UCBlocks.ALL_BLOCKS.entrySet()) {
            String name = entry.getKey();
            DeferredHolder<Block, Block> block = entry.getValue();

            // 26.3: fuel is the minecraft:cooking_fuel component, not a registry.
            // registerSimpleBlockItem takes no Properties, so burnable blocks
            // use the full registerItem form to attach the component.
            int burnTime = burnTimeFor(name);
            DeferredItem<BlockItem> item;

            if (burnTime > 0) {
                item = ITEMS.registerItem(name, props ->
                    new BlockItem(block.get(), props.component(
                        DataComponents.COOKING_FUEL, UCItemRegistry.fuel(burnTime))));
            } else {
                // registerSimpleBlockItem handles setId automatically
                item = ITEMS.registerSimpleBlockItem(name, block);
            }

            ALL_ITEMS.put(name, item);
        }
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModConstants.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UC_TAB =
        CREATIVE_TABS.register("uc_tab", () ->
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.uc"))
                .icon(() -> UCBlocks.ALL_BLOCKS.values().stream()
                    .findFirst()
                    .map(b -> new ItemStack(b.get()))
                    .orElse(ItemStack.EMPTY))
                .displayItems((params, output) -> {
                    ALL_ITEMS.values().forEach(output::accept);
                    UCItemRegistry.ALL_ITEMS.values().forEach(item -> output.accept(item.get()));
                    output.accept(UCItemRegistry.UC_COMPRESSOR.get());
                    UCOres.ALL_ORE_BLOCKS.values().forEach(b -> output.accept(b.get()));
                    UCEquipment.ALL_EQUIPMENT.values().forEach(item -> output.accept(item.get()));
                })
                .build()
        );
}