package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class UCArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
        DeferredRegister.create(Registries.ARMOR_MATERIAL, ModConstants.MOD_ID);

    public static final int IRON_DUR      = 15 * 9;
    public static final int GOLD_DUR      = 7  * 9;
    public static final int DIAMOND_DUR   = 33 * 9;
    public static final int NETHERITE_DUR = 37 * 9;

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COMPRESSED_IRON =
        ARMOR_MATERIALS.register("compressed_iron", () -> new ArmorMaterial(
            defenseMap(3, 7, 6, 3), 11,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(UCItemRegistry.ALL_ITEMS.get("compressed_iron_ingot").get()),
            List.of(new ArmorMaterial.Layer(
                ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "compressed_iron"))),
            1.0f, 0.0f
        ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COMPRESSED_GOLD =
        ARMOR_MATERIALS.register("compressed_gold", () -> new ArmorMaterial(
            defenseMap(3, 6, 4, 2), 27,
            SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(UCItemRegistry.ALL_ITEMS.get("compressed_gold_ingot").get()),
            List.of(new ArmorMaterial.Layer(
                ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "compressed_gold"))),
            0.0f, 0.0f
        ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COMPRESSED_DIAMOND =
        ARMOR_MATERIALS.register("compressed_diamond", () -> new ArmorMaterial(
            defenseMap(4, 8, 7, 4), 12,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            () -> Ingredient.of(UCItemRegistry.ALL_ITEMS.get("compressed_diamond").get()),
            List.of(new ArmorMaterial.Layer(
                ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "compressed_diamond"))),
            3.0f, 0.0f
        ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COMPRESSED_NETHERITE =
        ARMOR_MATERIALS.register("compressed_netherite", () -> new ArmorMaterial(
            defenseMap(4, 9, 8, 4), 18,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(UCItemRegistry.ALL_ITEMS.get("compressed_netherite_ingot").get()),
            List.of(new ArmorMaterial.Layer(
                ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "compressed_netherite"))),
            4.0f, 0.2f
        ));

    // -------------------------------------------------------------------------

    /**
     * In NeoForge 1.21.1, SoundEvents fields may be SoundEvent or Holder<SoundEvent>
     * depending on the build. We use BuiltInRegistries to get a guaranteed
     * Holder<SoundEvent> that works regardless.
     */
    private static Holder<SoundEvent> sound(SoundEvent event) {
        return BuiltInRegistries.SOUND_EVENT.wrapAsHolder(event);
    }

    public static int getDurability(DeferredHolder<ArmorMaterial, ArmorMaterial> material,
                                    ArmorItem.Type type) {
        if (material == COMPRESSED_IRON)      return type.getDurability(IRON_DUR);
        if (material == COMPRESSED_GOLD)      return type.getDurability(GOLD_DUR);
        if (material == COMPRESSED_DIAMOND)   return type.getDurability(DIAMOND_DUR);
        if (material == COMPRESSED_NETHERITE) return type.getDurability(NETHERITE_DUR);
        return 100;
    }

    private static Map<ArmorItem.Type, Integer> defenseMap(
            int helmet, int chestplate, int leggings, int boots) {
        Map<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.HELMET,     helmet);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.LEGGINGS,   leggings);
        map.put(ArmorItem.Type.BOOTS,      boots);
        return map;
    }
}