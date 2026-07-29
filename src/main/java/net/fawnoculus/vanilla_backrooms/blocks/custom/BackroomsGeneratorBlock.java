package net.fawnoculus.vanilla_backrooms.blocks.custom;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fawnoculus.vanilla_backrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanilla_backrooms.blocks.entities.BackroomsGeneratorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import xyz.nucleoid.packettweaker.PacketContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackroomsGeneratorBlock extends BaseEntityBlock implements PolymerBlock {
    public static final MapCodec<BackroomsGeneratorBlock> CODEC = simpleCodec(BackroomsGeneratorBlock::new);

    public BackroomsGeneratorBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return Blocks.TARGET.defaultBlockState();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BackroomsGeneratorBE(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, BlockState state, BlockEntityType<T> type) {
        if (world.isClientSide()) return null;
        return createTickerHelper(type, ModBlockEntities.BACKROOMS_GENERATOR_BE, BackroomsGeneratorBE::tick);
    }
}
