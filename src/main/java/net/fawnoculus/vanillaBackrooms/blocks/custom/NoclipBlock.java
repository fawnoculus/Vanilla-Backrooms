package net.fawnoculus.vanillaBackrooms.blocks.custom;

import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import net.fawnoculus.craft_attack.util.event.BackroomsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NoclipBlock extends SimplePolymerBlock {
  public NoclipBlock(Settings settings) {
    super(settings, Blocks.STRUCTURE_VOID);
  }

  @Override
  protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return VoxelShapes.empty();
  }

  @Override
  protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
    if (!(world instanceof ServerWorld serverWorld)) {
      return;
    }

    BackroomsUtil.switchDimension(serverWorld.getServer(), entity);
  }
}
