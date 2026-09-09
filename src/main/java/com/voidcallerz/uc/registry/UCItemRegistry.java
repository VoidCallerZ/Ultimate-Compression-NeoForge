package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;
import java.util.Map;

public class UCItemRegistry {

    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(ModConstants.MOD_ID);

    public static final Map<String, DeferredHolder<Item, Item>> ALL_ITEMS = new LinkedHashMap<>();

    // -------------------------------------------------------------------------
    // Compression catalyst — stays in the grid after crafting.
    //
    // The no-arg getCraftingRemainder() is final and reads the craftRemainder
    // property, which can't reference the item being built. The ItemInstance
    // overload on IItemExtension is the overridable one, so the self-reference
    // works there.
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, Item> UC_COMPRESSOR =
        ITEMS.registerItem("compression_catalyst", props -> new Item(props.stacksTo(1)) {
            @Override
            public ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
                return new ItemStackTemplate(this);
            }
        });

    /** Neutral smelting speed — fuel burns at the normal rate. */
    public static final float DEFAULT_SPEED = 1.0f;

    /**
     * Builds a fixed-duration cooking fuel component.
     *
     * 26.3 removed the fuel registry entirely: burn times are now the
     * minecraft:cooking_fuel data component, applied per item at construction.
     * ResolvableInt/ResolvableFloat are sealed types — Constant is the fixed
     * variant, Reference points at a registered number provider.
     */
    public static CookingFuel fuel(int burnTicks) {
        return new CookingFuel(
            new ResolvableInt.Constant(burnTicks),
            new ResolvableFloat.Constant(DEFAULT_SPEED));
    }

    // -------------------------------------------------------------------------
    // Item definitions: registry_name, burnTime in ticks (0 = not a fuel)
    //
    // Burn times are 9x their vanilla counterpart (stick 100 -> 900,
    // coal 1600 -> 14400) and are attached as the cooking_fuel component below.
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
        { "compressed_resin_clump",     0 },

        // --- Nuggets ---
        { "compressed_iron_nugget",     0 },
        { "compressed_gold_nugget",     0 },

        // --- Fuels ---
        { "compressed_coal",            14400 },
        { "compressed_blaze_rod",       11200 },
    };

    static {
        for (Object[] entry : ITEMS_LIST) {
            String name     = (String) entry[0];
            int    burnTime = (int)    entry[1];

            ALL_ITEMS.put(name, ITEMS.registerItem(name, props ->
                new Item(burnTime > 0
                    ? props.component(DataComponents.COOKING_FUEL, fuel(burnTime))
                    : props)));
        }
    }
}