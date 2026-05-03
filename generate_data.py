"""
Ultimate Compression — Data Generator
======================================
Generates everything the mod needs except textures.

  1. Blockstate JSONs
  2. Block model JSONs
  3. Block item model JSONs
  4. Standalone item model JSONs
  5. Recipe JSONs (blocks + items)
  6. Loot table JSONs
  7. Language file
  8. Block tags (additive — never removes manually added entries)

Recipe conflict note:
  Some items (iron ingot, diamond etc.) already have vanilla 9x3x3 recipes.
  These use an 8-around-1 pattern with a UC Compressor item in the center.
  Non-conflicting items use the standard 9x3x3 pattern.
  The compressor item itself is crafted from iron + redstone.

No external dependencies — uses only Python standard library.

Usage:
    python generate_data.py
"""

import json
from pathlib import Path

# =============================================================================
# CONFIGURATION
# =============================================================================

MOD_ID        = "uc"
TIER_PREFIXES = ["compressed", "double_compressed"]
TIER_LABELS   = ["Compressed", "Double Compressed"]

TAB_DISPLAY_NAME = "Ultimate Compression"

STANDARD_MATERIALS = [
    # Vanilla blocks
    "stone", "cobblestone", "dirt", "sand", "gravel",
    "netherrack", "soul_sand", "soul_soil", "blackstone", "deepslate",
    "calcite", "tuff", "obsidian",
    # Stone variants
    "andesite", "diorite", "granite", "cobbled_deepslate",
    # Ore blocks
    "iron_block", "gold_block", "diamond_block", "emerald_block",
    "lapis_block", "redstone_block", "coal_block", "copper_block",
    "netherite_block", "amethyst_block", "raw_iron_block", "raw_gold_block",
    "raw_copper_block",
    # Planks
    "oak_planks", "spruce_planks", "birch_planks", "jungle_planks",
    "acacia_planks", "dark_oak_planks", "mangrove_planks", "cherry_planks",
    "crimson_planks", "warped_planks",
    # Wool
    "black_wool", "white_wool", "red_wool", "green_wool", "blue_wool",
    "yellow_wool", "purple_wool", "orange_wool", "cyan_wool",
    "light_gray_wool", "gray_wool", "pink_wool", "magenta_wool",
    "brown_wool", "light_blue_wool", "lime_wool",
    # Concrete powder
    "black_concrete_powder", "white_concrete_powder", "red_concrete_powder",
    "green_concrete_powder", "blue_concrete_powder", "yellow_concrete_powder",
    "purple_concrete_powder", "orange_concrete_powder", "cyan_concrete_powder",
    "light_gray_concrete_powder", "gray_concrete_powder", "pink_concrete_powder",
    "magenta_concrete_powder", "brown_concrete_powder", "light_blue_concrete_powder",
    "lime_concrete_powder",
    # Concrete
    "black_concrete", "white_concrete", "red_concrete", "green_concrete",
    "blue_concrete", "yellow_concrete", "purple_concrete", "orange_concrete",
    "cyan_concrete", "light_gray_concrete", "gray_concrete", "pink_concrete",
    "magenta_concrete", "brown_concrete", "light_blue_concrete", "lime_concrete",
    # Misc
    "red_sand",
]

TOP_BOTTOM_MATERIALS = [
    
]

LOG_MATERIALS = [
    "oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log",
    "dark_oak_log", "mangrove_log", "cherry_log", "bamboo_block", 
    "crimson_stem", "warped_stem",
]

ALL_MATERIALS = STANDARD_MATERIALS + TOP_BOTTOM_MATERIALS + LOG_MATERIALS

# -------------------------------------------------------------------------
# Items that already have a vanilla 9-in-3x3 recipe.
# -------------------------------------------------------------------------
VANILLA_CONFLICTS = {
    "iron_ingot", "gold_ingot", "copper_ingot", "diamond", "emerald",
    "lapis_lazuli", "redstone", "coal", "netherite_ingot",
    "iron_nugget", "gold_nugget", "quartz", "bone", "string",
    "raw_iron", "raw_gold", "raw_copper",
}

