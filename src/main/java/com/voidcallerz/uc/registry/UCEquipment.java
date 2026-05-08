package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class UCEquipment {

    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(ModConstants.MOD_ID);

    public static final Map<String, DeferredItem<? extends Item>> ALL_EQUIPMENT
        = new LinkedHashMap<>();

    private static final Object[][] TOOL_TIERS = {
        { "wood",      UCToolTiers.COMPRESSED_WOOD,      5.0f, -3.2f },
        { "stone",     UCToolTiers.COMPRESSED_STONE,     6.0f, -3.2f },
        { "iron",      UCToolTiers.COMPRESSED_IRON,      7.0f, -3.1f },
        { "gold",      UCToolTiers.COMPRESSED_GOLD,      5.0f, -3.2f },
        { "diamond",   UCToolTiers.COMPRESSED_DIAMOND,   8.0f, -3.0f },
        { "netherite", UCToolTiers.COMPRESSED_NETHERITE, 9.0f, -3.0f },
    };

    static {
        for (Object[] entry : TOOL_TIERS) {
            String       mat      = (String)       entry[0];
            ToolMaterial material = (ToolMaterial) entry[1];
            float        axeDmg   = (float)        entry[2];
            float        axeSpd   = (float)        entry[3];
            String       pre      = "compressed_" + mat;
            final float fd = axeDmg, fs = axeSpd;

            // registerItem passes setId'd props into the factory
            reg(pre + "_sword",   props -> new SwordItem(material, 3, -2.4f,   props));
            reg(pre + "_pickaxe", props -> new PickaxeItem(material, 1, -2.8f, props));
            reg(pre + "_axe",     props -> new AxeItem(material, fd, fs,       props));
            reg(pre + "_shovel",  props -> new ShovelItem(material, 1.5f, -3.0f, props));
            reg(pre + "_hoe",     props -> new HoeItem(material, 0, -3.0f,     props));
        }

        armorSet("iron",      UCArmorMaterials.COMPRESSED_IRON);
        armorSet("gold",      UCArmorMaterials.COMPRESSED_GOLD);
        armorSet("diamond",   UCArmorMaterials.COMPRESSED_DIAMOND);
        armorSet("netherite", UCArmorMaterials.COMPRESSED_NETHERITE);
    }

    private static void armorSet(String mat, ArmorMaterial material) {
        String pre = "compressed_" + mat;
        for (ArmorType type : new ArmorType[]{
                ArmorType.HELMET, ArmorType.CHESTPLATE,
                ArmorType.LEGGINGS, ArmorType.BOOTS}) {
            final ArmorType t = type;
            reg(pre + "_" + type.getName(),
                props -> new ArmorItem(material, t, props));
        }
    }

    private static void reg(String name, Function<Item.Properties, ? extends Item> factory) {
        ALL_EQUIPMENT.put(name, ITEMS.registerItem(name, factory));
    }
}