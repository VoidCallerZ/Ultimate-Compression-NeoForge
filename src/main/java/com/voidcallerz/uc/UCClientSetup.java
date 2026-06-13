package com.voidcallerz.uc;

import com.voidcallerz.uc.registry.UCBlocks;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

@EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.CLIENT)
public class UCClientSetup {

    private static final BlockTintSource FOLIAGE_TINT = new BlockTintSource() {
        @Override
        public int color(BlockState state) {
            return FoliageColor.FOLIAGE_DEFAULT;
        }

        @Override
        public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
            return BiomeColors.getAverageFoliageColor(level, pos);
        }
    };

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        for (String name : UCBlocks.LEAVES_MATERIALS) {
            for (int tier = 0; tier < 2; tier++) {
                String registryName = (tier == 0 ? "compressed_" : "double_compressed_") + name;
                var block = UCBlocks.ALL_BLOCKS.get(registryName);
                if (block != null) {
                    event.register(List.of(FOLIAGE_TINT), block.value());
                }
            }
        }
    }
}