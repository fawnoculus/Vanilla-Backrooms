package net.fawnoculus.vanillaBackrooms.blocks.entities;

import net.fawnoculus.craft_attack.CraftAttack;
import net.fawnoculus.craft_attack.blocks.ModBlockEntities;
import net.fawnoculus.craft_attack.blocks.ModBlocks;
import net.fawnoculus.craft_attack.util.MixinUtil;
import net.fawnoculus.craft_attack.util.event.BackroomsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BackroomsGeneratorBE extends BlockEntity {
  private static final RegistryKey<StructurePool> RING_9_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring9"));
  private static final RegistryKey<StructurePool> RING_8_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring8"));
  private static final RegistryKey<StructurePool> RING_7_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring7"));
  private static final RegistryKey<StructurePool> RING_6_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring6"));
  private static final RegistryKey<StructurePool> RING_5_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring5"));
  private static final RegistryKey<StructurePool> RING_4_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring4"));
  private static final RegistryKey<StructurePool> RING_3_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring3"));
  private static final RegistryKey<StructurePool> RING_2_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring2"));
  private static final RegistryKey<StructurePool> RING_1_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring1"));
  private static final RegistryKey<StructurePool> RING_0_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/ring0"));
  private static final RegistryKey<StructurePool> SPAWN_ID = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, CraftAttack.id("backrooms/spawn"));
  private static final Identifier STRUCTURE_START = CraftAttack.id("backrooms_start");
  private static final double RING_9_DISTANCE = 900 * 900;
  private static final double RING_8_DISTANCE = 800 * 800;
  private static final double RING_7_DISTANCE = 700 * 700;
  private static final double RING_6_DISTANCE = 600 * 600;
  private static final double RING_5_DISTANCE = 500 * 500;
  private static final double RING_4_DISTANCE = 400 * 400;
  private static final double RING_3_DISTANCE = 300 * 300;
  private static final double RING_2_DISTANCE = 200 * 200;
  private static final double RING_1_DISTANCE = 100 * 100;
  private static final int HORIZONTAL_OFFSET = 48;
  private static final int Y_LEVEL = 0;

  public BackroomsGeneratorBE(BlockPos pos, BlockState state) {
    super(ModBlockEntities.BACKROOMS_GENERATOR_BE, pos, state);
  }

  public static void tick(World world, BlockPos pos, BlockState ignored, BackroomsGeneratorBE ignored2) {
    if (world.getRegistryKey().equals(BackroomsUtil.BACKROOMS_WORLD)
      && world instanceof ServerWorld serverWorld
      && pos.getY() == Y_LEVEL
    ) {
      tryPlaceNeighbours(serverWorld, pos);
    }
  }

  private static void tryPlaceNeighbours(ServerWorld world, BlockPos pos) {
    if (world.getBlockState(pos.add(0, 0, HORIZONTAL_OFFSET)).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
      placeBackroomsSegment(world, pos.add(0, 0, HORIZONTAL_OFFSET));
    }
    if (world.getBlockState(pos.add(HORIZONTAL_OFFSET, 0, 0)).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
      placeBackroomsSegment(world, pos.add(HORIZONTAL_OFFSET, 0, 0));
    }
    if (world.getBlockState(pos.add(0, 0, -HORIZONTAL_OFFSET)).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
      placeBackroomsSegment(world, pos.add(0, 0, -HORIZONTAL_OFFSET));
    }
    if (world.getBlockState(pos.add(-HORIZONTAL_OFFSET, 0, 0)).getBlock() != ModBlocks.BACKROOMS_GENERATOR) {
      placeBackroomsSegment(world, pos.add(-HORIZONTAL_OFFSET, 0, 0));
    }
  }

  public static void placeBackroomsSegment(ServerWorld world, BlockPos pos) {
    placeSegmentWithRotation(world, pos, BlockRotation.random(world.getRandom()));
  }

  private static void placeSegmentWithRotation(ServerWorld world, BlockPos pos, BlockRotation rotation) {
    int xOffset = 0;
    int zOffset = 0;

    switch (rotation) {
      case NONE -> {}
      case CLOCKWISE_90 -> xOffset = HORIZONTAL_OFFSET - 1;
      case COUNTERCLOCKWISE_90 -> zOffset = HORIZONTAL_OFFSET - 1;
      case CLOCKWISE_180 -> {
        xOffset = HORIZONTAL_OFFSET - 1;
        zOffset = HORIZONTAL_OFFSET - 1;
      }
    }

    BlockPos placementPos = pos.add(xOffset, 1, zOffset);

    MixinUtil.setRandomBlockRotationOverride(rotation);

    Registry<StructurePool> registry = world.getRegistryManager().getOrThrow(RegistryKeys.TEMPLATE_POOL);
    RegistryEntry<StructurePool> registryEntry = registry.getOrThrow(poolFromPos(pos));
    boolean success = StructurePoolBasedGenerator.generate(world, registryEntry, STRUCTURE_START, 20, placementPos, false);

    if (success) {
      world.setBlockState(pos, ModBlocks.BACKROOMS_GENERATOR.getDefaultState());
    } else {
      CraftAttack.LOGGER.warn("Failed to generate backrooms segment");
    }
  }

  private static RegistryKey<StructurePool> poolFromPos(BlockPos pos) {
    double distanceFromCenter = Math.abs((double) pos.getX() * pos.getX() + pos.getZ() * pos.getZ());

    if (distanceFromCenter == 0.0) {
      return SPAWN_ID;
    }
    if (distanceFromCenter < RING_1_DISTANCE) {
      return RING_0_ID;
    }
    if (distanceFromCenter < RING_2_DISTANCE) {
      return RING_1_ID;
    }
    if (distanceFromCenter < RING_3_DISTANCE) {
      return RING_2_ID;
    }
    if (distanceFromCenter < RING_4_DISTANCE) {
      return RING_3_ID;
    }
    if (distanceFromCenter < RING_5_DISTANCE) {
      return RING_4_ID;
    }
    if (distanceFromCenter < RING_6_DISTANCE) {
      return RING_5_ID;
    }
    if (distanceFromCenter < RING_7_DISTANCE) {
      return RING_6_ID;
    }
    if (distanceFromCenter < RING_8_DISTANCE) {
      return RING_7_ID;
    }
    if (distanceFromCenter < RING_9_DISTANCE) {
      return RING_8_ID;
    }
    return RING_9_ID;
  }
}
