package mariculture.fishery;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import mariculture.Mariculture;
import mariculture.api.core.Environment.Salinity;
import mariculture.api.core.IUpgradable;
import mariculture.api.core.MaricultureHandlers;
import mariculture.api.fishery.Fishing;
import mariculture.api.fishery.IFishHelper;
import mariculture.api.fishery.IIncubator;
import mariculture.api.fishery.IMutation.Mutation;
import mariculture.api.fishery.fish.FishDNABase;
import mariculture.api.fishery.fish.FishSpecies;
import mariculture.core.config.FishMechanics.FussyFish;
import mariculture.core.handlers.LogHandler;
import mariculture.fishery.items.ItemEgg;
import mariculture.fishery.items.ItemFishy;
import mariculture.lib.helpers.AverageHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

public class FishyHelper implements IFishHelper {
    public static int MALE = 0;
    public static int FEMALE = 1;
    private static final String CATEGORY = "Fish-Mappings";
    private static final String COMMENT = "Mariculture Fish all have IDs, this is required mostly for when they are dead. Fish IDs are assigned automatically by Mariculture, so you can just ignore this file, it's mostly being used to save them";

    private static class CachedFishData {
        private String modid;
        private FishSpecies species;
        private int default_id;

        public CachedFishData(String modid, FishSpecies species, int default_id) {
            this.modid = modid;
            this.species = species;
            this.default_id = default_id;
        }
    }

    @Override
    public FishSpecies registerFish(String modid, Class<? extends FishSpecies> fish) {
        return registerFish(modid, fish, 50);
    }

    private static class FishSort {
        public FishSpecies species;
        public int default_id;
        
        public FishSort(FishSpecies species, int default_id) {
            this.species = species;
            this.default_id = default_id;
        }
    }
    
    private List<FishSort> toSort = new ArrayList();

    @Override
    public FishSpecies registerFish(String modid, Class<? extends FishSpecies> fish, int default_id) {
        try {
            FishSpecies species = ((FishSpecies) fish.newInstance()).setup(modid);
            /** Mark this fish to be sorted later **/
            toSort.add(new FishSort(species, default_id));
            return species;
        } catch (Exception e) {
            LogHandler.log(Level.WARN, "Mariculture failed to add the fish: " + fish + " , The fish returned null, you will have serious problems with fish!!!");
            return null;
        }
    }
    
    public boolean contains (int value, Set<Integer> array) {
        for (Integer check: array) {
            if (check == value) return true;
        }
        
        return false;
    }
    
    @Override
    public void registerFishies() {
        //First step we need go through and grab all the 'stored' ids
        //Aka the ones that are in the config file
        Map<FishSpecies, Integer> speciesToStoredInteger = new HashMap();
        for (FishSort fish: toSort) {
            Configuration config = new Configuration(new File(Mariculture.root + "/mariculture/", "fish-mappings.cfg"));
            config.load();
            config.addCustomCategoryComment(CATEGORY, COMMENT);
            int id = config.get(CATEGORY, fish.species.modid + ":" + fish.species.getSimpleName(), fish.default_id).getInt();
            speciesToStoredInteger.put(fish.species, id);
        }
        
        Set<Integer> used_ids = new HashSet();
        Set<FishSort> mappings = new HashSet();
        
        for (FishSort fish: toSort) {
            int attemptID = speciesToStoredInteger.get(fish.species);
            if (contains(attemptID, used_ids)) {
                //The ID was found in the mappings, so we need to use a different one
                for (int i = 0; i < 32000; i++) {
                    if (!contains(i, used_ids)) { //If the id wasn't found, this is the fish's new id
                        mappings.add(new FishSort(fish.species, i));
                        used_ids.add(i);
                        break;
                    }
                }
            } else {
                //This id was not found in used_ids already, so we can use it
                mappings.add(new FishSort(fish.species, attemptID));
                used_ids.add(attemptID);
            }
        }
        
        //Now that we have the fish mapped with all their ids let's actually add them properly
        for (FishSort fish: mappings) {
            FishSpecies.ids.put(fish.species.getSpecies(), fish.default_id);
            FishSpecies.species.put(fish.default_id, fish.species);
            Configuration config = new Configuration(new File(Mariculture.root + "/mariculture/", "fish-mappings.cfg"));
            config.load();
            config.addCustomCategoryComment(CATEGORY, COMMENT);
            config.get(CATEGORY, fish.species.modid + ":" + fish.species.getSimpleName(), fish.default_id).getInt();
            config.save();
        }
    }
    