COMPRESSED_ITEMS = [
    { "name": "compressed_raw_iron",        "base": "raw_iron",        "burn": 0 },
    { "name": "compressed_raw_gold",        "base": "raw_gold",        "burn": 0 },
    { "name": "compressed_raw_copper",      "base": "raw_copper",      "burn": 0 },
    { "name": "compressed_iron_ingot",      "base": "iron_ingot",      "burn": 0 },
    { "name": "compressed_gold_ingot",      "base": "gold_ingot",      "burn": 0 },
    { "name": "compressed_copper_ingot",    "base": "copper_ingot",    "burn": 0 },
    { "name": "compressed_netherite_ingot", "base": "netherite_ingot", "burn": 0 },
    { "name": "compressed_diamond",         "base": "diamond",         "burn": 0 },
    { "name": "compressed_emerald",         "base": "emerald",         "burn": 0 },
    { "name": "compressed_amethyst_shard",  "base": "amethyst_shard",  "burn": 0 },
    { "name": "compressed_quartz",          "base": "quartz",          "burn": 0 },
    { "name": "compressed_lapis",           "base": "lapis_lazuli",    "burn": 0 },
    { "name": "compressed_redstone",        "base": "redstone",        "burn": 0 },
    { "name": "compressed_flint",           "base": "flint",           "burn": 0 },
    { "name": "compressed_stick",           "base": "stick",           "burn": 0 },
    { "name": "compressed_leather",         "base": "leather",         "burn": 0 },
    { "name": "compressed_bone",            "base": "bone",            "burn": 0 },
    { "name": "compressed_string",          "base": "string",          "burn": 0 },
    { "name": "compressed_feather",         "base": "feather",         "burn": 0 },
    { "name": "compressed_iron_nugget",     "base": "iron_nugget",     "burn": 0 },
    { "name": "compressed_gold_nugget",     "base": "gold_nugget",     "burn": 0 },
    { "name": "compressed_coal",            "base": "coal",            "burn": 14400 },
    { "name": "compressed_blaze_rod",       "base": "blaze_rod",       "burn": 11200 },
]

LOWERCASE_WORDS = {"a", "an", "the", "of", "in", "on", "at", "and", "or"}

COMPRESSOR_ID = f"{MOD_ID}:compression_catalyst"

# =============================================================================
# TOOL TIER SETS
# A block must be in EXACTLY ONE tier tag — being in multiple causes the
# highest tier to win, making lower tier tools unable to mine it.
# =============================================================================

# Diamond pickaxe minimum
NEEDS_DIAMOND = {
    "obsidian", "netherite_block", "diamond_block", "emerald_block",
}

# Iron pickaxe minimum
NEEDS_IRON = {
    "iron_block", "gold_block", "copper_block", "lapis_block",
    "redstone_block", "coal_block", "amethyst_block",
    "raw_iron_block", "raw_gold_block", "raw_copper_block",
}

# Materials that drop without any tool — no requiresCorrectToolForDrops
# Must match NO_TOOL_REQUIRED in UCBlocks.java
NO_TOOL_MATS = {
    "dirt", "sand", "gravel", "soul_sand", "soul_soil", "red_sand",
    "oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log",
    "dark_oak_log", "mangrove_log", "cherry_log", "bamboo_block",
    "crimson_stem", "warped_stem",
    "oak_planks", "spruce_planks", "birch_planks", "jungle_planks",
    "acacia_planks", "dark_oak_planks", "mangrove_planks", "cherry_planks",
    "crimson_planks", "warped_planks",
    "black_wool", "white_wool", "red_wool", "green_wool", "blue_wool",
    "yellow_wool", "purple_wool", "orange_wool", "cyan_wool",
    "light_gray_wool", "gray_wool", "pink_wool", "magenta_wool",
    "brown_wool", "light_blue_wool", "lime_wool",
    "black_concrete_powder", "white_concrete_powder", "red_concrete_powder",
    "green_concrete_powder", "blue_concrete_powder", "yellow_concrete_powder",
    "purple_concrete_powder", "orange_concrete_powder", "cyan_concrete_powder",
    "light_gray_concrete_powder", "gray_concrete_powder", "pink_concrete_powder",
    "magenta_concrete_powder", "brown_concrete_powder", "light_blue_concrete_powder",
    "lime_concrete_powder",
}

