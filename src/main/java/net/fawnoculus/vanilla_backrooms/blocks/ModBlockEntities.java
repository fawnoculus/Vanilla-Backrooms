package net.fawnoculus.vanilla_backrooms.blocks;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.fawnoculus.vanilla_backrooms.blocks.entities.BackroomsGeneratorBE;
import net.fawnoculus.vanilla_backrooms.blocks.entities.FlickeringLightBE;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
        BlockEntityType<T> type = Registry.register(
          BuiltInRegistries.BLOCK_ENTITY_TYPE,
          VanillaBackrooms.id(name),
          FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build()
        );
        PolymerBlockUtils.registerBlockEntity(type);
        return type;
    }

    public static void initialize() {
    }

    public static final BlockEntityType<FlickeringLightBE> FLICKERING_LIGHT_BE = register("flickering_light", FlickeringLightBE::new, ModBlocks.FLICKERING_LIGHT);


    public static final BlockEntityType<BackroomsGeneratorBE> BACKROOMS_GENERATOR_BE = register("backrooms_generator", BackroomsGeneratorBE::new, ModBlocks.BACKROOMS_GENERATOR);


}
