package net.fawnoculus.vanillaBackrooms.blocks.custom;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import xyz.nucleoid.packettweaker.PacketContext;

public class ActiveLightBlock extends Block implements PolymerBlock {
	public ActiveLightBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
		return Blocks.REDSTONE_LAMP.getDefaultState().with(Properties.LIT, true);
	}
}