# Everything else that needs a tool defaults to needs_stone_tool:
# stone, cobblestone, deepslate, blackstone, calcite, tuff,
# andesite, diorite, granite, cobbled_deepslate, grass_block etc.

# =============================================================================
# PATH DETECTION
# =============================================================================

def find_resource_path() -> Path:
    script_dir = Path(__file__).parent
    for folder in [script_dir, script_dir.parent, script_dir.parent.parent]:
        candidate = folder / "src" / "main" / "resources"
        if candidate.exists():
            return candidate
    raise FileNotFoundError(
        "Could not find src/main/resources. "
        "Run this script from your mod project root."
    )

# =============================================================================
# SHARED HELPERS
# =============================================================================

def write_json(path: Path, data: dict):
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)


def read_existing_tag(path: Path) -> list:
    """Read existing tag file values, or empty list if missing."""
    if path.exists():
        with open(path, "r", encoding="utf-8") as f:
            return json.load(f).get("values", [])
    return []


def write_tag_merged(path: Path, new_ids: list):
    """
    Merge new_ids into existing tag file without removing anything.
    Preserves manually added entries. Deduplicates and sorts for readability.
    """
    path.parent.mkdir(parents=True, exist_ok=True)
    existing = read_existing_tag(path)
    merged   = sorted(set(existing) | set(new_ids))
    write_json(path, {"replace": False, "values": merged})


def registry_name(material: str, tier_index: int) -> str:
    return f"{TIER_PREFIXES[tier_index]}_{material}"


def item_id(material: str, tier_index: int | None) -> str:
    if tier_index is None:
        return f"minecraft:{material}"
    return f"{MOD_ID}:{registry_name(material, tier_index)}"


def to_title_case(snake_str: str) -> str:
    words = snake_str.split("_")
    return " ".join(
        w.capitalize() if i == 0 or w not in LOWERCASE_WORDS else w
        for i, w in enumerate(words)
    )

# =============================================================================
# 1. BLOCKSTATES
# =============================================================================

def generate_blockstates(resource_path: Path) -> int:
    out_base = resource_path / "assets" / MOD_ID / "blockstates"
    count = 0
    for material in STANDARD_MATERIALS + TOP_BOTTOM_MATERIALS:
        for tier_index in range(len(TIER_PREFIXES)):
            name = registry_name(material, tier_index)
            write_json(out_base / f"{name}.json", {
                "variants": { "": { "model": f"{MOD_ID}:block/{name}" } }
            })
            count += 1
    for material in LOG_MATERIALS:
        for tier_index in range(len(TIER_PREFIXES)):
            name  = registry_name(material, tier_index)
            model = f"{MOD_ID}:block/{name}"
            write_json(out_base / f"{name}.json", {
                "variants": {
                    "axis=x": { "model": model, "x": 90, "y": 90 },
                    "axis=y": { "model": model },
                    "axis=z": { "model": model, "x": 90 }
                }
            })
            count += 1
    return count

# =============================================================================
# 2. BLOCK MODELS
# =============================================================================

def generate_block_models(resource_path: Path) -> int:
    out_base = resource_path / "assets" / MOD_ID / "models" / "block"
    count = 0
    for material in STANDARD_MATERIALS:
        for tier_index in range(len(TIER_PREFIXES)):
            name = registry_name(material, tier_index)
            write_json(out_base / f"{name}.json", {
                "parent": "minecraft:block/cube_all",
                "textures": { "all": f"{MOD_ID}:block/{name}" }
            })
            count += 1
    for material in TOP_BOTTOM_MATERIALS:
        for tier_index in range(len(TIER_PREFIXES)):
            name = registry_name(material, tier_index)
            side = f"{MOD_ID}:block/{name}"
            top  = f"{MOD_ID}:block/{name}_top"
            write_json(out_base / f"{name}.json", {
                "parent": "minecraft:block/cube",
                "textures": {
                    "particle": side,
                    "down": top, "up": top,
                    "north": side, "south": side,
                    "east": side, "west": side,
                }
            })
            count += 1
    for material in LOG_MATERIALS:
        for tier_index in range(len(TIER_PREFIXES)):
            name = registry_name(material, tier_index)
            side = f"{MOD_ID}:block/{name}"
            end  = f"{MOD_ID}:block/{name}_top"
            write_json(out_base / f"{name}.json", {
                "parent": "minecraft:block/cube",
                "textures": {
                    "particle": side,
                    "down": end, "up": end,
                    "north": side, "south": side,
                    "east": side, "west": side,
                }
            })
            count += 1
    return count

