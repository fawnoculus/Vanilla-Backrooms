package net.fawnoculus.vanillaBackrooms.util;

import net.fawnoculus.craft_attack.items.ModEnchantments;
import net.fawnoculus.craft_attack.misc.tags.ModBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class PlayerUtil {
  private static final String PLAYER_DATA_EXTENSION = "custom_data";
  private final static HashMap<UUID, NbtCompound> DATA_CACHE = new HashMap<>();

  @SuppressWarnings("deprecation")
  public static void onTryBreakBlock(ServerPlayerEntity player, ServerWorld world, BlockPos pos){
    BlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    ItemStack stack = player.getMainHandStack();
    int veinMinerLevel = stack.getEnchantments().getLevel(EnchantmentUtil.getEnchantmentEntry(world, ModEnchantments.VEIN_MINER));

    List<BlockPos> blocksToBreak = new ArrayList<>(1);
    blocksToBreak.add(pos);

    if(veinMinerLevel >= 1 && !player.isSneaking() && isCorrectForDrops(stack, state) && !block.getRegistryEntry().isIn(ModBlockTags.VEIN_MINER_EXCLUDE)) {
      try {
        scanNeighbours(world, block, pos, pos, blocksToBreak, veinMinerLevel);
      }catch (StackOverflowError ignored){
      }
    }

    int autoSmeltLevel = stack.getEnchantments().getLevel(EnchantmentUtil.getEnchantmentEntry(world, ModEnchantments.AUTO_SMELT));

    if(autoSmeltLevel >= 1 && !player.shouldSkipBlockDrops()){
      autoSmeltBlocks(blocksToBreak, world, stack, player);
    }else {
      for (BlockPos breakingPos : blocksToBreak){
        WorldUtil.removeBlock(world, breakingPos, player, !player.shouldSkipBlockDrops());
      }
    }
  }

  private static void autoSmeltBlocks(List<BlockPos> blocksToBreak, ServerWorld world, ItemStack stack, ServerPlayerEntity player) {
    for (BlockPos breakingPos : blocksToBreak) {
      BlockState stateToBreak = world.getBlockState(breakingPos);

      if(!isCorrectForDrops(stack, stateToBreak)){
        break;
      }

      LootWorldContext.Builder builder = new LootWorldContext.Builder(world)
        .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(breakingPos))
        .add(LootContextParameters.BLOCK_STATE, stateToBreak)
        .addOptional(LootContextParameters.BLOCK_ENTITY, world.getBlockEntity(breakingPos))
        .addOptional(LootContextParameters.THIS_ENTITY, player)
        .add(LootContextParameters.TOOL, stack);

      List<ItemStack> list = stateToBreak.getDroppedStacks(builder);
      for (ItemStack checkedStack : list) {
        SingleStackRecipeInput recipeInput = new SingleStackRecipeInput(checkedStack);

        Optional<RecipeEntry<SmeltingRecipe>> optional = ServerRecipeManager.createCachedMatchGetter(RecipeType.SMELTING).getFirstMatch(recipeInput, world);
        if (optional.isEmpty()) {
          continue;
        }

        RecipeEntry<SmeltingRecipe> recipeEntry = optional.get();

        ItemStack output = recipeEntry.value().craft(recipeInput, world.getRegistryManager());
        output.setCount(checkedStack.getCount());

        ItemScatterer.spawn(world, breakingPos.getX(), breakingPos.getY(), breakingPos.getZ(), output);

        WorldUtil.removeBlock(world, breakingPos, player, false);
      }
    }
  }

  private static boolean isCorrectForDrops(ItemStack stack, BlockState state) {
    ToolComponent toolComponent = stack.get(DataComponentTypes.TOOL);
    return toolComponent != null && toolComponent.isCorrectForDrops(state);
  }

  private static void scanNeighbours(World world, Block compareBlock, BlockPos originPos, BlockPos scanningPos, List<BlockPos> scannedBlocks, int level) {
    scanBlock(world, compareBlock, originPos, scanningPos.up(), scannedBlocks, level);
    scanBlock(world, compareBlock, originPos, scanningPos.down(), scannedBlocks, level);
    scanBlock(world, compareBlock, originPos, scanningPos.north(), scannedBlocks, level);
    scanBlock(world, compareBlock, originPos, scanningPos.east(), scannedBlocks, level);
    scanBlock(world, compareBlock, originPos, scanningPos.south(), scannedBlocks, level);
    scanBlock(world, compareBlock, originPos, scanningPos.west(), scannedBlocks, level);
  }

  private static void scanBlock(World world, Block compareBlock, BlockPos originPos, BlockPos scanningPos, List<BlockPos> scannedBlocks, int level) {
    if (scannedBlocks.contains(scanningPos)) return;
    if (world.getBlockState(scanningPos).getBlock() != compareBlock) return;
    if (!originPos.isWithinDistance(scanningPos, level + 1)) return;
    scannedBlocks.add(scanningPos);
    scanNeighbours(world, compareBlock, originPos, scanningPos, scannedBlocks,  level);
  }

  public static void setPermanentCustomData(@NotNull ServerPlayerEntity player, NbtCompound nbt) {
    MinecraftServer server = player.getServer();
    assert server != null;

    Path playerData = server.getPath("data")
      .resolve("ca")
      .resolve("player_data")
      .resolve(PLAYER_DATA_EXTENSION)
      .resolve(player.getUuidAsString() + ".dat");

    if(nbt == null || nbt.isEmpty()){
      if (playerData.toFile().exists()) {
        boolean ignored = playerData.toFile().delete();
      }

      DATA_CACHE.remove(player.getUuid());
      return;
    }

    try {
      boolean ignored = playerData.getParent().toFile().mkdirs();
      if (playerData.toFile().exists()) {
        boolean ignored2 = playerData.toFile().delete();
      }
      boolean ignored3 = playerData.toFile().createNewFile();

      NbtIo.write(nbt, playerData);
      DATA_CACHE.put(player.getUuid(), nbt);
    } catch (IOException ignored) {
    }
  }


  public static NbtCompound getPermanentCustomData(@NotNull ServerPlayerEntity player) {
    if(DATA_CACHE.containsKey(player.getUuid())) {
      return DATA_CACHE.get(player.getUuid());
    }

    MinecraftServer server = player.getServer();
    assert server != null;

    Path playerData = server.getPath("data")
      .resolve("ca")
      .resolve("player_data")
      .resolve(PLAYER_DATA_EXTENSION)
      .resolve(player.getUuidAsString() + ".dat");

    NbtCompound nbt = new NbtCompound();
    try {
      nbt = Objects.requireNonNull(NbtIo.read(playerData));
    } catch (Exception ignored) {
    }

    DATA_CACHE.put(player.getUuid(), nbt);

    return nbt;
  }
}
