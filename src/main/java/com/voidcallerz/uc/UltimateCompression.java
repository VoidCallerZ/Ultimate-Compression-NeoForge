package com.voidcallerz.uc;

import com.voidcallerz.uc.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ModConstants.MOD_ID)
public class UltimateCompression {

    private static final Logger LOGGER = LogUtils.getLogger();

    public UltimateCompression(IEventBus modEventBus) {
        // In NeoForge the mod event bus is injected directly into the constructor
        // rather than obtained via FMLJavaModLoadingContext

        // Compressed blocks and their BlockItems
        UCBlocks.BLOCKS.register(modEventBus);
        UCItems.ITEMS.register(modEventBus);

        // Standalone compressed items (ingots, gems, dusts, catalyst)
        UCItemRegistry.ITEMS.register(modEventBus);

        // Compressed ore blocks and their BlockItems
        UCOres.BLOCKS.register(modEventBus);
        UCOres.ITEMS.register(modEventBus);

        // Armor materials must be registered before equipment items
        UCArmorMaterials.ARMOR_MATERIALS.register(modEventBus);

        // Tools and armor items
        UCEquipment.ITEMS.register(modEventBus);

        // Creative tabs (must register after all item registries)
        UCItems.CREATIVE_TABS.register(modEventBus);

        LOGGER.info("{} is loading — {} tiers, auto-registration active.",
            ModConstants.MOD_NAME, ModConstants.TIER_COUNT);
    }
}