package com.voidcallerz.uc.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * Tool tiers for compressed tool sets.
 *
 * The incorrectBlocksForDrops tag defines which blocks this tool CANNOT mine.
 * This is the INVERSE of what you might expect:
 *
 *   NEEDS_STONE_TOOL   = blocks that need stone or better
 *   NEEDS_IRON_TOOL    = blocks that need iron or better
 *   NEEDS_DIAMOND_TOOL = blocks that need diamond or better
 *
 * So a tier that uses NEEDS_STONE_TOOL as incorrectBlocksForDrops means it
 * CANNOT mine stone-tier blocks — i.e. it is below stone level (wood/gold).
 *
 * Correct mapping:
 *   Wood tier    -> cannot mine stone-tier blocks -> NEEDS_STONE_TOOL
 *   Stone tier   -> cannot mine iron-tier blocks  -> NEEDS_IRON_TOOL
 *   Iron tier    -> cannot mine diamond-tier      -> NEEDS_DIAMOND_TOOL
 *   Gold tier    -> cannot mine stone-tier blocks -> NEEDS_STONE_TOOL (same as wood)
 *   Diamond tier -> cannot mine nothing above it  -> empty tag (mines everything)
 *   Netherite    -> cannot mine nothing above it  -> empty tag (mines everything)
 *
 * For diamond and netherite we use INCORRECT_FOR_NETHERITE_TOOL which is
 * an empty/non-existent tag so nothing is excluded — they can mine all blocks.
 *
 * Stats: durability = 9x vanilla, speed/attack = +0.5 above vanilla tier.
 */
public enum UCToolTiers implements Tier {

    COMPRESSED_WOOD(
        531,        // 9 × 59
        2.5f,
        0.5f,
        BlockTags.INCORRECT_FOR_WOODEN_TOOL,
        15,
        () -> Ingredient.of(UCBlocks.ALL_BLOCKS.get("compressed_oak_planks").get())
    ),

    COMPRESSED_STONE(
        1179,       // 9 × 131
        4.5f,
        1.5f,
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        5,
        () -> Ingredient.of(UCBlocks.ALL_BLOCKS.get("compressed_cobblestone").get())
    ),

    COMPRESSED_IRON(
        2250,       // 9 × 250
        6.5f,
        2.5f,
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        14,
        () -> Ingredient.of(UCItemRegistry.ALL_ITEMS.get("compressed_iron_ingot").get())
    ),

    COMPRESSED_GOLD(
        288,        // 9 × 32
        12.5f,
        0.5f,
        BlockTags.INCORRECT_FOR_GOLD_TOOL,
        22,
        () -> Ingredient.of(UCItemRegistry.ALL_ITEMS.get("compressed_gold_ingot").get())
    ),

    COMPRESSED_DIAMOND(
        14049,      // 9 × 1561
        8.5f,
        3.5f,
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
        10,
        () -> Ingredient.of(UCItemRegistry.ALL_ITEMS.get("compressed_diamond").get())
    ),

    COMPRESSED_NETHERITE(
        18279,      // 9 × 2031
        9.5f,
        4.5f,
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        15,
        () -> Ingredient.of(UCItemRegistry.ALL_ITEMS.get("compressed_netherite_ingot").get())
    );

    private final int durability;
    private final float speed;
    private final float attackDamageBonus;
    private final TagKey<Block> incorrectBlocksForDrops;
    private final int enchantability;
    private final java.util.function.Supplier<Ingredient> repairIngredient;

    UCToolTiers(int durability, float speed, float attackDamageBonus,
                TagKey<Block> incorrectBlocksForDrops, int enchantability,
                java.util.function.Supplier<Ingredient> repairIngredient) {
        this.durability              = durability;
        this.speed                   = speed;
        this.attackDamageBonus       = attackDamageBonus;
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.enchantability          = enchantability;
        this.repairIngredient        = repairIngredient;
    }

    @Override public int getUses()                { return durability; }
    @Override public float getSpeed()             { return speed; }
    @Override public float getAttackDamageBonus() { return attackDamageBonus; }
    @Override public TagKey<Block> getIncorrectBlocksForDrops() { return incorrectBlocksForDrops; }
    @Override public int getEnchantmentValue()    { return enchantability; }
    @Override public Ingredient getRepairIngredient() { return repairIngredient.get(); }
}