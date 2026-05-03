package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;
import java.util.Map;

public class UCItems {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.ITEM, ModConstants.MOD_ID);

    public static final Map<String, DeferredHolder<Item, Item>> ALL_ITEMS = new LinkedHashMap<>();

    // Register a BlockItem for every compressed block
    static {
        for (Map.Entry<String, DeferredHolder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.Block>> entry
                : UCBlocks.ALL_BLOCKS.entrySet()) {
            String name = entry.getKey();
            DeferredHolder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.Block> block = entry.getValue();
            DeferredHolder<Item, Item> item = ITEMS.register(name,
                () -> new BlockItem(block.get(), new Item.Properties()));
            ALL_ITEMS.put(name, item);
        }
    }

    // -------------------------------------------------------------------------
    // Creative tab — blocks, items, ores, tools, armor
    // -------------------------------------------------------------------------
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
                    // Compressed blocks
                    ALL_ITEMS.values().forEach(item -> output.accept(item.get()));
                    // Compressed standalone items
                    UCItemRegistry.ALL_ITEMS.values().forEach(item -> output.accept(item.get()));
                    // Compression catalyst
                    output.accept(UCItemRegistry.UC_COMPRESSOR.get());
                    // Compressed ore blocks
                    UCOres.ALL_ORE_BLOCKS.values().forEach(b -> output.accept(b.get()));
                    // Tools and armor
                    UCEquipment.ALL_EQUIPMENT.values().forEach(item -> output.accept(item.get()));
                })
                .build()
        );
}