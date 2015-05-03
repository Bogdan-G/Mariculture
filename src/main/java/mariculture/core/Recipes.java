package mariculture.core;

import static mariculture.core.helpers.RecipeHelper.add4x4Recipe;
import static mariculture.core.helpers.RecipeHelper.add9x9Recipe;
import static mariculture.core.helpers.RecipeHelper.addAnvilRecipe;
import static mariculture.core.helpers.RecipeHelper.addCrossHatchRecipe;
import static mariculture.core.helpers.RecipeHelper.addShaped;
import static mariculture.core.helpers.RecipeHelper.addShapeless;
import static mariculture.core.helpers.RecipeHelper.addSmelting;
import static mariculture.core.helpers.RecipeHelper.addUncraftingRecipe;
import static mariculture.core.helpers.RecipeHelper.addUpgrade;
import static mariculture.core.helpers.RecipeHelper.addVatItemRecipe;
import static mariculture.core.helpers.RecipeHelper.addWheelRecipe;
import static mariculture.core.helpers.RecipeHelper.asStack;
import static mariculture.core.lib.MCLib.*;
import static mariculture.core.util.Fluids.getFluidName;
import mariculture.core.helpers.RecipeHelper;
import mariculture.core.lib.MCLib;
import mariculture.core.lib.MetalRates;
import mariculture.core.lib.Modules;
import mariculture.core.lib.UpgradeMeta;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Recipes {
    public static void add() {
        RecipesSmelting.add();
        addAnvilRecipes();
        addCraftingItems();
        addMetalRecipes();
        addUpgradeRecipes();

        //Items
        addShaped(hammer, new Object[] { "PP ", " SP", "S  ", 'P', burntBrick, 'S', netherBrick });
        addShaped(ladle, new Object[] { " C ", " C ", "C  ", 'C', "ingotCopper" });
        addShaped(titaniumBucket, new Object[] { "T T", " T ", 'T', "ingotTitanium" });

        addShaped(asStack(emptyBottle, 3), new Object[] { "G G", " G ", 'G', heatglass });
        addShapeless(asStack(voidBottle, 8), new Object[] { emptyBottle, "dustRedstone", ink });
        addShaped(oysterPie, new Object[] { "SSS", "BOP", "WEW", 'S', "foodSalt", 'B', beef, 'O', oyster, 'P', pork, 'W', wheat, 'E', egg });

        //Blocks
        addShaped(baseBrick, new Object[] { "IGI", "G G", "IGI", 'I', burntBrick, 'G', ironBars });
        addShaped(baseIron, new Object[] { "IGI", "G G", "IGI", 'I', "ingotIron", 'G', glassPane });
        addShaped(baseWood, new Object[] { "IGI", "G G", "IGI", 'I', "logWood", 'G', fence });
        addShaped(airPump, new Object[] { "WGW", "PRP", "PMP", 'G', "blockGlass", 'R', "dustRedstone", 'P', "plankWood", 'M', piston, 'W', ironWheel });
        addShaped(asStack(tank, 1), new Object[] { "CWC", "WGW", "CWC", 'C', "ingotCopper", 'W', "plankWood", 'G', "blockGlass" });
        addShaped(asStack(tankAluminum, 1), new Object[] { "CWC", "WGW", "CWC", 'C', "ingotAluminum", 'W', "stone", 'G', heatglass});
        addShaped(asStack(tankTitanium, 1), new Object[] { "CWC", "WGW", "CWC", 'C', "ingotTitanium", 'W', "blockQuartz", 'G', heatglass});
        addShaped(asStack(gasTank, 1), new Object[] { "ARA", "ATA", "ACA", 'A', "blockAluminum", 'R', rotor, 'T', tankTitanium, 'C', baseIron});
        addShaped(storageBookshelf, new Object[] { "SPS", "PCP", "SSS", 'P', "plankWood", 'S', bookshelf, 'C', chest });
        addShaped(asStack(crucible, 2), new Object[] { " L ", "BGB", "HCH", 'B', burntBrick, 'L', lava, 'G', tank, 'H', heating, 'C', baseBrick });
        addShaped(anvil, new Object[] { "CCC", " N ", "BBB", 'C', baseBrick, 'B', burntBrick, 'N', netherBrick });
        addShaped(autohammer, new Object[] { " N ", "NCN", " N ", 'C', anvil, 'N', hammer });
        addShaped(vat, new Object[] { "C C", "C C", "CCC", 'C', "ingotCopper" });
        addShaped(blockCaster, new Object[] { "BBB", "B B", "BBB", 'B', burntBrick });
        addShaped(ingotCaster, new Object[] { " B ", "BBB", " B ", 'B', burntBrick });
        addShaped(nuggetCaster, new Object[] { " B ", "B B", " B ", 'B', burntBrick });

        for (int i = 0; i < 12; i++) {
            add4x4Recipe(asStack(pearlBlock, i, 1), asStack(pearls, i, 1));
            addUncraftingRecipe(asStack(pearls, i, 4), asStack(pearlBlock, i, 1));
        }

        addShaped(asStack(piston), new Object[] { "TTT", "#X#", "#R#", '#', "cobblestone", 'X', "ingotAluminum", 'R', "dustRedstone", 'T', "plankWood" });
        addShaped(asStack(book), new Object[] { "PP", "PW", 'P', paper, 'W', wicker });
    }

    private static void addCraftingItems() {
        addShaped(life, new Object[] { "DSR", "FHB", "PAC", 'D', dandelion, 'S', "treeSapling", 'R', rose, 'F', "fish", 'H', regen, 'B', bait, 'P', potato, 'A', lily, 'C', carrot });
        addVatItemRecipe(asStack(string), getFluidName("gold"), MetalRates.INGOT * 4, goldSilk, 5);
        addShaped(goldThread, new Object[] { "ABA", "ABA", 'B', polishedStick, 'A', goldSilk });
        addShaped(glassLens, new Object[] { " P ", "PGP", " P ", 'P', "plankWood", 'G', "blockGlass" });
        addShaped(asStack(heating, 2), new Object[] { "CC", 'C', carbide });
        addShaped(cooling, new Object[] { "  P", "PI ", "  P", 'P', "plankWood", 'I', "ingotIron" });
        addShaped(cooling, new Object[] { " P ", " I ", "P P", 'P', "plankWood", 'I', "ingotIron" });
        addShaped(cooling, new Object[] { "P  ", " IP", "P  ", 'P', "plankWood", 'I', "ingotIron" });
        addShaped(cooling, new Object[] { "P P", " I ", " P ", 'P', "plankWood", 'I', "ingotIron" });
        addShaped(carbide, new Object[] { " S ", "FBF", " S ", 'F', coal, 'S', sand, 'B', blockClay });
        addWheelRecipe(asStack(ironWheel, 3), "ingotIron", "slabWood");
        addCrossHatchRecipe(wicker, "stickWood", reeds);
        addVatItemRecipe("stickWood", getFluidName("titanium"), MetalRates.INGOT * 3, titaniumRod, 30);
        addVatItemRecipe(asStack(nettherackBrick), "lava", 100, burntBrick, 3);
        addVatItemRecipe(asStack(brick), "lava", 250, burntBrick, 5);

        if (!Modules.isActive(Modules.worldplus)) {
            add9x9Recipe(kelpWrap, cactusGreen);
        }
    }

    private static void addMetalRecipes() {
        add9x9Recipe(ingotRutile, "nuggetRutile");
        addUncraftingRecipe(asStack(nuggetRutile, 9), "ingotRutile");
        add9x9Recipe(ingotMagnesium, "nuggetMagnesium");
        addUncraftingRecipe(asStack(nuggetMagnesium, 9), "ingotMagnesium");
        add9x9Recipe(ingotTitanium, "nuggetTitanium");
        addUncraftingRecipe(asStack(nuggetTitanium, 9), "ingotTitanium");
        add9x9Recipe(ingotAluminum, "nuggetAluminum");
        addUncraftingRecipe(asStack(nuggetAluminum, 9), "ingotAluminum");
        add9x9Recipe(ingotCopper, "nuggetCopper");
        addUncraftingRecipe(asStack(nuggetCopper, 9), "ingotCopper");
        add9x9Recipe(asStack(ingotIron), "nuggetIron");
        addUncraftingRecipe(asStack(nuggetIron, 9), "ingotIron");

        add9x9Recipe(blockRutile, "ingotRutile");
        addUncraftingRecipe(asStack(ingotRutile, 9), "blockRutile");
        add9x9Recipe(blockMagnesium, "ingotMagnesium");
        addUncraftingRecipe(asStack(ingotMagnesium, 9), "blockMagnesium");
        add9x9Recipe(blockTitanium, "ingotTitanium");
        addUncraftingRecipe(asStack(ingotTitanium, 9), "blockTitanium");
        add9x9Recipe(blockAluminum, "ingotAluminum");
        addUncraftingRecipe(asStack(ingotAluminum, 9), "blockAluminum");
        add9x9Recipe(blockCopper, "ingotCopper");
        addUncraftingRecipe(asStack(ingotCopper, 9), "blockCopper");
        addSmelting(ingotCopper, oreCopper, 0.5F);
    }

    private static void addUpgradeRecipes() {
        //Storage
        ItemStack previous = addUpgrade(UpgradeMeta.BASIC_STORAGE, new Object[] { "WPW", "DCD", "WPW", 'D', "ingotCopper", 'P', "plankWood", 'C', chest, 'W', wicker });
        previous = addUpgrade(UpgradeMeta.STANDARD_STORAGE, new Object[] { "WCW", "CUC", "STS", 'C', "ingotCopper", 'S', "slabWood", 'T', chest, 'W', wicker, 'U', previous });
        previous = addUpgrade(UpgradeMeta.ADVANCED_STORAGE, new Object[] { "CSC", "AUA", "WTW", 'C', "ingotCopper", 'S', "slabWood", 'T', chest, 'A', aluminumSheet, 'W', wicker, 'U', previous });
        addUpgrade(UpgradeMeta.ULTIMATE_STORAGE, new Object[] { "GWG", "WUW", "ATA", 'G', "ingotGold", 'T', chest, 'A', aluminumSheet, 'W', wicker, 'U', previous });

        //Cooling
        previous = addUpgrade(UpgradeMeta.BASIC_COOLING, new Object[] { " S ", "CBC", " S ", 'S', snowball, 'B', snow, 'C', cooling });
        previous = addUpgrade(UpgradeMeta.STANDARD_COOLING, new Object[] { "ACA", "SUS", "CAC", 'S', snowball, 'A', "ingotAluminum", 'C', cooling, 'U', previous });
        previous = addUpgrade(UpgradeMeta.ADVANCED_COOLING, new Object[] { "CTC", "IUI", "TRT", 'I', ice, 'R', "ingotIron", 'T', "ingotAluminum", 'C', cooling, 'U', previous });
        addUpgrade(UpgradeMeta.ULTIMATE_COOLING, new Object[] { "TCT", "IUI", "CDC", 'I', ice, 'D', "ingotAluminum", 'T', "ingotTitanium", 'C', cooling, 'U', previous });

        //Heating
        previous = addUpgrade(UpgradeMeta.BASIC_HEATING, new Object[] { "HIH", 'I', "ingotIron", 'H', heating });
        previous = addUpgrade(UpgradeMeta.STANDARD_HEATING, new Object[] { "A A", "HUH", " A ", 'A', "ingotAluminum", 'H', heating, 'U', previous });
        previous = addUpgrade(UpgradeMeta.ADVANCED_HEATING, new Object[] { "IHI", "TUT", "IHI", 'T', "ingotAluminum", 'I', "ingotIron", 'H', heating, 'U', previous });
        addUpgrade(UpgradeMeta.ULTIMATE_HEATING, new Object[] { "TDT", "HUH", "GTG", 'G', "ingotGold", 'T', "ingotTitanium", 'D', blazeRod, 'H', heating, 'U', previous });

        //Purity and Filtrator
        previous = RecipeHelper.addUpgrade(UpgradeMeta.BASIC_PURITY, new Object[] { " A ", "PDP", " A ", 'D', "dyeRed", 'A', "ingotAluminum", 'P', whitePearl });
        ItemStack previous2 = RecipeHelper.addUpgrade(UpgradeMeta.FILTER, new Object[] { "MPM", "CUC", "AMA", 'A', "ingotAluminum", 'M', "ingotMagnesium", 'P', bluePearl, 'C', cooling, 'U', previous });
        addUpgrade(UpgradeMeta.FILTER_2, new Object[] { "UAU", 'A', "ingotAluminum", 'U', previous2 });
        previous = addUpgrade(UpgradeMeta.STANDARD_PURITY, new Object[] { "APA", "DUD", "APA", 'D', heart, 'A', "ingotAluminum", 'P', whitePearl, 'U', previous });
        previous = addUpgrade(UpgradeMeta.ADVANCED_PURITY, new Object[] { "RFR", "PUP", "ARA", 'R', heart, 'F', angelfish, 'P', whitePearl, 'U', previous, 'A', aquatic });
        addUpgrade(UpgradeMeta.ULTIMATE_PURITY, new Object[] { "RWR", "AUA", "PFP", 'R', heart, 'A', aquatic, 'P', healthPotion, 'U', previous, 'F', koi, 'W', whitePearl });

        //Impurity and salinator
        previous = RecipeHelper.addUpgrade(UpgradeMeta.BASIC_IMPURITY, new Object[] { " B ", "ENE", " B ", 'N', netherWart, 'E', fermentedEye, 'B', bone });
        previous2 = RecipeHelper.addUpgrade(UpgradeMeta.SALINATOR, new Object[] { "ASA", "SUS", "MAM", 'A', "ingotAluminum", 'S', "foodSalt", 'M', "ingotMagnesium", 'U', previous });
        addUpgrade(UpgradeMeta.SALINATOR_2, new Object[] { "UAU", 'A', "ingotAluminum", 'U', previous2 });
        previous = RecipeHelper.addUpgrade(UpgradeMeta.STANDARD_IMPURITY, new Object[] { " B ", "PUP", " B ", 'B', spider, 'P', poison, 'U', previous });
        previous = RecipeHelper.addUpgrade(UpgradeMeta.ADVANCED_IMPURITY, new Object[] { "PDP", "SUS", "HPH", 'D', undead, 'P', poison, 'H', attack, 'S', blaasop, 'U', previous });
        RecipeHelper.addUpgrade(UpgradeMeta.ULTIMATE_IMPURITY, new Object[] { "PDP", "DUD", "HLH", 'P', poison, 'L', boneless, 'D', attack, 'B', bone, 'U', previous, 'H', new ItemStack(Items.potionitem, 1, 8268) });

        //Ethereal
        addUpgrade(UpgradeMeta.ETHEREAL, new Object[] { "PUP", "GEG", "PDP", 'P', enderPearl, 'G', "ingotGold", 'E', eyeOfEnder, 'D', diamond, 'U', redstoneTorch });

        //Speed
        previous = addUpgrade(UpgradeMeta.BASIC_SPEED, new Object[] { " W ", "WSW", " W ", 'W', sugar, 'S', "ingotIron" });
        previous = addUpgrade(UpgradeMeta.STANDARD_SPEED, new Object[] { " S ", "AUA", " S ", 'A', "ingotAluminum", 'S', sugar, 'U', previous });
        previous = addUpgrade(UpgradeMeta.ADVANCED_SPEED, new Object[] { "ASA", "TUT", "SNS", 'A', "ingotAluminum", 'S', sugar, 'T', "ingotTitanium", 'N', ice, 'U', previous });
        addUpgrade(UpgradeMeta.ULTIMATE_SPEED, new Object[] { "TRT", "SUS", "ARA", 'A', aluminumSheet, 'S', sugar, 'T', "ingotTitanium", 'R', goldRail, 'U', previous });
    }

    public static void addAnvilRecipes() {
        addAnvilRecipe(new ItemStack(Blocks.gold_block), asStack(MCLib.goldSheet, 8), 50);
        addAnvilRecipe(blockAluminum, asStack(aluminumSheet, 8), 30);
        addAnvilRecipe(blockTitanium, asStack(titaniumSheet, 8), 75);
        addAnvilRecipe(asStack(bone), asStack(bonemeal, 7), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 0), asStack(roseDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 1), asStack(lightBlueDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 2), asStack(magentaDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 3), asStack(lightGreyDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 4), asStack(roseDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 5), asStack(orangeDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 6), asStack(lightGreyDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 7), asStack(pinkDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.red_flower, 1, 8), asStack(lightGreyDye, 2), 5);
        addAnvilRecipe(new ItemStack(Blocks.yellow_flower), asStack(dandelionDye, 3), 5);
        addAnvilRecipe(new ItemStack(Blocks.double_plant, 1, 0), asStack(dandelionDye, 4), 5);
        addAnvilRecipe(new ItemStack(Blocks.double_plant, 1, 1), asStack(magentaDye, 4), 5);
        addAnvilRecipe(new ItemStack(Blocks.double_plant, 1, 4), asStack(roseDye, 4), 5);
        addAnvilRecipe(new ItemStack(Blocks.double_plant, 1, 5), asStack(pinkDye, 4), 5);
    }
}
