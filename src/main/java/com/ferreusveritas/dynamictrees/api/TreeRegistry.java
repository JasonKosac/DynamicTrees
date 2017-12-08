package com.ferreusveritas.dynamictrees.api;

import java.util.ArrayList;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.treedata.IBiomeSuitabilityDecider;
import com.ferreusveritas.dynamictrees.api.treedata.ISpecies;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;

/**
* A registry for all of the dynamic trees. Use this for this mod or other mods.
* 
* @author ferreusveritas
*/
public class TreeRegistry {

	public static IForgeRegistry<ISpecies> speciesRegistry;
	private static ArrayList<IBiomeSuitabilityDecider> biomeSuitabilityDeciders = new ArrayList<IBiomeSuitabilityDecider>();
	
	//////////////////////////////
	// TREE REGISTRY
	//////////////////////////////
	
	/**
	 * Mods should use this to register their {@link DynamicTree}
	 * 
	 * Places the tree in a central registry.
	 * The proper place to use this is during the preInit phase of your mod.
	 * 
	 * @param species The dynamic tree being registered
	 * @return DynamicTree for chaining
	 */
	public static ISpecies registerSpecies(ISpecies species) {
		speciesRegistry.register(species);
		return species;
	}

	public static void registerSpecies(ISpecies ... values) {
		speciesRegistry.registerAll(values);
	}
	
	public static ISpecies findSpecies(ResourceLocation name) {
		return speciesRegistry.getValue(name);
	}
	
	/**
	 * Searches first for the full tree name.  If that fails then it
	 * will find the first tree matching the simple name and return it instead otherwise null
	 * 
	 * @param name The name of the tree.  Either the simple name or the full name
	 * @return The tree that was found or null if not found
	 */
	public static ISpecies findSpeciesSloppy(String name) {
		
		//Exact find
		ResourceLocation resloc = new ResourceLocation(name);
		if(speciesRegistry.containsKey(resloc)) {
			return speciesRegistry.getValue(resloc);
		}

		//Search DynamicTrees Domain
		resloc = new ResourceLocation(ModConstants.MODID, resloc.getResourcePath());
		if(speciesRegistry.containsKey(resloc)) {
			return speciesRegistry.getValue(resloc);
		}
		
		//Search all domains
		for(ISpecies species : speciesRegistry) {
			if(species.getRegistryName().getResourcePath().equals(resloc.getResourcePath())) {
				return species;
			}
		}
		
		return null;
	}
	
	//////////////////////////////
	// BIOME HANDLING
	//////////////////////////////
	
	/**
	 * Mods should call this to register an {@link IBiomeSuitabilityDecider}
	 * 
	 * @param decider The decider being registered
	 */
	public static void registerBiomeSuitabilityDecider(IBiomeSuitabilityDecider decider) {
		biomeSuitabilityDeciders.add(decider);
	}
	
	private static final IBiomeSuitabilityDecider.Decision undecided = new IBiomeSuitabilityDecider.Decision();
	
	public static IBiomeSuitabilityDecider.Decision getBiomeSuitability(World world, Biome biome, Species species, BlockPos pos) {
		for(IBiomeSuitabilityDecider decider: biomeSuitabilityDeciders) {
			IBiomeSuitabilityDecider.Decision decision = decider.getBiomeSuitability(world, biome, species, pos);
			if(decision.isHandled()) {
				return decision;
			}
		}
		
		return undecided;
	}
	
	public static boolean isBiomeSuitabilityOverrideEnabled() {
		return !biomeSuitabilityDeciders.isEmpty();
	}
	
}
