package net.fawnoculus.vanillaBackrooms.blocks.entities;

import net.fawnoculus.vanillaBackrooms.blocks.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

public class FlickeringLightBE extends BlockEntity {
	private int flickerChance = 9;

	public FlickeringLightBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FLICKERING_LIGHT_BE, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, FlickeringLightBE entity) {
		if (world.getRandom().nextBetween(0, entity.flickerChance) == 0) {
			world.setBlockState(pos, state.cycle(Properties.LIT));
		}
		entity.markDirty();
	}

	public void onUse(PlayerEntity player) {
		if (player.getGameMode() == GameMode.ADVENTURE) {
			return;
		}

		if (player.isSneaking()) {
			this.flickerChance--;
		} else {
			this.flickerChance++;
		}

		if (this.flickerChance < 0) {
			this.flickerChance = 99;
		}
		if (this.flickerChance > 99) {
			this.flickerChance = 0;
		}

		player.sendMessage(
		  Text.translatableWithFallback(
			"message.vanilla_backrooms.flicker_chance",
			"Flicker Chance: 1/%1$s (%2$s%%) pro Tick",
			this.flickerChance + 1,
			String.format("%1$#.2f", 100.0 / (this.flickerChance + 1))
		  ), true
		);

	}


	@Override
	protected void writeData(WriteView view) {
		super.writeData(view);
		view.putInt("flicker_chance", this.flickerChance);
	}

	@Override
	protected void readData(ReadView view) {
		super.readData(view);
		this.flickerChance = view.getInt("flicker_chance", 9);
	}
}
