package com.voidcallerz.uc;

import com.voidcallerz.uc.registry.UCBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.CLIENT)
public class UCClientSetup {

    /**
     * Register biome foliage tint colors for compressed leaves blocks.
     * Without this, leaves render gray because the biome color is never applied.
     */
    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        for (String name : UCBlocks.LEAVES_MATERIALS) {
            for (int tier = 0; tier < 2; tier++) {
                String registryName = (tier == 0 ? "compressed_" : "double_compressed_") + name;
                var block = UCBlocks.ALL_BLOCKS.get(registryName);
                if (block != null) {
                    event.register(
                        (BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) ->
                            level != null && pos != null
                                ? BiomeColors.getAverageFoliageColor(level, pos)
                                : FoliageColor.FOLIAGE_DEFAULT,
                        block.value()
                    );
                }
            }
        }
    }
}