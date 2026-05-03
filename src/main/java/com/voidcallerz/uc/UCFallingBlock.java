package com.voidcallerz.uc;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.FallingBlock;

public class UCFallingBlock extends FallingBlock {
    public UCFallingBlock(Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'codec'");
    }
}
