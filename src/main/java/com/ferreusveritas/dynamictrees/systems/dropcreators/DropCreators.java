package com.ferreusveritas.dynamictrees.systems.dropcreators;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;

/**
 * @author Harley O'Connor
 */
public final class DropCreators {

    public static final DropCreator NORMAL = new NormalDropCreator(DynamicTrees.resLoc("normal"));
    public static final DropCreator LOOT_TABLE = new LootTableDropCreator(DynamicTrees.resLoc("loot_table"));
    public static final DropCreator SEED = new SeedDropCreator(DynamicTrees.resLoc("seed"));

    public void register(final RegistryEvent<DropCreator> event) {
        event.getRegistry().registerAll(LOOT_TABLE);
    }

}
