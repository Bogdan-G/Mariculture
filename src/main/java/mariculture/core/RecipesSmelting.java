package mariculture.core;

import static mariculture.core.lib.MCLib.blockSnow;
import static mariculture.core.lib.MCLib.dustSalt;
import static mariculture.core.lib.MCLib.glass;
import static mariculture.core.lib.MCLib.glassPane;
import static mariculture.core.lib.MCLib.ice;
import static mariculture.core.lib.MCLib.limestone;
import static mariculture.core.lib.MCLib.limestoneSmooth;
import static mariculture.core.lib.MCLib.obsidian;
import static mariculture.core.lib.MCLib.salt;
import static mariculture.core.lib.MCLib.sand;
import static mariculture.core.lib.MCLib.stone;
import static mariculture.core.util.Fluids.getFluid;
import static mariculture.core.util.Fluids.getFluidName;
import static mariculture.core.util.Fluids.getFluidStack;

import java.util.ArrayList;

import mariculture.api.core.FuelInfo;
import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.RecipeSmelter;
import mariculture.core.config.Machines.MachineSettings;
import mariculture.core.handlers.CrucibleExplosionTickHandler;
import mariculture.core.helpers.ItemHelper;
import mariculture.core.helpers.RecipeHelper;
import mariculture.core.lib.GlassMeta;
import mariculture.core.lib.MaterialsMeta;
import mariculture.core.lib.MetalRates;
import mariculture.core.lib.TransparentMeta;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipesSmelting {
    public static int iron = 1538;
    public static int gold = 1064;
    public static int tin = 232;
    public static int copper = 1085;
    public static int silver = 962;
    public static int lead = 328;
    public static int magnesium = 650;
    public static int nickel = 1455;
    public static int bronze = 950;
    public static int steel = 1370;
    public static int aluminum = 660;
    public static int titanium = 1662;
    public static int electrum = 1000;
    public static int alloy = 1725;
    public static int ardite = 2000;
    public static int cobalt = 1495;
    public static int osmium = 2000;
    public static int zinc = 420;
    public static int platinum_temp = 1768;

    public static void addRecipe(String fluidName, Object[] item, int[] volume, int temperature) {
        addRecipe(fluidName, item, volume, temperature, null, 0);
    }

    public static void addRecipe(String fluidName, Object[] item, int[] volume, int temperature, ItemStack bonus, int chance) {
        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null) return;
        for (int i = 0; i < item.length; i++) {
            Object o = item[i];
            ArrayList<ItemStack> stacks = new ArrayList();
            if (o instanceof String) {
                stacks = OreDictionary.getOres((String) o);
            } else if (o instanceof ItemStack) {
                stacks.add((ItemStack) o);
            } else if (o instanceof Block) {
                stacks.add(new ItemStack((Block) o));
            } else if (o instanceof Item) {
                stacks.add(new ItemStack((Item) o));
            }

            for (ItemStack stack : stacks)
                if (bonus == null || chance <= 0) {
                    RecipeHelper.addMelting(stack, temperature, new FluidStack(fluid, volume[i]));
                } else {
                    RecipeHelper.addMelting(stack, temperature, new FluidStack(fluid, volume[i]), bonus, chance);
                }
        }
    }

    public static void add() {
        addFuels();
        addMetalRecipes();
    }

    public static void postAdd() {
        ItemStack sulfur = fetchItem(new String[] { "dustSulfur", "crystalSulfur" });
        ItemStack salt = new ItemStack(Core.materials, 1, MaterialsMeta.DUST_SALT);
        ItemStack silicon = fetchItem(new String[] { "itemSilicon", "dustSiliconDioxide" });
        ItemStack platinum = fetchItem(new String[] { "dustPlatinum", "ingotPlatinum" });
        ItemStack iridium = Loader.isModLoaded("IC2") ? new ItemStack(GameRegistry.findItem("IC2", "itemOreIridium")): null;

        //Alumumumium Dust
        LinkedMetal[] alums = new LinkedMetal[] { new LinkedMetal("tin", 7), new LinkedMetal("iron", 5), new LinkedMetal("rutile", 35) };
        addDust(MaterialsMeta.DUST_ALUMINUM, aluminum, null, 0, alums);

        //Copperous Dust
        LinkedMetal[] coppers = new LinkedMetal[] { new LinkedMetal("iron", 4), new LinkedMetal("silver", 7),  new LinkedMetal("osmium", 30), new LinkedMetal("gold", 10), new LinkedMetal("cobalt", 15), new LinkedMetal("nickel", 8), new LinkedMetal("lead", 7), new LinkedMetal("tin", 6) };
        addDust(MaterialsMeta.DUST_COPPEROUS, copper, sulfur, 10, coppers);

        //Golden Dust
        LinkedMetal[] golds = new LinkedMetal[] { new LinkedMetal("electrum", 3), new LinkedMetal("silver", 7), new LinkedMetal("gold", 25) };
        addDust(MaterialsMeta.DUST_GOLDEN, gold, null, 0, golds);

        //Ironic Dust
        LinkedMetal[] irons = new LinkedMetal[] { new LinkedMetal("aluminum", 3), new LinkedMetal("tin", 8), new LinkedMetal("copper", 6) };
        addDust(MaterialsMeta.DUST_IRONIC, iron, silicon, 6, irons);

        //Leader Dust
        LinkedMetal[] leads = new LinkedMetal[] { new LinkedMetal("silver", 3), new LinkedMetal("iron", 6), new LinkedMetal("copper", 8), new LinkedMetal("tin", 10) };
        addDust(MaterialsMeta.DUST_LEADER, lead, null, 0, leads);

        //Silvery Dust
        LinkedMetal[] silvers = new LinkedMetal[] { new LinkedMetal("lead", 2), new LinkedMetal("electrum", 4) };
        addDust(MaterialsMeta.DUST_SILVERY, silver, sulfur, 5, silvers);

        //Tinnic Dust
        LinkedMetal[] tins = new LinkedMetal[] { new LinkedMetal("copper", 3), new LinkedMetal("iron", 6), new LinkedMetal("lead", 8) };
        addDust(MaterialsMeta.DUST_TINNIC, tin, sulfur, 7, tins);
        
        //TODO: Add Osmium, Ardite, Platinum, Cobalt, Zinc Ores

        addMetal(getFluidName("tin"), "Tin", tin, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_TINNIC), 10);
        addMetal(getFluidName("copper"), "Copper", copper, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_COPPEROUS), 10);
        addMetal(getFluidName("silver"), "Silver", silver, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_SILVERY), 10);
        addMetal(getFluidName("lead"), "Lead", lead, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_LEADER), 10);
        addMetal(getFluidName("nickel"), "Nickel", nickel, platinum, 10);
        addMetal(getFluidName("bronze"), "Bronze", bronze, null, 0);
        addMetal(getFluidName("steel"), "Steel", steel, null, 0);
        addMetal(getFluidName("electrum"), "Electrum", electrum, null, 0);
        addMetal(getFluidName("ardite"), "Ardite", ardite, new ItemStack(Blocks.netherrack), 2);
        addMetal(getFluidName("cobalt"), "Cobalt", cobalt, sulfur, 3);
        addMetal(getFluidName("platinum"), "Platinum", platinum_temp, sulfur, 5);
        addMetal(getFluidName("osmium"), "Osmium", osmium, iridium, 200);
        addMetal(getFluidName("zinc"), "Zinc", zinc, sulfur, 5);

        //Gold + Silver = Electrum
        if (OreDictionary.getOres("ingotElectrum").size() > 0 && OreDictionary.getOres("ingotSilver").size() > 0) {
            FluidStack moltenSilver = getFluidStack("silver", MetalRates.NUGGET);
            FluidStack moltenGold = getFluidStack("gold", MetalRates.NUGGET);
            FluidStack moltenElectrum = getFluidStack("electrum", MetalRates.NUGGET * 2);
            RecipeHelper.addFluidAlloy(moltenSilver, moltenGold, moltenElectrum, 1);
        }
    }

    private static class LinkedMetal {
        public String metal;
        public Integer chance;

        public LinkedMetal(String metal, Integer chance) {
            this.metal = metal;
            this.chance = chance;
        }
    }

    private static void addDust(int meta, int temp, ItemStack bonus, int chance, LinkedMetal[] metals) {
        ArrayList<FluidStack> fluids = new ArrayList<FluidStack>();
        ArrayList<Integer> chances = new ArrayList<Integer>();

        for (LinkedMetal metal : metals) {
            if (getFluid(metal.metal) != null) {
                String name = "ingot" + WordUtils.capitalize(metal.metal);
                if (OreDictionary.getOres(name).size() > 0) {
                    fluids.add(getFluidStack(metal.metal, MetalRates.INGOT));
                    chances.add(metal.chance);
                }
            }
        }

        if (fluids.size() > 0) {
            MaricultureHandlers.crucible.addRecipe(new RecipeSmelter(new ItemStack(Core.materials, 1, meta), temp, fluids.toArray(new FluidStack[fluids.size()]), chances.toArray(new Integer[chances.size()]), bonus, chance));
        }
    }

    private static ItemStack fetchItem(String[] array) {
        for (String arr : array)
            if (OreDictionary.getOres(arr).size() > 0) return OreDictionary.getOres(arr).get(0);

        return null;
    }

    private static void addFuels() {
        RecipeHelper.addFuel("blockCoal", new FuelInfo(2000, 1680, 10800));
        RecipeHelper.addFuel(new ItemStack(Items.coal, 1, 0), new FuelInfo(2000, 168, 1200));
        RecipeHelper.addFuel(new ItemStack(Items.coal, 1, 1), new FuelInfo(1600, 128, 900));
        RecipeHelper.addFuel("logWood", new FuelInfo(480, 48, 300));
        RecipeHelper.addFuel("plankWood", new FuelInfo(320, 32, 200));
        RecipeHelper.addFuel("stickWood", new FuelInfo(160, 16, 100));
        RecipeHelper.addFuel(getFluidName("natural_gas"), new FuelInfo(2000, 35, 1200));
        RecipeHelper.addFuel("gascraft_naturalgas", new FuelInfo(2000, 35, 1000));
        RecipeHelper.addFuel("fuel", new FuelInfo(2000, 35, 1000));
        RecipeHelper.addFuel("pyrotheum", new FuelInfo(2000, 100, 100));
        RecipeHelper.addFuel("coal", new FuelInfo(2000, 168, 300));
        RecipeHelper.addFuel("biofuel", new FuelInfo(1800, 20, 2000));
        RecipeHelper.addFuel("oil", new FuelInfo(1750, 20, 400));
        RecipeHelper.addFuel("lava", new FuelInfo(1500, 20, 360));
        RecipeHelper.addFuel("biomass", new FuelInfo(1500, 10, 1800));
        RecipeHelper.addFuel("bioethanol", new FuelInfo(1500, 10, 1800));
        //Ender IO
        RecipeHelper.addFuel("hootch", new FuelInfo(1800, 35, 250));
        RecipeHelper.addFuel("fire_water", new FuelInfo(1900, 40, 500));
        RecipeHelper.addFuel("rocket_fuel", new FuelInfo(2000, 45, 750));
        
        //Gunpowder and TNT
        RecipeHelper.addTickHandlingFuel(new ItemStack(Items.gunpowder), new FuelInfo(1600, 40, 50), new CrucibleExplosionTickHandler(1F, 32));
        RecipeHelper.addTickHandlingFuel(new ItemStack(Blocks.tnt), new FuelInfo(2000, 250, 250), new CrucibleExplosionTickHandler(4F, 16));
    }

    public static void addFullSet(String fluid, Object[] items, int temp, ItemStack output, int chance) {
        addRecipe(fluid, new Object[] { items[0] }, MetalRates.ORES, temp, output, chance);
        addRecipe(fluid, new Object[] { items[1], items[2], items[3], items[4] }, MetalRates.METALS, temp);
        addRecipe(fluid, new Object[] { items[5], items[6], items[7], items[8], items[9] }, MetalRates.TOOLS, temp, new ItemStack(Items.stick), 1);
        addRecipe(fluid, new Object[] { items[10], items[11], items[12], items[13] }, MetalRates.ARMOR, temp);
    }

    public static void addMetal(String fluid, String metal, int temp, ItemStack bonus, int chance) {
    	if (!MachineSettings.CRUCIBLE_ENABLE_DUSTS) {
    		bonus = null;
    		chance = 0;
    	}
    	
        addRecipe(fluid, new Object[] { "ore" + metal }, MetalRates.ORES, temp, bonus, chance);
        addRecipe(fluid, new Object[] { "nugget" + metal, "ingot" + metal, "block" + metal, "dust" + metal }, MetalRates.METALS, temp);

        if (OreDictionary.getOres("ingot" + metal).size() > 0) {
            RecipeHelper.addMetalCasting(metal);
        }
    }

    public static void addMetalRecipes() {
        addFullSet(getFluidName("iron"), new Object[] { "oreIron", "nuggetIron", "ingotIron", "blockIron", "dustIron", Items.iron_pickaxe, Items.iron_shovel, Items.iron_axe, Items.iron_sword, Items.iron_hoe, Items.iron_helmet, Items.iron_chestplate, Items.iron_leggings, Items.iron_boots }, iron, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_IRONIC), 10);
        RecipeHelper.addMetalCasting("Iron");

        addFullSet(getFluidName("gold"), new Object[] { "oreGold", "nuggetGold", "ingotGold", "blockGold", "dustGold", Items.golden_pickaxe, Items.golden_shovel, Items.golden_axe, Items.golden_sword, Items.golden_hoe, Items.golden_helmet, Items.golden_chestplate, Items.golden_leggings, Items.golden_boots }, gold, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_GOLDEN), 10);
        RecipeHelper.addMetalCasting("Gold");

        addMetal(getFluidName("aluminum"), "Aluminum", aluminum, new ItemStack(Core.materials, 1, MaterialsMeta.DUST_ALUMINUM), 10);
        addMetal(getFluidName("rutile"), "Rutile", titanium, limestone, 2);
        addMetal(getFluidName("titanium"), "Titanium", titanium, limestone, 2);
        addMetal(getFluidName("magnesium"), "Magnesium", magnesium, new ItemStack(stone), 2);

        FluidStack moltenRutile = getFluidStack("rutile", MetalRates.INGOT);
        FluidStack moltenMagnesium = getFluidStack("magnesium", MetalRates.INGOT);
        FluidStack moltenTitanium = getFluidStack("titanium", MetalRates.INGOT);
        RecipeHelper.addFluidAlloy(moltenRutile, moltenMagnesium, moltenTitanium, 1);
        
        FluidStack quicklime = getFluidStack("quicklime", 3000);
        FluidStack water = getFluidStack("water", 1000);
        FluidStack magnesium = getFluidStack("magnesium", MetalRates.INGOT);
        RecipeHelper.addFluidAlloy(quicklime, water, magnesium, 5);

        //Gold Back
        RecipeHelper.addMelting(new ItemStack(Blocks.light_weighted_pressure_plate), gold, gold(MetalRates.INGOT * 2));
        RecipeHelper.addMelting(new ItemStack(Items.clock), gold, gold(MetalRates.INGOT * 4), new ItemStack(Items.redstone), 2);
        RecipeHelper.addMelting(new ItemStack(Items.golden_horse_armor), gold, gold(MetalRates.INGOT * 6), new ItemStack(Items.saddle), 4);

        //Iron Back
        RecipeHelper.addMelting(new ItemStack(Items.bucket), iron, getFluidName("iron"), MetalRates.INGOT * 3);
        RecipeHelper.addMelting(new ItemStack(Items.iron_door), iron, getFluidName("iron"), MetalRates.INGOT * 6);
        RecipeHelper.addMelting(new ItemStack(Blocks.iron_bars), iron, getFluidName("iron"), (int) (MetalRates.INGOT * 0.25));
        RecipeHelper.addMelting(new ItemStack(Items.shears), iron, getFluidName("iron"), MetalRates.INGOT * 2);
        RecipeHelper.addMelting(new ItemStack(Blocks.anvil, 1, 0), iron, getFluidName("iron"), MetalRates.INGOT * 31);
        RecipeHelper.addMelting(new ItemStack(Blocks.anvil, 1, 1), iron, getFluidName("iron"), MetalRates.INGOT * 22);
        RecipeHelper.addMelting(new ItemStack(Blocks.anvil, 1, 2), iron, getFluidName("iron"), MetalRates.INGOT * 13);
        RecipeHelper.addMelting(new ItemStack(Blocks.heavy_weighted_pressure_plate), iron, getFluidName("iron"), MetalRates.INGOT * 2);
        RecipeHelper.addMelting(new ItemStack(Items.compass), iron, iron(MetalRates.INGOT * 4), new ItemStack(Items.redstone), 2);
        RecipeHelper.addMelting(new ItemStack(Blocks.hopper), iron, iron(MetalRates.INGOT * 5), new ItemStack(Blocks.chest), 2);
        RecipeHelper.addMelting(new ItemStack(Items.flint_and_steel), iron, iron(MetalRates.INGOT));
        RecipeHelper.addMelting(new ItemStack(Items.iron_horse_armor), iron, iron(MetalRates.INGOT * 6), new ItemStack(Items.saddle), 4);

        //Glass, Ice, Snow, Plastic, Obisidian
        RecipeHelper.addBlockCasting(get(getFluidName("glass"), 1000), new ItemStack(glass));
        RecipeHelper.addMelting(new ItemStack(sand), 1000, getFluidName("glass"), 1000);
        RecipeHelper.addMelting(new ItemStack(glass), 900, getFluidName("glass"), 1000);
        RecipeHelper.addMelting(new ItemStack(glassPane), 500, getFluidName("glass"), 375);
        RecipeHelper.addMelting(new ItemStack(ice), 1, "water", 1000);
        RecipeHelper.addMelting(new ItemStack(blockSnow), 1, "water", 1000);

        RecipeHelper.addVatItemRecipe(new ItemStack(glass), getFluidName("natural_gas"), 1000, new ItemStack(Core.transparent, 1, TransparentMeta.PLASTIC), 5);
        if (getFluid("bioethanol") != null) {
            RecipeHelper.addVatItemRecipe(new ItemStack(glass), getFluidName("bioethanol"), 1500, new ItemStack(Core.transparent, 1, TransparentMeta.PLASTIC), 10);
        }

        RecipeHelper.addFluidAlloyResultItem(get("water", 1000), get("lava", 1000), new ItemStack(obsidian), 10);

        //8 Parts Quicklime + 5 Parts Water = Unknown Metal Dust + 3 Parts Water (Takes 10 seconds)
        RecipeHelper.addFluidAlloy(get("water", 1000), get(getFluidName("quicklime"), 3000), get("magnesium", MetalRates.INGOT), 9);

        ArrayList<ItemStack> added = new ArrayList();
        for (ItemStack stack : OreDictionary.getOres("blockLimestone")) {
            if (stack == null || stack.getItem() == null) {
                continue;
            }
            RecipeHelper.addMelting(stack, 825, get(getFluidName("quicklime"), 900));
            added.add(stack);
        }

        for (ItemStack stack : OreDictionary.getOres("limestone")) {
            boolean exists = false;
            if (stack == null || stack.getItem() == null) {
                continue;
            }
            for (ItemStack check : added)
                if (ItemHelper.areEqual(stack, check)) {
                    exists = true;
                }

            if (!exists) {
                RecipeHelper.addMelting(stack, 825, get(getFluidName("quicklime"), 900));
            }
        }

        RecipeHelper.addNuggetCasting(new FluidStack(FluidRegistry.WATER, 250), salt);
        RecipeHelper.addMelting(limestoneSmooth, 825, get(getFluidName("quicklime"), 1000));
        RecipeHelper.addMelting(dustSalt, 801, get(getFluidName("salt"), 20));
        RecipeHelper.addFluidAlloyNItemResultItem(get(getFluidName("aluminum"), MetalRates.NUGGET), get(getFluidName("quicklime"), 300), new ItemStack(glass), new ItemStack(Core.glass, 1, GlassMeta.HEAT), 3);
    }

    public static FluidStack gold(int vol) {
        return getFluidStack("gold", vol);
    }

    public static FluidStack iron(int vol) {
        return getFluidStack("iron", vol);
    }

    public static FluidStack get(String name, int vol) {
        return FluidRegistry.getFluidStack(name, vol);
    }

    public static FluidStack get(String name) {
        return FluidRegistry.getFluidStack(name, MetalRates.INGOT);
    }
}
