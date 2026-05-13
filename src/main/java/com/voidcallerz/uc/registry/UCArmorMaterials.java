package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;
import java.util.Map;

public class UCArmorMaterials {

    public static final int IRON_DUR      = 15 * 9;
    public static final int GOLD_DUR      = 7  * 9;
    public static final int DIAMOND_DUR   = 33 * 9;
    public static final int NETHERITE_DUR = 37 * 9;

    public static final ArmorMaterial COMPRESSED_IRON = new ArmorMaterial(
        IRON_DUR, defenseMap(3, 7, 6, 3), 11,
        SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 0.0f,
        ItemTags.REPAIRS_IRON_ARMOR,
        ResourceKey.create(EquipmentAssets.ROOT_ID,
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "compressed_iron"))
    );

    public static final ArmorMaterial COMPRESSED_GOLD = new ArmorMaterial(
        GOLD_DUR, defenseMap(3, 6, 4, 2), 27,
        SoundEvents.ARMOR_EQUIP_GOLD, 0.0f, 0.0f,
        ItemTags.REPAIRS_GOLD_ARMOR,
        ResourceKey.create(EquipmentAssets.ROOT_ID,
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "compressed_gold"))
    );

    public static final ArmorMaterial COMPRESSED_DIAMOND = new ArmorMaterial(
        DIAMOND_DUR, defenseMap(4, 8, 7, 4), 12,
        SoundEvents.ARMOR_EQUIP_DIAMOND, 3.0f, 0.0f,
        ItemTags.REPAIRS_DIAMOND_ARMOR,
        ResourceKey.create(EquipmentAssets.ROOT_ID,
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "compressed_diamond"))
    );

    public static final ArmorMaterial COMPRESSED_NETHERITE = new ArmorMaterial(
        NETHERITE_DUR, defenseMap(4, 9, 8, 4), 18,
        SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0f, 0.2f,
        ItemTags.REPAIRS_NETHERITE_ARMOR,
        ResourceKey.create(EquipmentAssets.ROOT_ID,
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "compressed_netherite"))
    );

    private static Map<ArmorType, Integer> defenseMap(
            int helmet, int chestplate, int leggings, int boots) {
        Map<ArmorType, Integer> map = new EnumMap<>(ArmorType.class);
        map.put(ArmorType.HELMET,     helmet);
        map.put(ArmorType.CHESTPLATE, chestplate);
        map.put(ArmorType.LEGGINGS,   leggings);
        map.put(ArmorType.BOOTS,      boots);
        return map;
    }
}