# =============================================================================
# 3. ITEM MODELS
# =============================================================================

def generate_item_models_blocks(resource_path: Path) -> int:
    out_base = resource_path / "assets" / MOD_ID / "models" / "item"
    count = 0
    for material in ALL_MATERIALS:
        for tier_index in range(len(TIER_PREFIXES)):
            name = registry_name(material, tier_index)
            write_json(out_base / f"{name}.json", {
                "parent": f"{MOD_ID}:block/{name}"
            })
            count += 1
    return count


def generate_item_models_standalone(resource_path: Path) -> int:
    out_base = resource_path / "assets" / MOD_ID / "models" / "item"
    count = 0
    for entry in COMPRESSED_ITEMS:
        name = entry["name"]
        write_json(out_base / f"{name}.json", {
            "parent": "minecraft:item/generated",
            "textures": { "layer0": f"{MOD_ID}:item/{name}" }
        })
        count += 1
    write_json(out_base / "compression_catalyst.json", {
        "parent": "minecraft:item/generated",
        "textures": { "layer0": f"{MOD_ID}:item/compression_catalyst" }
    })
    count += 1
    return count

# =============================================================================
# 4. RECIPES
# =============================================================================

def make_standard_compress(input_id: str, output_id: str) -> dict:
    return {
        "type": "minecraft:crafting_shaped",
        "pattern": ["###", "###", "###"],
        "key": { "#": { "item": input_id } },
        "result": { "id": output_id, "count": 1 }
    }


def make_compressor_compress(input_id: str, output_id: str) -> dict:
    return {
        "type": "minecraft:crafting_shaped",
        "pattern": ["###", "#C#", "###"],
        "key": {
            "#": { "item": input_id },
            "C": { "item": COMPRESSOR_ID }
        },
        "result": { "id": output_id, "count": 1 }
    }


def make_decompress(input_id: str, output_id: str, count: int = 9) -> dict:
    return {
        "type": "minecraft:crafting_shapeless",
        "ingredients": [{ "item": input_id }],
        "result": { "id": output_id, "count": count }
    }


def make_compressor_recipe() -> dict:
    return {
        "type": "minecraft:crafting_shaped",
        "pattern": ["RRR", "RIR", "RRR"],
        "key": {
            "R": { "item": "minecraft:redstone" },
            "I": { "item": "minecraft:iron_ingot" }
        },
        "result": { "id": COMPRESSOR_ID, "count": 1 }
    }


def generate_block_recipes(resource_path: Path) -> int:
    out_base = resource_path / "data" / MOD_ID / "recipe"
    count = 0
    for material in ALL_MATERIALS:
        write_json(out_base / f"compress_{material}.json",
                   make_standard_compress(item_id(material, None), item_id(material, 0)))
        write_json(out_base / f"decompress_{material}.json",
                   make_decompress(item_id(material, 0), item_id(material, None)))
        write_json(out_base / f"compress_double_{material}.json",
                   make_standard_compress(item_id(material, 0), item_id(material, 1)))
        write_json(out_base / f"decompress_double_{material}.json",
                   make_decompress(item_id(material, 1), item_id(material, 0)))
        count += 4
    return count


