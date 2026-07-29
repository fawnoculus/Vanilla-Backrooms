package net.fawnoculus.vanilla_backrooms.blocks.custom;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import xyz.nucleoid.packettweaker.PacketContext;

public class FakeSkyBlock extends Block implements PolymerBlock {
    public FakeSkyBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.LIGHT_BLUE_CONCRETE.defaultBlockState();
    }
}
