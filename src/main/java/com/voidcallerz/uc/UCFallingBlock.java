package com.voidcallerz.uc;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class UCFallingBlock extends FallingBlock {

    public static final MapCodec<UCFallingBlock> CODEC =
        simpleCodec(UCFallingBlock::new);

    public UCFallingBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }
}