package net.fawnoculus.vanillaBackrooms.blocks.custom;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fawnoculus.craft_attack.blocks.ModBlockEntities;
import net.fawnoculus.craft_attack.blocks.entities.BackroomsGeneratorBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class BackroomsGeneratorBlock extends BlockWithEntity implements PolymerBlock {
  public static final MapCodec<BackroomsGeneratorBlock> CODEC = createCodec(BackroomsGeneratorBlock::new);

  public BackroomsGeneratorBlock(Settings settings) {
    super(settings);
  }

  @Override
  public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
    return Blocks.TARGET.getDefaultState();
  }

  @Override
  protected MapCodec<? extends BlockWithEntity> getCodec() {
    return CODEC;
  }

  @Override
  public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new BackroomsGeneratorBE(pos, state);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, BlockState state, BlockEntityType<T> type) {
    if (world.isClient()) return null;
    return validateTicker(type, ModBlockEntities.BACKROOMS_GENERATOR_BE, BackroomsGeneratorBE::tick);
  }
}
