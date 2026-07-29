package net.fawnoculus.vanilla_backrooms.blocks.custom;

import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NoclipBlock extends SimplePolymerBlock {
    public NoclipBlock(Properties settings) {
        super(settings, Blocks.STRUCTURE_VOID);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        if (!(world instanceof ServerLevel serverWorld)) {
            return;
        }

        BackroomsHandler.noclip(serverWorld.getServer(), entity);
    }
}
