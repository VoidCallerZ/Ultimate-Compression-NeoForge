package com.voidcallerz.uc;

public class ModConstants {

    public static final String MOD_ID = "uc";
    public static final String MOD_NAME = "Ultimate Compression";

    // Number of compression tiers (1 = Compressed, 2 = Double Compressed, etc.)
    public static final int TIER_COUNT = 2;

    // Tier name prefixes — add more here if you ever increase TIER_COUNT
    public static final String[] TIER_PREFIXES = {
        "compressed",
        "double_compressed"
    };

    // Human-readable tier display names (used in item/block display names)
    public static final String[] TIER_DISPLAY_NAMES = {
        "Compressed",
        "Double Compressed"
    };
}