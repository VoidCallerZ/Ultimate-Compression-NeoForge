package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;
import java.util.Map;

public class UCItemRegistry {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.ITEM, ModConstants.MOD_ID);

    // All registered compressed items, keyed by registry name
    public static final Map<String, DeferredHolder<Item, Item>> ALL_ITEMS = new LinkedHashMap<>();

    // -------------------------------------------------------------------------
    // The UC Compressor — used as a center ingredient in crafting recipes
    // to avoid conflicts with vanilla 9-in-3x3 recipes (e.g. 9 iron ingots).
    // Registered separately so it can be referenced directly in code if needed.
    // stacksTo(1) so it behaves like a tool rather than a stackable resource.
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, Item> UC_COMPRESSOR =
        ITEMS.register("compression_catalyst", () -> new Item(new Item.Properties().stacksTo(1)) {
            @Override
            public net.minecraft.world.item.ItemStack getCraftingRemainingItem(
                    net.minecraft.world.item.ItemStack stack) {
                // Return a copy of itself so it stays in the crafting grid
                // after being used as the center ingredient
                return stack.copy();
            }

            @Override
            public boolean hasCraftingRemainingItem(net.minecraft.world.item.ItemStack stack) {
                return true;
            }
        });

    // -------------------------------------------------------------------------
    // Item definitions
    // Each entry is: registry_name, burnTime (0 = not a fuel)
    //
    // Only single compressed tier — items don't have a double compressed variant.
    // To add a new item in the future, just add one line here.
    // -------------------------------------------------------------------------
    private static final Object[][] ITEMS_LIST = {

        // --- Raw materials ---
        { "compressed_raw_iron",        0 },
        { "compressed_raw_gold",        0 },
        { "compressed_raw_copper",      0 },

        // --- Ingots ---
        { "compressed_iron_ingot",      0 },
        { "compressed_gold_ingot",      0 },
        { "compressed_copper_ingot",    0 },
        { "compressed_netherite_ingot", 0 },

        // --- Gems ---
        { "compressed_diamond",         0 },
        { "compressed_emerald",         0 },
        { "compressed_amethyst_shard",  0 },
        { "compressed_quartz",          0 },

        // --- Dusts & misc ---
        { "compressed_lapis",           0 },
        { "compressed_redstone",        0 },
        { "compressed_flint",           0 },
        { "compressed_stick",           900 },
        { "compressed_leather",         0 },
        { "compressed_bone",            0 },
        { "compressed_string",          0 },
        { "compressed_feather",         0 },

        // --- Nuggets ---
        { "compressed_iron_nugget",     0 },
        { "compressed_gold_nugget",     0 },

        // --- Fuels (burnTime in ticks — vanilla coal = 1600, compressed = 9x = 14400) ---
        { "compressed_coal",            14400 },
        { "compressed_blaze_rod",       11200 },
    };

    // -------------------------------------------------------------------------
    // Auto-registration
    // -------------------------------------------------------------------------
    static {
        for (Object[] entry : ITEMS_LIST) {
            String name     = (String) entry[0];
            int    burnTime = (int)    entry[1];

            DeferredHolder<Item, Item> item;

            if (burnTime > 0) {
                final int finalBurnTime = burnTime;
                item = ITEMS.register(name, () -> new Item(new Item.Properties()) {
                    @Override
                    public int getBurnTime(net.minecraft.world.item.ItemStack stack,
                                          @org.jetbrains.annotations.Nullable net.minecraft.world.item.crafting.RecipeType<?> recipeType) {
                        return finalBurnTime;
                    }
                });
            } else {
                item = ITEMS.register(name, () -> new Item(new Item.Properties()));
            }

            ALL_ITEMS.put(name, item);
        }
    }
}