package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Registers compressed and double compressed block items as furnace fuel.
 * Compressed = 9x vanilla burn time, double compressed = 81x.
 *
 * Item fuels (compressed_coal, compressed_stick, etc.) are handled in
 * UCItemRegistry via getBurnTime() override — this class only handles blocks.
 */
@EventBusSubscriber(modid = ModConstants.MOD_ID)
public class UCFuelHandler {

    private static final Map<String, Integer> FUEL_MAP = new HashMap<>();

    static {
        // Add fuel blocks here: material name -> vanilla burn time
        // Compressed = 9x, double = 81x (calculated below)
        addFuel("oak_log",         300);
        addFuel("spruce_log",      300);
        addFuel("birch_log",       300);
        addFuel("jungle_log",      300);
        addFuel("acacia_log",      300);
        addFuel("dark_oak_log",    300);
        addFuel("mangrove_log",    300);
        addFuel("cherry_log",      300);
        addFuel("bamboo_block",    300);
        addFuel("oak_planks",      300);
        addFuel("spruce_planks",   300);
        addFuel("birch_planks",    300);
        addFuel("jungle_planks",   300);
        addFuel("acacia_planks",   300);
        addFuel("dark_oak_planks", 300);
        addFuel("mangrove_planks", 300);
        addFuel("cherry_planks",   300);
        addFuel("crimson_planks",  300);
        addFuel("warped_planks",   300);
        addFuel("coal_block",      16000);
        addFuel("white_wool",      100);
        addFuel("orange_wool",     100);
        addFuel("magenta_wool",    100);
        addFuel("light_blue_wool", 100);
        addFuel("yellow_wool",     100);
        addFuel("lime_wool",       100);
        addFuel("pink_wool",       100);
        addFuel("gray_wool",       100);
        addFuel("light_gray_wool", 100);
        addFuel("cyan_wool",       100);
        addFuel("purple_wool",     100);
        addFuel("blue_wool",       100);
        addFuel("brown_wool",      100);
        addFuel("green_wool",      100);
        addFuel("red_wool",        100);
        addFuel("black_wool",      100);
        addFuel("stick",           100);
        addFuel("coal",            1600);
        addFuel("blaze_rod",       2400);
    }

    private static void addFuel(String material, int vanillaBurnTime) {
        FUEL_MAP.put("compressed_" + material, vanillaBurnTime * 9);
        FUEL_MAP.put("double_compressed_" + material, vanillaBurnTime * 81);
    }

    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        String id = stack.getItem().builtInRegistryHolder().key().location().getPath();
        if (id.startsWith("compressed_") || id.startsWith("double_compressed_")) {
            Integer burnTime = FUEL_MAP.get(id);
            if (burnTime != null) {
                event.setBurnTime(burnTime);
            }
        }
    }
}