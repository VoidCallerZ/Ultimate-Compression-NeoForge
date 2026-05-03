"""
Ultimate Compression — Texture Generator v5
=============================================
Extracts vanilla textures from your Minecraft jar, applies per-tier
darkening + saturation + border effects, and writes them into your mod's
resource folder. Also generates a preview sheet for blocks and items.

Changelog:
  v5 — Added tool item textures and armor layer textures.

Requirements:
    pip install Pillow

Usage:
    python generate_textures.py
"""

import os
import sys
import zipfile
from pathlib import Path
from PIL import Image, ImageEnhance, ImageDraw

# =============================================================================
# CONFIGURATION
# =============================================================================

MOD_ID        = "uc"
TIER_PREFIXES = ["compressed", "double_compressed"]

TIER_BRIGHTNESS = [0.75, 0.55]
TIER_SATURATION = [1.30, 1.60]

BORDER_THICKNESS = 1
BORDER_STRENGTH  = 0.72

# Block materials — must match UCBlocks.java
MATERIALS = [
    # Vanilla blocks
    "stone", "cobblestone", "dirt", "grass_block", "sand", "gravel",
    "netherrack", "soul_sand", "soul_soil", "blackstone", "deepslate",
    "calcite", "tuff", "obsidian",
    # Stone variants
    "andesite", "diorite", "granite", "cobbled_deepslate",
    # Ore blocks
    "iron_block", "gold_block", "diamond_block", "emerald_block",
    "lapis_block", "redstone_block", "coal_block", "copper_block",
    "netherite_block", "amethyst_block", "raw_iron_block", "raw_gold_block",
    "raw_copper_block",
    # Logs
    "oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log",
    "dark_oak_log", "mangrove_log", "cherry_log", "bamboo_block",
    "crimson_stem", "warped_stem",
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

TEXTURE_OVERRIDES = {
    "grass_block":   "grass_block_side",
    "bamboo_block":  "bamboo_planks",
    "crimson_stem":  "crimson_stem",
    "warped_stem":   "warped_stem",
    "crimson_planks": "crimson_planks",
    "warped_planks":  "warped_planks",
}

LOG_TOP_TEXTURES = {
    "oak_log":      "oak_log_top",
    "spruce_log":   "spruce_log_top",
    "birch_log":    "birch_log_top",
    "jungle_log":   "jungle_log_top",
    "acacia_log":   "acacia_log_top",
    "dark_oak_log": "dark_oak_log_top",
    "mangrove_log": "mangrove_log_top",
    "cherry_log":   "cherry_log_top",
    "bamboo_block": "bamboo_planks",
    "crimson_stem": "crimson_stem_top",
    "warped_stem":  "warped_stem_top",
    "grass_block":  "grass_block_top",
}

COMPRESSED_ITEM_TEXTURES = {
    "compressed_raw_iron":        "raw_iron",
    "compressed_raw_gold":        "raw_gold",
    "compressed_raw_copper":      "raw_copper",
    "compressed_iron_ingot":      "iron_ingot",
    "compressed_gold_ingot":      "gold_ingot",
    "compressed_copper_ingot":    "copper_ingot",
    "compressed_netherite_ingot": "netherite_ingot",
    "compressed_diamond":         "diamond",
    "compressed_emerald":         "emerald",
    "compressed_amethyst_shard":  "amethyst_shard",
    "compressed_quartz":          "quartz",
    "compressed_lapis":           "lapis_lazuli",
    "compressed_redstone":        "redstone",
    "compressed_flint":           "flint",
    "compressed_stick":           "stick",
    "compressed_leather":         "leather",
    "compressed_bone":            "bone",
    "compressed_string":          "string",
    "compressed_feather":         "feather",
    "compressed_iron_nugget":     "iron_nugget",
    "compressed_gold_nugget":     "gold_nugget",
    "compressed_coal":            "coal",
    "compressed_blaze_rod":       "blaze_rod",
}

# -------------------------------------------------------------------------
# TOOL ITEM TEXTURES
# Maps compressed tool item name -> vanilla item texture stem
# Tool types: sword, pickaxe, axe, shovel, hoe
# -------------------------------------------------------------------------
TOOL_MATERIALS = {
    "wood":      "wooden",
    "stone":     "stone",
    "iron":      "iron",
    "gold":      "golden",
    "diamond":   "diamond",
    "netherite": "netherite",
}

TOOL_TYPES = ["sword", "pickaxe", "axe", "shovel", "hoe"]

# Build the full tool texture map: compressed_iron_sword -> iron_sword etc.
COMPRESSED_TOOL_TEXTURES = {}
for mat, vanilla_mat in TOOL_MATERIALS.items():
    for tool in TOOL_TYPES:
        COMPRESSED_TOOL_TEXTURES[f"compressed_{mat}_{tool}"] = f"{vanilla_mat}_{tool}"

# -------------------------------------------------------------------------
# ARMOR ITEM TEXTURES
# Maps compressed armor item name -> vanilla item texture stem
# -------------------------------------------------------------------------
ARMOR_MATERIALS = {
    "iron":      "iron",
    "gold":      "golden",
    "diamond":   "diamond",
    "netherite": "netherite",
}

ARMOR_PIECES = ["helmet", "chestplate", "leggings", "boots"]

COMPRESSED_ARMOR_ITEM_TEXTURES = {}
for mat, vanilla_mat in ARMOR_MATERIALS.items():
    for piece in ARMOR_PIECES:
        COMPRESSED_ARMOR_ITEM_TEXTURES[f"compressed_{mat}_{piece}"] = f"{vanilla_mat}_{piece}"

# -------------------------------------------------------------------------
# ARMOR LAYER TEXTURES
# These are the textures shown ON the player body when wearing armor.
# Vanilla stores them in assets/minecraft/textures/models/armor/
# layer_1 = body (helmet, chestplate, boots)
# layer_2 = leggings
# -------------------------------------------------------------------------
ARMOR_LAYER_SOURCES = {
    "iron":      "iron",
    "gold":      "gold",
    "diamond":   "diamond",
    "netherite": "netherite",
}


# -------------------------------------------------------------------------
# COMPRESSED ORE TEXTURES
# Maps ore registry name -> vanilla deepslate ore texture stem
# -------------------------------------------------------------------------
COMPRESSED_ORE_TEXTURES = {
    "compressed_coal_ore":     "deepslate_coal_ore",
    "compressed_iron_ore":     "deepslate_iron_ore",
    "compressed_gold_ore":     "deepslate_gold_ore",
    "compressed_copper_ore":   "deepslate_copper_ore",
    "compressed_diamond_ore":  "deepslate_diamond_ore",
    "compressed_emerald_ore":  "deepslate_emerald_ore",
    "compressed_lapis_ore":    "deepslate_lapis_ore",
    "compressed_redstone_ore": "deepslate_redstone_ore",
}

# -------------------------------------------------------------------------
# COMPRESSION CATALYST
# Base texture sourced from iron ingot + redstone composite in write step
# -------------------------------------------------------------------------
CATALYST_NAME = "compression_catalyst"
# =============================================================================
# PATH DETECTION
# =============================================================================

def find_minecraft_jar():
    candidates = []
    if sys.platform == "win32":
        appdata = Path(os.environ.get("APPDATA", ""))
        candidates.append(appdata / ".minecraft" / "versions")
    elif sys.platform == "darwin":
        candidates.append(Path.home() / "Library" / "Application Support" / "minecraft" / "versions")
    else:
        candidates.append(Path.home() / ".minecraft" / "versions")

    jars = []
    for base in candidates:
        if base.exists():
            jars.extend(base.glob("*/*.jar"))

    release_jars = [j for j in jars if j.parent.name[0].isdigit()]
    release_jars.sort(key=lambda p: p.parent.name, reverse=True)

    if not release_jars:
        raise FileNotFoundError("Could not find a Minecraft jar. Launch Minecraft at least once first.")

    print(f"  Found Minecraft jar: {release_jars[0]}")
    return release_jars[0]


def find_mod_resource_path():
    script_dir = Path(__file__).parent
    for folder in [script_dir, script_dir.parent, script_dir.parent.parent]:
        candidate = folder / "src" / "main" / "resources"
        if candidate.exists():
            return candidate
    raise FileNotFoundError("Could not find src/main/resources. Run from your mod project root.")

# =============================================================================
# TEXTURE EXTRACTION
# =============================================================================

def extract_from_jar(jar, folder: str, stem: str):
    """Extract one texture from a specific folder inside the jar."""
    path = f"assets/minecraft/textures/{folder}/{stem}.png"
    if path not in jar.namelist():
        return None
    with jar.open(path) as f:
        img = Image.open(f).convert("RGBA")
        # Don't crop armor layer textures — they're intentionally tall
        if folder != "models/armor" and img.height > img.width:
            img = img.crop((0, 0, img.width, img.width))
        return img.copy()


def extract_all_textures(jar_path):
    side_textures  = {}
    top_textures   = {}
    item_textures  = {}
    tool_textures  = {}
    armor_item_textures  = {}
    armor_layer_textures = {}

    with zipfile.ZipFile(jar_path, "r") as jar:

        print("\n  Block side textures:")
        for material in MATERIALS:
            stem = TEXTURE_OVERRIDES.get(material, material)
            img  = extract_from_jar(jar, "block", stem)
            if img:
                side_textures[material] = img
                print(f"    v {material}")
            else:
                print(f"    x {material} -- not found, skipping")

        print("\n  Block top textures:")
        for material, top_stem in LOG_TOP_TEXTURES.items():
            img = extract_from_jar(jar, "block", top_stem)
            if img:
                top_textures[material] = img
                print(f"    v {material} top")
            else:
                print(f"    x {material} top -- not found, skipping")

        print("\n  Item textures:")
        for comp_name, vanilla_stem in COMPRESSED_ITEM_TEXTURES.items():
            img = extract_from_jar(jar, "item", vanilla_stem)
            if img:
                item_textures[comp_name] = img
                print(f"    v {comp_name}")
            else:
                print(f"    x {comp_name} -- not found, skipping")

        print("\n  Tool textures:")
        for comp_name, vanilla_stem in COMPRESSED_TOOL_TEXTURES.items():
            img = extract_from_jar(jar, "item", vanilla_stem)
            if img:
                tool_textures[comp_name] = img
                print(f"    v {comp_name}")
            else:
                print(f"    x {comp_name} -- not found, skipping")

        print("\n  Armor item textures:")
        for comp_name, vanilla_stem in COMPRESSED_ARMOR_ITEM_TEXTURES.items():
            img = extract_from_jar(jar, "item", vanilla_stem)
            if img:
                armor_item_textures[comp_name] = img
                print(f"    v {comp_name}")
            else:
                print(f"    x {comp_name} -- not found, skipping")

        print("\n  Armor layer textures:")
        for mat, vanilla_mat in ARMOR_LAYER_SOURCES.items():
            # In 1.21.1, vanilla armor textures moved to entity/equipment/
            # layer_1 (body) = humanoid/<name>.png
            # layer_2 (legs) = humanoid_leggings/<name>.png
            # We fall back to the old models/armor path if the new one isn't found
            for layer, folder, stem in [
                (1, "entity/equipment/humanoid",          vanilla_mat),
                (2, "entity/equipment/humanoid_leggings", vanilla_mat),
            ]:
                img = extract_from_jar(jar, folder, stem)
                # Fallback to old path
                if img is None:
                    img = extract_from_jar(jar, "models/armor", f"{vanilla_mat}_layer_{layer}")
                if img:
                    armor_layer_textures[f"compressed_{mat}_layer_{layer}"] = img
                    print(f"    v compressed_{mat}_layer_{layer}")
                else:
                    print(f"    x compressed_{mat}_layer_{layer} -- not found in jar")


        print("\n  Ore textures:")
        ore_textures = {}
        for comp_name, vanilla_stem in COMPRESSED_ORE_TEXTURES.items():
            img = extract_from_jar(jar, "block", vanilla_stem)
            if img:
                ore_textures[comp_name] = img
                print(f"    v {comp_name}")
            else:
                print(f"    x {comp_name} -- not found, skipping")

        print("\n  Compression catalyst base textures:")
        catalyst_iron = extract_from_jar(jar, "item", "iron_ingot")
        catalyst_redstone = extract_from_jar(jar, "item", "redstone")

    return side_textures, top_textures, item_textures, tool_textures, armor_item_textures, armor_layer_textures, ore_textures, catalyst_iron, catalyst_redstone

# =============================================================================
# TEXTURE PROCESSING
# =============================================================================

def apply_border_darkening(image):
    img    = image.copy()
    pixels = img.load()
    w, h   = img.size
    for x in range(w):
        for y in range(h):
            if (x < BORDER_THICKNESS or x >= w - BORDER_THICKNESS or
                    y < BORDER_THICKNESS or y >= h - BORDER_THICKNESS):
                r, g, b, a = pixels[x, y]
                pixels[x, y] = (
                    int(r * BORDER_STRENGTH),
                    int(g * BORDER_STRENGTH),
                    int(b * BORDER_STRENGTH),
                    a
                )
    return img


def apply_tier_effects(base_image, tier_index):
    r, g, b, a = base_image.split()
    rgb = Image.merge("RGB", (r, g, b))
    rgb = ImageEnhance.Color(rgb).enhance(TIER_SATURATION[tier_index])
    rgb = ImageEnhance.Brightness(rgb).enhance(TIER_BRIGHTNESS[tier_index])
    result = rgb.convert("RGBA")
    result.putalpha(a)
    return apply_border_darkening(result)


def apply_effects_no_border(base_image, tier_index):
    """
    Apply brightness/saturation without the border darkening effect.
    Used for armor layer textures — the border effect looks wrong on the
    large non-square armor layout textures.
    """
    r, g, b, a = base_image.split()
    rgb = Image.merge("RGB", (r, g, b))
    rgb = ImageEnhance.Color(rgb).enhance(TIER_SATURATION[tier_index])
    rgb = ImageEnhance.Brightness(rgb).enhance(TIER_BRIGHTNESS[tier_index])
    result = rgb.convert("RGBA")
    result.putalpha(a)
    return result

# =============================================================================
# OUTPUT
# =============================================================================

def write_block_textures(side_textures, top_textures, resource_path):
    out_base = resource_path / "assets" / MOD_ID / "textures" / "block"
    out_base.mkdir(parents=True, exist_ok=True)
    for material, base_img in side_textures.items():
        for tier_index, prefix in enumerate(TIER_PREFIXES):
            processed = apply_tier_effects(base_img, tier_index)
            processed.save(out_base / f"{prefix}_{material}.png")
            if material in top_textures:
                top_processed = apply_tier_effects(top_textures[material], tier_index)
                top_processed.save(out_base / f"{prefix}_{material}_top.png")
        print(f"    v {material}")


def write_item_textures(item_textures, resource_path, jar_path):
    out_base = resource_path / "assets" / MOD_ID / "textures" / "item"
    out_base.mkdir(parents=True, exist_ok=True)
    for name, base_img in item_textures.items():
        processed = apply_tier_effects(base_img, 0)
        processed.save(out_base / f"{name}.png")
        print(f"    v {name}")


def write_tool_textures(tool_textures, resource_path):
    """Tool item textures — same darkening as other items, no border."""
    out_base = resource_path / "assets" / MOD_ID / "textures" / "item"
    out_base.mkdir(parents=True, exist_ok=True)
    for name, base_img in tool_textures.items():
        processed = apply_tier_effects(base_img, 0)
        processed.save(out_base / f"{name}.png")
        print(f"    v {name}")


def write_armor_item_textures(armor_item_textures, resource_path):
    """Armor item textures (shown in inventory)."""
    out_base = resource_path / "assets" / MOD_ID / "textures" / "item"
    out_base.mkdir(parents=True, exist_ok=True)
    for name, base_img in armor_item_textures.items():
        processed = apply_tier_effects(base_img, 0)
        processed.save(out_base / f"{name}.png")
        print(f"    v {name}")


def write_armor_layer_textures(armor_layer_textures, resource_path):
    """
    Armor layer textures (shown on the player body).
    Written to BOTH locations for compatibility:
      - assets/uc/textures/models/armor/  (old path, Forge 1.21.1)
      - assets/uc/textures/entity/equipment/humanoid/       (layer_1, new path)
      - assets/uc/textures/entity/equipment/humanoid_leggings/ (layer_2, new path)
    No border darkening — the large layout texture looks wrong with it.
    """
    old_base     = resource_path / "assets" / MOD_ID / "textures" / "models" / "armor"
    new_humanoid = resource_path / "assets" / MOD_ID / "textures" / "entity" / "equipment" / "humanoid"
    new_leggings = resource_path / "assets" / MOD_ID / "textures" / "entity" / "equipment" / "humanoid_leggings"

    for base in [old_base, new_humanoid, new_leggings]:
        base.mkdir(parents=True, exist_ok=True)

    for name, base_img in armor_layer_textures.items():
        processed = apply_effects_no_border(base_img, 0)

        # Old path: compressed_iron_layer_1.png / compressed_iron_layer_2.png
        processed.save(old_base / f"{name}.png")

        # New path: strip _layer_1 / _layer_2 suffix, use material name only
        # e.g. compressed_iron_layer_1 -> compressed_iron.png in humanoid/
        #      compressed_iron_layer_2 -> compressed_iron.png in humanoid_leggings/
        mat_name = name.rsplit("_layer_", 1)[0]   # e.g. "compressed_iron"
        layer_num = int(name.rsplit("_layer_", 1)[1])  # 1 or 2

        if layer_num == 1:
            processed.save(new_humanoid / f"{mat_name}.png")
        else:
            processed.save(new_leggings / f"{mat_name}.png")

        print(f"    v {name}")


def write_ore_textures(ore_textures, resource_path):
    """Write compressed ore textures to textures/block/."""
    out_base = resource_path / "assets" / MOD_ID / "textures" / "block"
    out_base.mkdir(parents=True, exist_ok=True)
    for name, base_img in ore_textures.items():
        processed = apply_tier_effects(base_img, 0)
        processed.save(out_base / f"{name}.png")
        print(f"    v {name}")


def write_catalyst_texture(catalyst_iron, catalyst_redstone, resource_path):
    """
    Generate compression_catalyst.png by compositing iron ingot + redstone.
    Iron ingot forms the base, redstone is overlaid at 55% opacity.
    """
    out_base = resource_path / "assets" / MOD_ID / "textures" / "item"
    out_base.mkdir(parents=True, exist_ok=True)
    out_path = out_base / f"{CATALYST_NAME}.png"
    if out_path.exists():
        print(f"    - {CATALYST_NAME}.png already exists, skipping (using your custom texture)")
        return
    if catalyst_iron and catalyst_redstone:
        base    = catalyst_iron.resize((16, 16), Image.NEAREST).convert("RGBA")
        overlay = catalyst_redstone.resize((16, 16), Image.NEAREST).convert("RGBA")
        r, g, b, a = overlay.split()
        a = a.point(lambda x: int(x * 0.55))
        overlay.putalpha(a)
        composite = base.copy()
        composite.paste(overlay, (0, 0), overlay)
        composite.save(out_path)
        print(f"    v {CATALYST_NAME}.png (iron + redstone composite)")
    else:
        print(f"    x {CATALYST_NAME}.png -- base textures not found in jar")

# =============================================================================
# PREVIEW SHEET
# =============================================================================

def generate_preview(side_textures, top_textures, item_textures, out_path):
    print("\n  Generating preview sheet...")

    CELL, PAD, LABEL = 64, 8, 180
    HEADER   = 30
    SEPARATOR = CELL + PAD

    block_rows = len(side_textures) + len(top_textures)
    item_rows  = len(item_textures)

    total_w = PAD + 3 * (CELL + PAD) + LABEL
    total_h = HEADER + block_rows * (CELL + PAD) + SEPARATOR + item_rows * (CELL + PAD) + PAD

    sheet = Image.new("RGBA", (total_w, total_h), (30, 30, 30, 255))
    draw  = ImageDraw.Draw(sheet)

    for col, header in enumerate(["Vanilla", "Tier 1", "Tier 2"]):
        draw.text((PAD + col * (CELL + PAD), 6), header, fill=(220, 220, 220, 255))

    row = 0
    for material, base_img in side_textures.items():
        y = HEADER + PAD + row * (CELL + PAD)
        for col, img in enumerate([base_img] + [apply_tier_effects(base_img, t) for t in range(len(TIER_PREFIXES))]):
            scaled = img.resize((CELL, CELL), Image.NEAREST)
            sheet.paste(scaled, (PAD + col * (CELL + PAD), y), scaled)
        draw.text((PAD + 3 * (CELL + PAD), y + CELL // 2 - 6), material, fill=(200, 200, 200, 255))
        row += 1
        if material in top_textures:
            y = HEADER + PAD + row * (CELL + PAD)
            top_img = top_textures[material]
            for col, img in enumerate([top_img] + [apply_tier_effects(top_img, t) for t in range(len(TIER_PREFIXES))]):
                scaled = img.resize((CELL, CELL), Image.NEAREST)
                sheet.paste(scaled, (PAD + col * (CELL + PAD), y), scaled)
            draw.text((PAD + 3 * (CELL + PAD), y + CELL // 2 - 6), f"{material} (top)", fill=(160, 200, 160, 255))
            row += 1

    item_section_y = HEADER + block_rows * (CELL + PAD) + SEPARATOR
    draw.text((PAD, item_section_y - 20), "── Items (single tier) ──", fill=(180, 180, 100, 255))
    for col, header in enumerate(["Vanilla", "Compressed", ""]):
        draw.text((PAD + col * (CELL + PAD), item_section_y - 6), header, fill=(220, 220, 220, 255))

    for i, (name, base_img) in enumerate(item_textures.items()):
        y = item_section_y + PAD + i * (CELL + PAD)
        compressed = apply_tier_effects(base_img, 0)
        for col, img in enumerate([base_img, compressed]):
            scaled = img.resize((CELL, CELL), Image.NEAREST)
            sheet.paste(scaled, (PAD + col * (CELL + PAD), y), scaled)
        draw.text((PAD + 3 * (CELL + PAD), y + CELL // 2 - 6), name, fill=(200, 200, 200, 255))

    out_path.parent.mkdir(parents=True, exist_ok=True)
    sheet.save(out_path)
    print(f"  Preview saved to: {out_path}")

# =============================================================================
# MAIN
# =============================================================================

def main():
    print("=" * 60)
    print("  Ultimate Compression -- Texture Generator v5")
    print("=" * 60)

    print("\n[1/4] Locating paths...")
    jar_path      = find_minecraft_jar()
    resource_path = find_mod_resource_path()
    print(f"  Mod resources: {resource_path}")

    print("\n[2/4] Extracting vanilla textures...")
    (side_textures, top_textures, item_textures,
     tool_textures, armor_item_textures, armor_layer_textures,
     ore_textures, catalyst_iron, catalyst_redstone) = extract_all_textures(jar_path)
    print(f"\n  {len(side_textures)} block, {len(top_textures)} top, "
          f"{len(item_textures)} item, {len(tool_textures)} tool, "
          f"{len(armor_item_textures)} armor item, "
          f"{len(armor_layer_textures)} armor layer, "
          f"{len(ore_textures)} ore textures extracted.")

    print("\n[3/4] Generating preview (blocks + items)...")
    preview_path = Path(__file__).parent / "texture_preview.png"
    generate_preview(side_textures, top_textures, item_textures, preview_path)

    block_files       = (len(side_textures) + len(top_textures)) * len(TIER_PREFIXES)
    item_files        = len(item_textures)
    tool_files        = len(tool_textures)
    armor_item_files  = len(armor_item_textures)
    armor_layer_files = len(armor_layer_textures)
    ore_files         = len(ore_textures)
    total             = block_files + item_files + tool_files + armor_item_files + armor_layer_files + ore_files

    print(f"\n[4/4] Ready to write {total} textures total:")
    print(f"  {block_files} block  |  {item_files} item  |  {tool_files} tool  "
          f"|  {armor_item_files} armor item  |  {armor_layer_files} armor layer  |  {ore_files} ore")

    answer = input("\n  Check texture_preview.png first.\n  Write textures now? [y/N]: ").strip().lower()

    if answer == "y":
        print("\n  Writing block textures...")
        write_block_textures(side_textures, top_textures, resource_path)
        print("\n  Writing item textures...")
        write_item_textures(item_textures, resource_path, jar_path)
        print("\n  Writing tool textures...")
        write_tool_textures(tool_textures, resource_path)
        print("\n  Writing armor item textures...")
        write_armor_item_textures(armor_item_textures, resource_path)
        print("\n  Writing armor layer textures...")
        write_armor_layer_textures(armor_layer_textures, resource_path)
        print("\n  Writing ore textures...")
        write_ore_textures(ore_textures, resource_path)
        print("\n  Writing compression catalyst texture...")
        write_catalyst_texture(catalyst_iron, catalyst_redstone, resource_path)
        print(f"\n  Done! {total} textures written.")
    else:
        print("\n  Aborted -- no files written.")

    print("\n" + "=" * 60)


if __name__ == "__main__":
    main()