def generate_item_recipes(resource_path: Path) -> int:
    out_base = resource_path / "data" / MOD_ID / "recipe"
    count = 0
    write_json(out_base / "compression_catalyst.json", make_compressor_recipe())
    count += 1
    for entry in COMPRESSED_ITEMS:
        name    = entry["name"]
        base    = entry["base"]
        base_id = f"minecraft:{base}"
        comp_id = f"{MOD_ID}:{name}"
        if base in VANILLA_CONFLICTS:
            write_json(out_base / f"{name}.json",
                       make_compressor_compress(base_id, comp_id))
        else:
            write_json(out_base / f"{name}.json",
                       make_standard_compress(base_id, comp_id))
        decompress_count = 8 if base in VANILLA_CONFLICTS else 9
        write_json(out_base / f"decompress_{name}.json",
                   make_decompress(comp_id, base_id, decompress_count))
        count += 2
    return count

# =============================================================================
# 5. LOOT TABLES
# =============================================================================

def generate_loot_tables(resource_path: Path) -> int:
    out_base = resource_path / "data" / MOD_ID / "loot_table" / "blocks"
    count = 0
    for material in ALL_MATERIALS:
        for tier_index in range(len(TIER_PREFIXES)):
            name = registry_name(material, tier_index)
            write_json(out_base / f"{name}.json", {
                "type": "minecraft:block",
                "pools": [{
                    "rolls": 1,
                    "bonus_rolls": 0,
                    "entries": [{ "type": "minecraft:item", "name": f"{MOD_ID}:{name}" }],
                    "conditions": [{ "condition": "minecraft:survives_explosion" }]
                }]
            })
            count += 1
    return count

# =============================================================================
# 6. LANGUAGE FILE
# =============================================================================

def generate_lang(resource_path: Path) -> int:
    out_path = resource_path / "assets" / MOD_ID / "lang" / "en_us.json"
    if out_path.exists():
        with open(out_path, "r", encoding="utf-8") as f:
            entries = json.load(f)
    else:
        entries = {}
    entries[f"itemGroup.{MOD_ID}"] = TAB_DISPLAY_NAME
    for material in ALL_MATERIALS:
        for tier_index in range(len(TIER_PREFIXES)):
            key   = f"block.{MOD_ID}.{registry_name(material, tier_index)}"
            value = f"{TIER_LABELS[tier_index]} {to_title_case(material)}"
            entries[key] = value
    for entry in COMPRESSED_ITEMS:
        key   = f"item.{MOD_ID}.{entry['name']}"
        value = to_title_case(entry["name"])
        entries[key] = value
    entries[f"item.{MOD_ID}.compression_catalyst"] = "Compression Catalyst"
    # Add equipment lang entries
    for mat, label in EQUIPMENT_MATERIALS_TOOLS:
        for tool, tool_label in TOOL_TYPES:
            entries[f"item.{MOD_ID}.compressed_{mat}_{tool}"] = f"Compressed {label} {tool_label}"
    for mat, label in EQUIPMENT_MATERIALS_ARMOR:
        for piece, piece_label in ARMOR_TYPES:
            entries[f"item.{MOD_ID}.compressed_{mat}_{piece}"] = f"Compressed {label} {piece_label}"
    write_json(out_path, entries)
    return len(entries)

# =============================================================================
# 7. BLOCK TAGS — additive, never removes manually added entries
# =============================================================================

def generate_block_tags(resource_path: Path) -> int:
    out_base  = resource_path / "data" / "minecraft" / "tags" / "block"
    tool_mats = [m for m in ALL_MATERIALS if m not in NO_TOOL_MATS]

    # mineable/pickaxe — all tool-required blocks
    pickaxe_ids = [
        f"{MOD_ID}:{registry_name(mat, t)}"
        for mat in tool_mats
        for t in range(len(TIER_PREFIXES))
    ]
    write_tag_merged(out_base / "mineable" / "pickaxe.json", pickaxe_ids)

    # needs_diamond_tool
    diamond_ids = [
        f"{MOD_ID}:{registry_name(mat, t)}"
        for mat in tool_mats if mat in NEEDS_DIAMOND
        for t in range(len(TIER_PREFIXES))
    ]
    write_tag_merged(out_base / "needs_diamond_tool.json", diamond_ids)

    # needs_iron_tool
    iron_ids = [
        f"{MOD_ID}:{registry_name(mat, t)}"
        for mat in tool_mats if mat in NEEDS_IRON
        for t in range(len(TIER_PREFIXES))
    ]
    write_tag_merged(out_base / "needs_iron_tool.json", iron_ids)

    # needs_stone_tool — everything else that needs a tool
    stone_ids = [
        f"{MOD_ID}:{registry_name(mat, t)}"
        for mat in tool_mats
        if mat not in NEEDS_DIAMOND and mat not in NEEDS_IRON
        for t in range(len(TIER_PREFIXES))
    ]
    write_tag_merged(out_base / "needs_stone_tool.json", stone_ids)

    return 4


