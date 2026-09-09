package com.voidcallerz.uc;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class UCFallingBlock extends FallingBlock {

    public static final MapCodec<UCFallingBlock> CODEC =
        simpleCodec(UCFallingBlock::new);

    public UCFallingBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public int getDustColor(BlockState arg0, BlockGetter arg1, BlockPos arg2) {
        return this.defaultMapColor().col;
    }
}