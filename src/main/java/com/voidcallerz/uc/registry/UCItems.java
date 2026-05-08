package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.network.chat.Component;
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

    static {
        for (Map.Entry<String, DeferredHolder<Block, Block>> entry
                : UCBlocks.ALL_BLOCKS.entrySet()) {
            String name = entry.getKey();
            DeferredHolder<Block, Block> block = entry.getValue();
            // registerSimpleBlockItem handles setId automatically
            DeferredItem<BlockItem> item = ITEMS.registerSimpleBlockItem(name, block);
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