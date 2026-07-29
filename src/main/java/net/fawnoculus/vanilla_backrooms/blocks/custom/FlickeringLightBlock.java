package net.fawnoculus.vanilla_backrooms.blocks.custom;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fawnoculus.vanilla_backrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanilla_backrooms.blocks.entities.FlickeringLightBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import xyz.nucleoid.packettweaker.PacketContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlickeringLightBlock extends BaseEntityBlock implements PolymerBlock {
    public static final MapCodec<FlickeringLightBlock> CODEC = simpleCodec(FlickeringLightBlock::new);

    public FlickeringLightBlock(Properties settings) {
        super(settings);

        registerDefaultState(this.defaultBlockState()
          .setValue(BlockStateProperties.LIT, false)
        );
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.REDSTONE_LAMP.defaultBlockState().setValue(BlockStateProperties.LIT, state.getValueOrElse(BlockStateProperties.LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.LIT);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, BlockState state, BlockEntityType<T> type) {
        if (world.isClientSide()) return null;
        return createTickerHelper(type, ModBlockEntities.FLICKERING_LIGHT_BE, FlickeringLightBE::tick);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FlickeringLightBE(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof FlickeringLightBE flickeringLightBE) {
            flickeringLightBE.onUse(player);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }
}
