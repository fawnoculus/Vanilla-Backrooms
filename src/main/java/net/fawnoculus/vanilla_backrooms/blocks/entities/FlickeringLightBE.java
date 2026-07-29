package net.fawnoculus.vanilla_backrooms.blocks.entities;

import net.fawnoculus.vanilla_backrooms.blocks.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class FlickeringLightBE extends BlockEntity {
    private int flickerChance = 9;

    public FlickeringLightBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLICKERING_LIGHT_BE, pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, FlickeringLightBE entity) {
        if (world.getRandom().nextIntBetweenInclusive(0, entity.flickerChance) == 0) {
            world.setBlockAndUpdate(pos, state.cycle(BlockStateProperties.LIT));
        }
        entity.setChanged();
    }

    public void onUse(Player player) {
        if (player.gameMode() == GameType.ADVENTURE) {
            return;
        }

        if (player.isShiftKeyDown()) {
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

        player.displayClientMessage(
          Component.translatableWithFallback(
            "message.vanilla_backrooms.flicker_chance",
            "Flicker chance: 1/%1$s (%2$s%%) pro tick",
            this.flickerChance + 1,
            String.format("%1$#.2f", 100.0 / (this.flickerChance + 1))
          ), true
        );

    }


    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        view.putInt("flicker_chance", this.flickerChance);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        this.flickerChance = view.getIntOr("flicker_chance", 9);
    }
}