# =============================================================================
# EQUIPMENT CONFIG
# =============================================================================

EQUIPMENT_MATERIALS_TOOLS = [
    ("wood",      "Wood"),
    ("stone",     "Stone"),
    ("iron",      "Iron"),
    ("gold",      "Gold"),
    ("diamond",   "Diamond"),
    ("netherite", "Netherite"),
]

EQUIPMENT_MATERIALS_ARMOR = [
    ("iron",      "Iron"),
    ("gold",      "Gold"),
    ("diamond",   "Diamond"),
    ("netherite", "Netherite"),
]

TOOL_TYPES = [
    ("sword",   "Sword"),
    ("pickaxe", "Pickaxe"),
    ("axe",     "Axe"),
    ("shovel",  "Shovel"),
    ("hoe",     "Hoe"),
]

ARMOR_TYPES = [
    ("helmet",     "Helmet"),
    ("chestplate", "Chestplate"),
    ("leggings",   "Leggings"),
    ("boots",      "Boots"),
]

# Crafting ingredient for each tool material
TOOL_INGREDIENTS = {
    "wood":      "uc:compressed_oak_planks",
    "stone":     "uc:compressed_cobblestone",
    "iron":      "uc:compressed_iron_ingot",
    "gold":      "uc:compressed_gold_ingot",
    "diamond":   "uc:compressed_diamond",
    "netherite": "uc:compressed_netherite_ingot",
}

ARMOR_INGREDIENTS = {
    "iron":      "uc:compressed_iron_ingot",
    "gold":      "uc:compressed_gold_ingot",
    "diamond":   "uc:compressed_diamond",
    "netherite": "uc:compressed_netherite_ingot",
}

# Vanilla shaped patterns for each tool type
TOOL_PATTERNS = {
    "sword":   ["#", "#", "S"],   # S = stick
    "pickaxe": ["###", " S ", " S "],
    "axe":     ["## ", "#S ", " S "],
    "shovel":  ["#", "S", "S"],
    "hoe":     ["##", " S", " S"],
}

ARMOR_PATTERNS = {
    "helmet":     ["###", "# #", "   "],
    "chestplate": ["# #", "###", "###"],
    "leggings":   ["###", "# #", "# #"],
    "boots":      ["   ", "# #", "# #"],
}


def generate_equipment_recipes(resource_path: Path) -> int:
    out_base = resource_path / "data" / MOD_ID / "recipe"
    count = 0
    stick_id = f"{MOD_ID}:compressed_stick"

    for mat, _ in EQUIPMENT_MATERIALS_TOOLS:
        mat_id = TOOL_INGREDIENTS[mat]
        for tool, _ in TOOL_TYPES:
            name    = f"compressed_{mat}_{tool}"
            pattern = TOOL_PATTERNS[tool]
            # Build key — tools use # for material and S for stick
            key = {"#": {"item": mat_id}}
            if any("S" in row for row in pattern):
                key["S"] = {"item": stick_id}
            write_json(out_base / f"{name}.json", {
                "type": "minecraft:crafting_shaped",
                "pattern": pattern,
                "key": key,
                "result": {"id": f"{MOD_ID}:{name}", "count": 1}
            })
            count += 1

    for mat, _ in EQUIPMENT_MATERIALS_ARMOR:
        mat_id = ARMOR_INGREDIENTS[mat]
        for piece, _ in ARMOR_TYPES:
            name    = f"compressed_{mat}_{piece}"
            pattern = ARMOR_PATTERNS[piece]
            write_json(out_base / f"{name}.json", {
                "type": "minecraft:crafting_shaped",
                "pattern": pattern,
                "key": {"#": {"item": mat_id}},
                "result": {"id": f"{MOD_ID}:{name}", "count": 1}
            })
            count += 1

    return count


