package net.fawnoculus.vanillaBackrooms.client.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
		  .add(ModBlocks.ACTIVE_LIGHT)
		  .add(ModBlocks.FLICKERING_LIGHT)
		  .add(ModBlocks.FAKE_SKY);
	}
}
