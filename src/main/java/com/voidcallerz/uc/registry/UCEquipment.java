package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * NeoForge 1.21.1:
 *   - DeferredHolder replaces RegistryObject
 *   - ArmorItem takes (Holder<ArmorMaterial>, ArmorItem.Type, Item.Properties)
 *     DeferredHolder implements Holder so it can be passed directly
 *   - Tool constructors take (Tier, Item.Properties) only
 *   - Durability set via Item.Properties.durability(type.getDurability(base))
 */
public class UCEquipment {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(BuiltInRegistries.ITEM, ModConstants.MOD_ID);

    public static final Map<String, DeferredHolder<Item, ? extends Item>> ALL_EQUIPMENT
        = new LinkedHashMap<>();

    // =========================================================================
    // TOOL SETS
    // =========================================================================

    private static final Object[][] TOOL_TIERS = {
        { "wood",      UCToolTiers.COMPRESSED_WOOD      },
        { "stone",     UCToolTiers.COMPRESSED_STONE     },
        { "iron",      UCToolTiers.COMPRESSED_IRON      },
        { "gold",      UCToolTiers.COMPRESSED_GOLD      },
        { "diamond",   UCToolTiers.COMPRESSED_DIAMOND   },
        { "netherite", UCToolTiers.COMPRESSED_NETHERITE },
    };

    static {
        for (Object[] entry : TOOL_TIERS) {
            String      mat  = (String)      entry[0];
            UCToolTiers tier = (UCToolTiers) entry[1];
            String      pre  = "compressed_" + mat;

            reg(pre + "_sword",   () -> new SwordItem(tier,   new Item.Properties()));
            reg(pre + "_pickaxe", () -> new PickaxeItem(tier, new Item.Properties()));
            reg(pre + "_axe",     () -> new AxeItem(tier,     new Item.Properties()));
            reg(pre + "_shovel",  () -> new ShovelItem(tier,  new Item.Properties()));
            reg(pre + "_hoe",     () -> new HoeItem(tier,     new Item.Properties()));
        }
    }

    // =========================================================================
    // ARMOR SETS
    // DeferredHolder<ArmorMaterial, ArmorMaterial> implements Holder<ArmorMaterial>
    // so it can be passed directly to ArmorItem constructor
    // =========================================================================

    static {
        armorSet("iron",      UCArmorMaterials.COMPRESSED_IRON);
        armorSet("gold",      UCArmorMaterials.COMPRESSED_GOLD);
        armorSet("diamond",   UCArmorMaterials.COMPRESSED_DIAMOND);
        armorSet("netherite", UCArmorMaterials.COMPRESSED_NETHERITE);
    }

    private static void armorSet(String mat,
            DeferredHolder<ArmorMaterial, ArmorMaterial> materialHolder) {
        String pre = "compressed_" + mat;
        for (ArmorItem.Type type : new ArmorItem.Type[]{
                ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE,
                ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS}) {
            final ArmorItem.Type t = type;
            reg(pre + "_" + type.getName(), () -> new ArmorItem(
                materialHolder,
                t,
                new Item.Properties().durability(
                    UCArmorMaterials.getDurability(materialHolder, t))
            ));
        }
    }

    // =========================================================================

    @SuppressWarnings("unchecked")
    private static void reg(String name, Supplier<? extends Item> supplier) {
        ALL_EQUIPMENT.put(name,
            (DeferredHolder<Item, ? extends Item>) ITEMS.register(name, supplier));
    }
}