def generate_equipment_item_models(resource_path: Path) -> int:
    out_base = resource_path / "assets" / MOD_ID / "models" / "item"
    count = 0
    for mat, _ in EQUIPMENT_MATERIALS_TOOLS:
        for tool, _ in TOOL_TYPES:
            name = f"compressed_{mat}_{tool}"
            write_json(out_base / f"{name}.json", {
                "parent": "minecraft:item/handheld",
                "textures": {"layer0": f"{MOD_ID}:item/{name}"}
            })
            count += 1
    for mat, _ in EQUIPMENT_MATERIALS_ARMOR:
        for piece, _ in ARMOR_TYPES:
            name = f"compressed_{mat}_{piece}"
            write_json(out_base / f"{name}.json", {
                "parent": "minecraft:item/generated",
                "textures": {"layer0": f"{MOD_ID}:item/{name}"}
            })
            count += 1
    return count

# =============================================================================
# SUMMARY & MAIN
# =============================================================================

def print_summary(resource_path: Path):
    total_blocks   = len(ALL_MATERIALS) * len(TIER_PREFIXES)
    total_items    = len(COMPRESSED_ITEMS)
    conflict_count = sum(1 for e in COMPRESSED_ITEMS if e["base"] in VANILLA_CONFLICTS)
    print(f"""
  What will be generated
  ─────────────────────────────────────────
  Blockstate JSONs         : {total_blocks}
  Block model JSONs        : {total_blocks}
  Block item model JSONs   : {total_blocks}
  Standalone item models   : {total_items + 1}  (+1 catalyst)
  Block recipe JSONs       : {total_blocks * 2}
  Item recipe JSONs        : {total_items * 2 + 1}  (+1 catalyst craft)
    of which use catalyst  : {conflict_count}
  Loot table JSONs         : {total_blocks}
  Language entries         : {total_blocks + total_items + 2}
  Block tag files          : 4 (additive merge)
  ─────────────────────────────────────────
  Output root: {resource_path}
    """)


def main():
    print("=" * 60)
    print("  Ultimate Compression -- Data Generator")
    print("=" * 60)

    print("\n[1/3] Locating mod resource path...")
    resource_path = find_resource_path()
    print(f"  Found: {resource_path}")

    print("\n[2/3] Generation summary:")
    print_summary(resource_path)

    answer = input("  Write all files now? [y/N]: ").strip().lower()
    if answer != "y":
        print("\n  Aborted — no files written.")
        return

    print("\n[3/3] Generating...")
    equip_rec = generate_equipment_recipes(resource_path)
    equip_im  = generate_equipment_item_models(resource_path)
    tags = generate_block_tags(resource_path)
    bs   = generate_blockstates(resource_path)
    bm   = generate_block_models(resource_path)
    im   = generate_item_models_blocks(resource_path)
    im2  = generate_item_models_standalone(resource_path)
    rec  = generate_block_recipes(resource_path)
    irec = generate_item_recipes(resource_path)
    loot = generate_loot_tables(resource_path)
    lang = generate_lang(resource_path)

    print(f"""
  Done!
  ─────────────────────────────────────────
  Equipment recipes        : {equip_rec}
  Equipment item models    : {equip_im}
  Block tag files          : {tags} (additive)
  Blockstate JSONs         : {bs}
  Block model JSONs        : {bm}
  Block item model JSONs   : {im}
  Standalone item models   : {im2}
  Block recipe JSONs       : {rec}
  Item recipe JSONs        : {irec}
  Loot table JSONs         : {loot}
  Language entries         : {lang}
    """)
    print("=" * 60)


if __name__ == "__main__":
    main()