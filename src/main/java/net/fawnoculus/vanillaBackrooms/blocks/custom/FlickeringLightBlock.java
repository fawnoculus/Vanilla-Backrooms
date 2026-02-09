package net.fawnoculus.vanillaBackrooms.blocks.custom;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fawnoculus.craft_attack.blocks.ModBlockEntities;
import net.fawnoculus.craft_attack.blocks.entities.FlickeringLightBE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class FlickeringLightBlock extends BlockWithEntity implements PolymerBlock {
  public static final MapCodec<FlickeringLightBlock> CODEC = createCodec(FlickeringLightBlock::new);

  public FlickeringLightBlock(Settings settings) {
    super(settings);

    setDefaultState(this.getDefaultState()
      .with(Properties.LIT, false)
    );
  }

  @Override
  public MapCodec<? extends BlockWithEntity> getCodec() {
    return CODEC;
  }

  @Override
  public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
    return Blocks.REDSTONE_LAMP.getDefaultState().with(Properties.LIT, state.get(Properties.LIT, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(Properties.LIT);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, BlockState state, BlockEntityType<T> type) {
    if (world.isClient()) return null;
    return validateTicker(type, ModBlockEntities.FLICKERING_LIGHT_BE, FlickeringLightBE::tick);
  }

  @Override
  public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new FlickeringLightBE(pos, state);
  }

  @Override
  protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    if(world.getBlockEntity(pos) instanceof FlickeringLightBE flickeringLightBE){
      flickeringLightBE.onUse(player);
      return ActionResult.SUCCESS;
    }
    return super.onUse(state, world, pos, player, hit);
  }
}
