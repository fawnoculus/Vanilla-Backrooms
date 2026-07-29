package net.fawnoculus.vanilla_backrooms.blocks;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.fawnoculus.vanilla_backrooms.blocks.custom.ActiveLightBlock;
import net.fawnoculus.vanilla_backrooms.blocks.custom.BackroomsGeneratorBlock;
import net.fawnoculus.vanilla_backrooms.blocks.custom.FakeSkyBlock;
import net.fawnoculus.vanilla_backrooms.blocks.custom.FlickeringLightBlock;
import net.fawnoculus.vanilla_backrooms.blocks.custom.NoclipBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static final Block FAKE_SKY = register(
      "fake_sky",
      FakeSkyBlock::new,
      BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_CONCRETE).lightLevel(ignored -> 15)
    );
    public static final Block FLICKERING_LIGHT = register(
      "flickering_light",
      FlickeringLightBlock::new,
      BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP)
    );
    public static final Block ACTIVE_LIGHT = register(
      "active_light",
      ActiveLightBlock::new,
      BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP).lightLevel(ignored -> 15)
    );
    public static final Block NOCLIP_BLOCK = register(
      "noclip_block",
      NoclipBlock::new,
      BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK).replaceable()
    );
    public static final Block BACKROOMS_GENERATOR = register(
      "backrooms_generator",
      BackroomsGeneratorBlock::new,
      BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)
    );
    public static final Block FINISHED_BACKROOMS_GENERATOR = register(
      "finished_backrooms_generator",
      settings -> new SimplePolymerBlock(settings, Blocks.BEDROCK),
      BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)
    );

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, VanillaBackrooms.id(name));

        Block block = blockFactory.apply(settings.setId(blockKey));
        if (!(block instanceof PolymerBlock)) {
            throw new IllegalArgumentException("Block Factory must return a PolymerBlock");
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static void initialize() {
    }
}
