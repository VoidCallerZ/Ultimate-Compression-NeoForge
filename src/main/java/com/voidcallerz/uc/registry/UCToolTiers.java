package com.voidcallerz.uc.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;

/**
 * In 1.21.2+, Tier is replaced by ToolMaterial — a plain record.
 * ToolMaterial constructor:
 *   (TagKey<Block> incorrectBlocksForDrops,
 *    int durability,
 *    float speed,
 *    float attackDamageBonus,
 *    int enchantability,
 *    TagKey<Item> repairItems)
 */
public class UCToolTiers {

    public static final ToolMaterial COMPRESSED_WOOD = new ToolMaterial(
        BlockTags.INCORRECT_FOR_WOODEN_TOOL,
        531,    // 9 × 59
        2.5f,
        0.5f,
        15,
        ItemTags.WOODEN_TOOL_MATERIALS
    );

    public static final ToolMaterial COMPRESSED_STONE = new ToolMaterial(
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        1179,   // 9 × 131
        4.5f,
        1.5f,
        5,
        ItemTags.STONE_TOOL_MATERIALS
    );

    public static final ToolMaterial COMPRESSED_IRON = new ToolMaterial(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        2250,   // 9 × 250
        6.5f,
        2.5f,
        14,
        ItemTags.REPAIRS_IRON_ARMOR
    );

    public static final ToolMaterial COMPRESSED_GOLD = new ToolMaterial(
        BlockTags.INCORRECT_FOR_GOLD_TOOL,
        288,    // 9 × 32
        12.5f,
        0.5f,
        22,
        ItemTags.REPAIRS_GOLD_ARMOR
    );

    public static final ToolMaterial COMPRESSED_DIAMOND = new ToolMaterial(
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
        14049,  // 9 × 1561
        8.5f,
        3.5f,
        10,
        ItemTags.REPAIRS_DIAMOND_ARMOR
    );

    public static final ToolMaterial COMPRESSED_NETHERITE = new ToolMaterial(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        18279,  // 9 × 2031
        9.5f,
        4.5f,
        15,
        ItemTags.REPAIRS_NETHERITE_ARMOR
    );
}