    @Override
    public ItemStack makePureFish(FishSpecies species) {
        ItemStack fishStack = new ItemStack(Fishery.fishy);
        if (!fishStack.hasTagCompound()) {
            fishStack.setTagCompound(new NBTTagCompound());
        }

        for (int i = 0; i < FishDNABase.DNAParts.size(); i++) {
            FishDNABase.DNAParts.get(i).addDNA(fishStack, FishDNABase.DNAParts.get(i).getDNAFromSpecies(species));
            FishDNABase.DNAParts.get(i).addLowerDNA(fishStack, FishDNABase.DNAParts.get(i).getDNAFromSpecies(species));
        }

        return fishStack;
    }

    @Override
    public ItemStack makeBredFish(ItemStack egg, Random rand, double modifier) {
        ItemStack fish = new ItemStack(Fishery.fishy);
        for (int i = 0; i < FishDNABase.DNAParts.size(); i++)
            if (!FishDNABase.DNAParts.get(i).hasEggData(egg)) return null;

        for (int i = 0; i < FishDNABase.DNAParts.size(); i++) {
            int[] DNAlist = FishDNABase.DNAParts.get(i).getDNAList(egg);

            int parent1DNA = DNAlist[rand.nextInt(2)];
            int parent2DNA = DNAlist[rand.nextInt(2) + 2];

            int[] babyDNA = FishDNABase.DNAParts.get(i).attemptMutation(parent1DNA, parent2DNA);

            FishDNABase.DNAParts.get(i).addDNA(fish, babyDNA[0]);
            FishDNABase.DNAParts.get(i).addLowerDNA(fish, babyDNA[1]);
        }

        /* Mutate the fish species */
        int species1 = Fish.species.getDNA(fish);
        int species2 = Fish.species.getLowerDNA(fish);

        ArrayList<Mutation> mutations = Fishing.mutation.getMutations(FishSpecies.species.get(species1), FishSpecies.species.get(species2));
        if (species1 != species2 && mutations != null && mutations.size() > 0) {
            for (Mutation mute : mutations) {
                if (mute.requirement.canMutationOccur(egg)) {
                    FishSpecies baby = Fishing.fishHelper.getSpecies(mute.baby);
                    if (baby != null) {
                        if (rand.nextInt(1000) < mute.chance * 10 * modifier) {
                            for (int i = 0; i < FishDNABase.DNAParts.size(); i++) {
                                FishDNABase.DNAParts.get(i).addDNA(fish, FishDNABase.DNAParts.get(i).getDNAFromSpecies(baby));
                            }
                        }

                        //Second attempt for a mutation
                        if (rand.nextInt(1000) < mute.chance * 10 * modifier) {
                            for (int i = 0; i < FishDNABase.DNAParts.size(); i++) {
                                FishDNABase.DNAParts.get(i).addLowerDNA(fish, FishDNABase.DNAParts.get(i).getDNAFromSpecies(baby));
                            }
                        }
                    }
                }
            }
        }

        /* Reorder the fish's DNA to be sorted by dominance */
        for (int i = 0; i < FishDNABase.DNAParts.size(); i++) {
            int dna1 = FishDNABase.DNAParts.get(i).getDNA(fish);
            int dna2 = FishDNABase.DNAParts.get(i).getLowerDNA(fish);

            int[] dominance = FishDNABase.DNAParts.get(i).getDominant(dna1, dna2, rand);

            FishDNABase.DNAParts.get(i).addDNA(fish, dominance[0]);
            FishDNABase.DNAParts.get(i).addLowerDNA(fish, dominance[1]);
        }

        Fish.gender.addDNA(fish, rand.nextInt(2));

        return fish;
    }
    
