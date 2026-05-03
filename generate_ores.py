"""
Ultimate Compression — Ore Generator
======================================
Generates all JSON files needed for compressed ores to spawn underground:

  1. Configured feature JSONs   — defines vein size and target block
  2. Placed feature JSONs       — defines depth, frequency and biome filter
  3. Biome modifier JSONs       — injects features into overworld biomes
  4. Loot table JSONs           — silk touch drops ore block, otherwise
                                  drops raw ore/gem with fortune support
  5. Blockstate JSONs           — simple single-variant
  6. Block model JSONs          — cube_all using compressed ore texture
  7. Item model JSONs           — inherits from block model
  8. Lang entries               — appended to existing en_us.json

All compressed ores spawn only at deepslate depth (negative Y levels),
in smaller veins than their vanilla counterparts.

No external dependencies — uses only Python standard library.

Usage:
    python generate_ores.py

Run after:
    python generate_textures.py  (to generate the ore textures first)
"""

import json
from pathlib import Path

# =============================================================================
# CONFIGURATION — must match UCOres.java
# =============================================================================

MOD_ID = "uc"

# Ore definitions
# name             : registry name
# vanilla_drop     : what drops without silk touch (minecraft: item)
# drop_count       : base drop count without fortune
# fortune_bonus    : uses fortune enchantment on drops
# vein_size        : max blocks per vein (1 = single block)
# veins_per_chunk  : average veins per chunk
# min_y / max_y    : absolute Y level range (negative = underground)

