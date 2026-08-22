package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;

public class UCItemRegistry {

    // 1.21.2+: must use DeferredRegister.Items. The specialized register is what
    // attaches the ResourceKey to Item.Properties; plain DeferredRegister.create
    // fails at registration with "Item not set".
    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(ModConstants.MOD_ID);

    public static final Map<String, DeferredHolder<Item, Item>> ALL_ITEMS = new LinkedHashMap<>();

    // -------------------------------------------------------------------------
    // The UC Compressor — center ingredient for recipes that would otherwise
    // conflict with a vanilla 9-in-3x3 recipe.
    //
    // 1.21.2+: the no-arg getCraftingRemainder() is final and reads the
    // craftRemainder property, which can't reference the item being built.
    // The ItemStack-sensitive overload is the overridable one, and returning a
    // non-empty stack is what keeps the catalyst in the grid — the old
    // hasCraftingRemainingItem companion no longer exists.
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, Item> UC_COMPRESSOR =
        ITEMS.registerItem("compression_catalyst", props -> new Item(props.stacksTo(1)) {
            @Override
            public ItemStack getCraftingRemainder(ItemStack stack) {
                return stack.copy();
            }
        });

    // -------------------------------------------------------------------------
    // Item definitions: registry_name, burnTime in ticks (0 = not a fuel)
    //
    // Burn times are 9x their vanilla counterpart (stick 100 -> 900,
    // coal 1600 -> 14400). They are applied in UCFuelHandler, which reads
    // fuelItems() below — Item#getBurnTime is gone in 1.21.2+.
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

        // --- Fuels ---
        { "compressed_coal",            14400 },
        { "compressed_blaze_rod",       11200 },
    };

    static {
        for (Object[] entry : ITEMS_LIST) {
            String name = (String) entry[0];
            ALL_ITEMS.put(name, ITEMS.registerItem(name, Item::new));
        }
    }

    /** Every item with a non-zero burn time, as registry_name -> ticks. */
    public static Map<String, Integer> fuelItems() {
        Map<String, Integer> fuels = new LinkedHashMap<>();
        for (Object[] entry : ITEMS_LIST) {
            int burn = (int) entry[1];
            if (burn > 0) fuels.put((String) entry[0], burn);
        }
        return fuels;
    }
}