    @Override
    public boolean canLive(World world, int x, int y, int z, ItemStack stack, boolean isDaytime) {
        if (FussyFish.IGNORE_ALL_REQUIREMENTS) {
            return true;
        }

        FishSpecies fish = Fishing.fishHelper.getSpecies(stack);
        if (fish == null) return false;
        else {
            Salinity salt = MaricultureHandlers.environment.getSalinity(world, x, z);
            int temperature = MaricultureHandlers.environment.getTemperature(world, x, y, z);
            boolean worldCorrect = fish.isValidDimensionForWork(world);
            boolean hasAquascum = false;
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile != null && tile instanceof IUpgradable) {
                IUpgradable upgradable = (IUpgradable) tile;
                temperature += MaricultureHandlers.upgrades.getData("temp", upgradable);
                int salinity = salt.ordinal() + MaricultureHandlers.upgrades.getData("salinity", upgradable);
                if (salinity <= 0) {
                    salinity = 0;
                }

                if (salinity > 2) {
                    salinity = 2;
                }

                salt = Salinity.values()[salinity];
                if (!worldCorrect) {
                    worldCorrect = MaricultureHandlers.upgrades.hasUpgrade("ethereal", upgradable);
                }

                hasAquascum = MaricultureHandlers.upgrades.hasUpgrade("aquascum", upgradable);
            }

            int salinityTolerance = fish.ignoresSalinity() ? 2 : Fish.salinity.getDNA(stack);
            if ((!FussyFish.IGNORE_DIMENSION_REQUIREMENTS && !worldCorrect) || (!FussyFish.IGNORE_DAY_REQUIREMENTS && !fish.canWorkAtThisTime(isDaytime))) return false;
            else {
                if (hasAquascum) {
                    salt = fish.getSalinityBase();
                    temperature = fish.getTemperatureBase();
                }
                
                return MaricultureHandlers.environment.matches(salt, temperature, fish.getSalinityBase(), salinityTolerance, fish.getTemperatureBase(), Fish.temperature.getDNA(stack));
            }
        }
    }

    @Override
    public boolean canLive(World world, int x, int y, int z, ItemStack stack) {
        return canLive(world, x, y, z, stack, world.isDaytime());
    }

    @Override
    public boolean isPure(ItemStack stack) {
        FishSpecies active = FishSpecies.species.get(Fish.species.getDNA(stack));
        FishSpecies inactive = FishSpecies.species.get(Fish.species.getLowerDNA(stack));
        if (stack.hasTagCompound()) if (stack.stackTagCompound.getInteger("SpeciesID") == stack.stackTagCompound.getInteger("lowerSpeciesID")) return true;

        return false;
    }

    @Override
    public boolean isMale(ItemStack stack) {
        return getSpecies(stack) != null && Fish.gender.getDNA(stack) == FishyHelper.MALE;
    }

    @Override
    public boolean isFemale(ItemStack stack) {
        return getSpecies(stack) != null && Fish.gender.getDNA(stack) == FishyHelper.FEMALE;
    }

    @Override
    public boolean isEgg(ItemStack stack) {
        return stack.getItem() instanceof ItemEgg && stack.hasTagCompound();
    }

    @Override
    public ItemStack generateEgg(ItemStack fish1, ItemStack fish2) {
        ItemStack egg = new ItemStack(Fishery.fishEggs);
        egg.setTagCompound(new NBTTagCompound());

        for (int i = 0; i < FishDNABase.DNAParts.size(); i++) {
            int[] DNAlist = new int[4];
            DNAlist[0] = FishDNABase.DNAParts.get(i).getDNA(fish1);
            DNAlist[1] = FishDNABase.DNAParts.get(i).getLowerDNA(fish1);
            DNAlist[2] = FishDNABase.DNAParts.get(i).getDNA(fish2);
            DNAlist[3] = FishDNABase.DNAParts.get(i).getLowerDNA(fish2);
            FishDNABase.DNAParts.get(i).addDNAList(egg, DNAlist);
        }

        int[] fertility = egg.stackTagCompound.getIntArray(Fish.fertility.getEggString());
        int eggLife = AverageHelper.getMode(fertility);
        egg.stackTagCompound.setInteger("currentFertility", eggLife);
        egg.stackTagCompound.setInteger("malesGenerated", 0);
        egg.stackTagCompound.setInteger("femalesGenerated", 0);
        return egg;
    }

    @Override
    public ItemStack attemptToHatchEgg(ItemStack egg, Random rand, double mutation, IIncubator tile) {
        int[] fertility = egg.stackTagCompound.getIntArray(Fish.fertility.getEggString());
        int[] lifes = egg.stackTagCompound.getIntArray(Fish.lifespan.getEggString());

        if (egg.getTagCompound().hasKey("SpeciesList")) {
            int birthChance = 1 + tile.getBirthChanceBoost();
            egg.getTagCompound().setInteger("currentFertility", egg.getTagCompound().getInteger("currentFertility") - 1);
            if (rand.nextInt(1000) < birthChance) {
                ItemStack fish = Fishing.fishHelper.makeBredFish(egg, rand, mutation);
                if (fish != null) {
                    //Check it again, as the fish may have been adjusted to be null
                    if (fish != null) {
                        int gender = Fish.gender.getDNA(fish);
                        tile.eject(fish);
                        if (gender == FishyHelper.MALE) {
                            egg.getTagCompound().setInteger("malesGenerated", egg.getTagCompound().getInteger("malesGenerated") + 1);
                        } else if (gender == FishyHelper.FEMALE) {
                            egg.getTagCompound().setInteger("femalesGenerated", egg.getTagCompound().getInteger("femalesGenerated") + 1);
                        }
                    }
                } else {
                    tile.eject(new ItemStack(Items.fish, 2, 0));
                }
            }

            if (egg.getTagCompound().getInteger("currentFertility") == 0) {
                ItemStack fish = Fishing.fishHelper.makeBredFish(egg, rand, mutation);
                if (fish != null) {
                    // If no males were generated create one
                    if (egg.getTagCompound().getInteger("malesGenerated") <= 0) {
                        tile.eject(Fish.gender.addDNA(fish.copy(), FishyHelper.MALE));
                    }

                    fish = Fishing.fishHelper.makeBredFish(egg, rand, mutation);
                    if (fish != null) // If no females were generated create one
                    if (egg.getTagCompound().getInteger("femalesGenerated") <= 0) {
                        tile.eject(Fish.gender.addDNA(fish.copy(), FishyHelper.FEMALE));
                    }
                } else {
                    tile.eject(new ItemStack(Items.fish));
                }

                return null;
            }
        }

        return egg;
    }

    @Override
    public FishSpecies getSpecies(ItemStack stack) {
        if (stack == null || stack.getItem() == null || !stack.hasTagCompound() || !(stack.getItem() instanceof ItemFishy)) return null;
        return getSpecies(Fish.species.getDNA(stack));
    }

    @Override
    public FishSpecies getSpecies(String species) {
        try {
            return getSpecies(FishSpecies.ids.get(species));
        } catch (Exception e) {
            LogHandler.log(Level.INFO, "Failed to find fish species: " + species);
            return null;
        }
    }

    @Override
    public FishSpecies getSpecies(int id) {
        return FishSpecies.species.get(id);
    }

    @Override
    public Integer getDNA(String str, ItemStack stack) {
        for (FishDNABase dna : FishDNABase.DNAParts)
            if (str.equals(dna.getName())) return dna.getDNA(stack);

        return -1;
    }

    @Override
    public Integer getLowerDNA(String str, ItemStack stack) {
        for (FishDNABase dna : FishDNABase.DNAParts)
            if (str.equals(dna.getName())) return dna.getLowerDNA(stack);

        return -1;
    }
}
