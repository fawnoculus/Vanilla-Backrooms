package net.fawnoculus.vanilla_backrooms.client.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fawnoculus.vanilla_backrooms.blocks.ModBlocks;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {
    public BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.ACTIVE_LIGHT);
        dropSelf(ModBlocks.FLICKERING_LIGHT);
        dropSelf(ModBlocks.FAKE_SKY);
    }
}