ORES = [
    {
        "name":             "compressed_coal_ore",
        "display":          "Compressed Coal Ore",
        "vanilla_drop":     "coal",
        "compressed_drop":  "compressed_coal",
        "fortune_bonus":    True,
        "vein_size":        3,
        "veins_per_chunk":  2,
        "min_y":            -64,
        "max_y":            -32,
    },
    {
        "name":             "compressed_iron_ore",
        "display":          "Compressed Iron Ore",
        "vanilla_drop":     "raw_iron",
        "compressed_drop":  "compressed_raw_iron",
        "fortune_bonus":    True,
        "vein_size":        3,
        "veins_per_chunk":  2,
        "min_y":            -64,
        "max_y":            -32,
    },
    {
        "name":             "compressed_gold_ore",
        "display":          "Compressed Gold Ore",
        "vanilla_drop":     "raw_gold",
        "compressed_drop":  "compressed_raw_gold",
        "fortune_bonus":    True,
        "vein_size":        2,
        "veins_per_chunk":  1,
        "min_y":            -64,
        "max_y":            -32,
    },
    {
        "name":             "compressed_copper_ore",
        "display":          "Compressed Copper Ore",
        "vanilla_drop":     "raw_copper",
        "compressed_drop":  "compressed_raw_copper",
        "fortune_bonus":    True,
        "vein_size":        3,
        "veins_per_chunk":  2,
        "min_y":            -64,
        "max_y":            -16,
    },
    {
        "name":             "compressed_diamond_ore",
        "display":          "Compressed Diamond Ore",
        "vanilla_drop":     "diamond",
        "compressed_drop":  "compressed_diamond",
        "fortune_bonus":    True,
        "vein_size":        1,
        "veins_per_chunk":  1,
        "min_y":            -64,
        "max_y":            -48,
    },
    {
        "name":             "compressed_emerald_ore",
        "display":          "Compressed Emerald Ore",
        "vanilla_drop":     "emerald",
        "compressed_drop":  "compressed_emerald",
        "fortune_bonus":    True,
        "vein_size":        1,
        "veins_per_chunk":  1,
        "min_y":            -64,
        "max_y":            -32,
    },
    {
        "name":             "compressed_lapis_ore",
        "display":          "Compressed Lapis Ore",
        "vanilla_drop":     "lapis_lazuli",
        "compressed_drop":  "compressed_lapis",
        "fortune_bonus":    True,
        "vein_size":        2,
        "veins_per_chunk":  1,
        "min_y":            -64,
        "max_y":            -32,
    },
    {
        "name":             "compressed_redstone_ore",
        "display":          "Compressed Redstone Ore",
        "vanilla_drop":     "redstone",
        "compressed_drop":  "compressed_redstone",
        "fortune_bonus":    True,
        "vein_size":        2,
        "veins_per_chunk":  2,
        "min_y":            -64,
        "max_y":            -32,
    },
]

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
# HELPERS
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
    Preserves manually added entries. Deduplicates and sorts.
    """
    path.parent.mkdir(parents=True, exist_ok=True)
    existing = read_existing_tag(path)
    merged   = sorted(set(existing) | set(new_ids))
    write_json(path, {"replace": False, "values": merged})


# =============================================================================
# 1. CONFIGURED FEATURES
# Defines what the ore vein looks like — which block, how big
# =============================================================================

def generate_configured_features(resource_path: Path) -> int:
    out_base = resource_path / "data" / MOD_ID / "worldgen" / "configured_feature"
    count = 0
    for ore in ORES:
        name = ore["name"]
        data = {
            "type": "minecraft:ore",
            "config": {
                "size": ore["vein_size"],
                "discard_chance_on_air_exposure": 0.0,
                "targets": [
                    {
                        # Only replaces deepslate — not stone, not air
                        "target": {
                            "predicate_type": "minecraft:tag_match",
                            "tag": "minecraft:deepslate_ore_replaceables"
                        },
                        "state": {
                            "Name": f"{MOD_ID}:{name}"
                        }
                    }
                ]
            }
        }
        write_json(out_base / f"{name}.json", data)
        count += 1
    return count

# =============================================================================
# 2. PLACED FEATURES
# Defines where the ore spawns — Y range, frequency, biome filter
# =============================================================================

def generate_placed_features(resource_path: Path) -> int:
    out_base = resource_path / "data" / MOD_ID / "worldgen" / "placed_feature"
    count = 0
    for ore in ORES:
        name = ore["name"]
        data = {
            "feature": f"{MOD_ID}:{name}",
            "placement": [
                # How many veins attempt to place per chunk
                {
                    "type": "minecraft:count",
                    "count": ore["veins_per_chunk"]
                },
                # Scatter attempts randomly within the chunk column
                {
                    "type": "minecraft:in_square"
                },
                # Y range — absolute (not relative to surface)
                {
                    "type": "minecraft:height_range",
                    "height": {
                        "type": "minecraft:uniform",
                        "min_inclusive": {
                            "absolute": ore["min_y"]
                        },
                        "max_inclusive": {
                            "absolute": ore["max_y"]
                        }
                    }
                },
                # Only place in valid biomes
                {
                    "type": "minecraft:biome"
                }
            ]
        }
        write_json(out_base / f"{name}.json", data)
        count += 1
    return count

# =============================================================================
# 3. BIOME MODIFIERS
# Injects the placed features into overworld biomes
# =============================================================================

# Set to "forge" for Forge builds, "neoforge" for NeoForge builds
LOADER = "neoforge"

def generate_biome_modifiers(resource_path: Path) -> int:
    out_base = resource_path / "data" / MOD_ID / LOADER / "biome_modifier"
    count = 0
    for ore in ORES:
        name = ore["name"]
        data = {
            "type": f"{LOADER}:add_features",
            "biomes": "#minecraft:is_overworld",
            "features": f"{MOD_ID}:{name}",
            "step": "underground_ores"
        }
        write_json(out_base / f"{name}.json", data)
        count += 1
    return count

# =============================================================================
# 4. LOOT TABLES
# Silk touch -> drops the ore block itself
# No silk touch -> drops raw ore/gem, fortune applies where relevant
# =============================================================================

def make_ore_loot_table(ore: dict) -> dict:
    ore_block_id = f"{MOD_ID}:{ore['name']}"
    # Some ores drop a "raw" item (iron, gold, copper) rather than the ingot.
    # The drop name can't always be derived from the ore name, so we use an
    # explicit mapping in the ore definition instead.
    drop_item_id = f"{MOD_ID}:{ore['compressed_drop']}"
    drop_count   = 1   # 1 compressed item = 9x vanilla already
    use_fortune  = ore["fortune_bonus"]

    # In 1.21.1 the silk touch condition uses minecraft:table_bonus
    # with a list approach, or more reliably the has_enchantment predicate.
    # The cleanest approach that works in 1.21.1 is using
    # minecraft:match_tool with the new "predicates" -> "enchantments" format.
    silk_touch_condition = {
        "condition": "minecraft:match_tool",
        "predicate": {
            "predicates": {
                "minecraft:enchantments": [
                    {
                        "enchantments": "minecraft:silk_touch",
                        "levels": { "min": 1 }
                    }
                ]
            }
        }
    }

    # Fortune bonus using ore_drops formula
    fortune_function = {
        "function": "minecraft:apply_bonus",
        "enchantment": "minecraft:fortune",
        "formula": "minecraft:ore_drops"
    }

    # Normal drop functions
    drop_functions = [
        {
            "function": "minecraft:set_count",
            "count": drop_count
        }
    ]
    if use_fortune:
        drop_functions.append(fortune_function)
    drop_functions.append({ "function": "minecraft:explosion_decay" })

    return {
        "type": "minecraft:block",
        "pools": [
            {
                # Silk touch pool — drops the ore block itself
                "rolls": 1,
                "bonus_rolls": 0,
                "entries": [
                    {
                        "type": "minecraft:item",
                        "name": ore_block_id
                    }
                ],
                "conditions": [silk_touch_condition]
            },
            {
                # Normal pool — drops raw ore/gem with fortune support
                "rolls": 1,
                "bonus_rolls": 0,
                "entries": [
                    {
                        "type": "minecraft:item",
                        "name": drop_item_id,
                        "functions": drop_functions
                    }
                ],
                "conditions": [
                    {
                        "condition": "minecraft:inverted",
                        "term": silk_touch_condition
                    }
                ]
            }
        ]
    }


def generate_loot_tables(resource_path: Path) -> int:
    out_base = resource_path / "data" / MOD_ID / "loot_table" / "blocks"
    count = 0
    for ore in ORES:
        write_json(out_base / f"{ore['name']}.json", make_ore_loot_table(ore))
        count += 1
    return count

# =============================================================================
# 5. BLOCKSTATES
# =============================================================================

def generate_blockstates(resource_path: Path) -> int:
    out_base = resource_path / "assets" / MOD_ID / "blockstates"
    count = 0
    for ore in ORES:
        name = ore["name"]
        write_json(out_base / f"{name}.json", {
            "variants": {
                "": { "model": f"{MOD_ID}:block/{name}" }
            }
        })
        count += 1
    return count

# =============================================================================
# 6. BLOCK MODELS
# =============================================================================

def generate_block_models(resource_path: Path) -> int:
    out_base = resource_path / "assets" / MOD_ID / "models" / "block"
    count = 0
    for ore in ORES:
        name = ore["name"]
        write_json(out_base / f"{name}.json", {
            "parent": "minecraft:block/cube_all",
            "textures": { "all": f"{MOD_ID}:block/{name}" }
        })
        count += 1
    return count

# =============================================================================
# 7. ITEM MODELS
# =============================================================================

def generate_item_models(resource_path: Path) -> int:
    out_base = resource_path / "assets" / MOD_ID / "models" / "item"
    count = 0
    for ore in ORES:
        name = ore["name"]
        write_json(out_base / f"{name}.json", {
            "parent": f"{MOD_ID}:block/{name}"
        })
        count += 1
    return count

# =============================================================================
# 8. LANGUAGE FILE — merge into existing en_us.json
# =============================================================================

def update_lang(resource_path: Path) -> int:
    lang_path = resource_path / "assets" / MOD_ID / "lang" / "en_us.json"

    # Load existing lang file if it exists
    if lang_path.exists():
        with open(lang_path, "r", encoding="utf-8") as f:
            entries = json.load(f)
    else:
        entries = {}

    count = 0
    for ore in ORES:
        key = f"block.{MOD_ID}.{ore['name']}"
        if key not in entries:
            entries[key] = ore["display"]
            count += 1

    with open(lang_path, "w", encoding="utf-8") as f:
        json.dump(entries, f, indent=4)

    return count


# =============================================================================
# 8. BLOCK TAGS
# Tells Minecraft which tool mines these blocks and what tier is required.
# Without these, requiresCorrectToolForDrops() causes the block to drop nothing.
# =============================================================================

def generate_block_tags(resource_path: Path) -> int:
    out_base = resource_path / "data" / "minecraft" / "tags" / "block"

    ore_ids = [f"{MOD_ID}:{ore['name']}" for ore in ORES]

    # Additive merge — never overwrites existing entries from generate_data.py
    write_tag_merged(out_base / "mineable" / "pickaxe.json", ore_ids)
    write_tag_merged(out_base / "needs_diamond_tool.json", ore_ids)

    return 2

# =============================================================================
# SUMMARY & MAIN
# =============================================================================

def print_summary(resource_path: Path):
    print(f"""
  What will be generated
  ─────────────────────────────────────────
  Ores                     : {len(ORES)}
  Configured feature JSONs : {len(ORES)}
  Placed feature JSONs     : {len(ORES)}
  Biome modifier JSONs     : {len(ORES)}
  Loot table JSONs         : {len(ORES)}
  Blockstate JSONs         : {len(ORES)}
  Block model JSONs        : {len(ORES)}
  Item model JSONs         : {len(ORES)}
  Lang entries added       : up to {len(ORES)}
  ─────────────────────────────────────────
  Total files              : {len(ORES) * 7 + 1}

  Ore spawn summary:
""")
    for ore in ORES:
        print(f"    {ore['display']:<30} "
              f"vein={ore['vein_size']}  "
              f"count={ore['veins_per_chunk']}/chunk  "
              f"Y {ore['min_y']} to {ore['max_y']}")
    print(f"\n  Output root: {resource_path}")


def main():
    print("=" * 60)
    print("  Ultimate Compression -- Ore Generator")
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
    tags = generate_block_tags(resource_path)
    cf   = generate_configured_features(resource_path)
    pf   = generate_placed_features(resource_path)
    bm_o = generate_biome_modifiers(resource_path)
    lt   = generate_loot_tables(resource_path)
    bs   = generate_blockstates(resource_path)
    bm   = generate_block_models(resource_path)
    im   = generate_item_models(resource_path)
    lang = update_lang(resource_path)

    print(f"""
  Done!
  ─────────────────────────────────────────
  Block tag files          : {tags}
  Configured features      : {cf}
  Placed features          : {pf}
  Biome modifiers          : {bm_o}
  Loot tables              : {lt}
  Blockstates              : {bs}
  Block models             : {bm}
  Item models              : {im}
  Lang entries added       : {lang}
    """)
    print("=" * 60)


if __name__ == "__main__":
    main()