package net.fawnoculus.vanillaBackrooms.blocks.custom;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import xyz.nucleoid.packettweaker.PacketContext;

public class FakeSkyBlock extends Block implements PolymerBlock {
	public FakeSkyBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
		return Blocks.LIGHT_BLUE_CONCRETE.getDefaultState();
	}
}
