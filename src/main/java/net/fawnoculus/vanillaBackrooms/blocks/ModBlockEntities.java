package net.fawnoculus.vanillaBackrooms.blocks;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fawnoculus.craft_attack.CraftAttack;
import net.fawnoculus.craft_attack.blocks.entities.BackroomsGeneratorBE;
import net.fawnoculus.craft_attack.blocks.entities.FlickeringLightBE;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
  public static final BlockEntityType<FlickeringLightBE> FLICKERING_LIGHT_BE = register("flickering_light", FlickeringLightBE::new, ModBlocks.FLICKERING_LIGHT);
  public static final BlockEntityType<BackroomsGeneratorBE> BACKROOMS_GENERATOR_BE = register("backrooms_generator", BackroomsGeneratorBE::new, ModBlocks.BACKROOMS_GENERATOR);

  private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
    BlockEntityType<T> type = Registry.register(
      Registries.BLOCK_ENTITY_TYPE,
      CraftAttack.id(name),
      FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build()
    );
    PolymerBlockUtils.registerBlockEntity(type);
    return type;
  }

  public static void initialize() {
  }
}
