package mariculture.plugins;

import static thaumcraft.api.aspects.Aspect.AIR;
import static thaumcraft.api.aspects.Aspect.ARMOR;
import static thaumcraft.api.aspects.Aspect.BEAST;
import static thaumcraft.api.aspects.Aspect.COLD;
import static thaumcraft.api.aspects.Aspect.CRYSTAL;
import static thaumcraft.api.aspects.Aspect.DARKNESS;
import static thaumcraft.api.aspects.Aspect.EARTH;
import static thaumcraft.api.aspects.Aspect.ELDRITCH;
import static thaumcraft.api.aspects.Aspect.ENERGY;
import static thaumcraft.api.aspects.Aspect.ENTROPY;
import static thaumcraft.api.aspects.Aspect.FIRE;
import static thaumcraft.api.aspects.Aspect.FLESH;
import static thaumcraft.api.aspects.Aspect.GREED;
import static thaumcraft.api.aspects.Aspect.HEAL;
import static thaumcraft.api.aspects.Aspect.LIFE;
import static thaumcraft.api.aspects.Aspect.LIGHT;
import static thaumcraft.api.aspects.Aspect.MAGIC;
import static thaumcraft.api.aspects.Aspect.MECHANISM;
import static thaumcraft.api.aspects.Aspect.METAL;
import static thaumcraft.api.aspects.Aspect.MIND;
import static thaumcraft.api.aspects.Aspect.PLANT;
import static thaumcraft.api.aspects.Aspect.POISON;
import static thaumcraft.api.aspects.Aspect.TOOL;
import static thaumcraft.api.aspects.Aspect.TREE;
import static thaumcraft.api.aspects.Aspect.WATER;
import mariculture.core.Core;
import mariculture.core.lib.BaitMeta;
import mariculture.core.lib.DropletMeta;
import mariculture.core.lib.Modules;
import mariculture.core.lib.PearlColor;
import mariculture.core.lib.WaterMeta;
import mariculture.fishery.Fishery;
import mariculture.plugins.Plugins.Plugin;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;

public class PluginThaumcraft extends Plugin {
    public PluginThaumcraft(String name) {
        super(name);
    }

    @Override
    public void preInit() {
        return;
    }

    @Override
    public void init() {
        ThaumcraftApi.registerObjectTag("nuggetAluminum", new AspectList().add(METAL, 1));
        ThaumcraftApi.registerObjectTag("nuggetRutile", new AspectList().add(METAL, 1));
        ThaumcraftApi.registerObjectTag("nuggetTitanium", new AspectList().add(METAL, 1));
        ThaumcraftApi.registerObjectTag("nuggetMagnesium", new AspectList().add(METAL, 1));
        ThaumcraftApi.registerObjectTag("ingotAluminum", new AspectList().add(AIR, 2).add(METAL, 1));
        ThaumcraftApi.registerObjectTag("ingotRutile", new AspectList().add(METAL, 2).add(ENTROPY, 1));
        ThaumcraftApi.registerObjectTag("ingotTitanium", new AspectList().add(METAL, 4).add(DARKNESS, 1));
        ThaumcraftApi.registerObjectTag("ingotMagnesium", new AspectList().add(ENERGY, 1).add(METAL, 1).add(ENTROPY, 1));
        ThaumcraftApi.registerObjectTag("oreAluminum", new AspectList().add(METAL, 1).add(AIR, 2));
        ThaumcraftApi.registerObjectTag("oreRutile", new AspectList().add(METAL, 4).add(ENTROPY, 1));
        ThaumcraftApi.registerObjectTag("blockLimestone", new AspectList().add(WATER, 1).add(EARTH, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.water, 1, WaterMeta.OYSTER), new AspectList().add(WATER, 5).add(LIFE, 3));

        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.WHITE), new AspectList().add(WATER, 1).add(CRYSTAL, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.GREEN), new AspectList().add(WATER, 1).add(TREE, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.YELLOW), new AspectList().add(WATER, 1).add(HEAL, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.ORANGE), new AspectList().add(WATER, 1).add(ARMOR, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.RED), new AspectList().add(WATER, 1).add(ENERGY, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.GOLD), new AspectList().add(WATER, 1).add(GREED, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.BROWN), new AspectList().add(WATER, 1).add(TOOL, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.PURPLE), new AspectList().add(WATER, 1).add(MIND, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.BLUE), new AspectList().add(WATER, 1).add(BEAST, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.BLACK), new AspectList().add(WATER, 1).add(MECHANISM, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.PINK), new AspectList().add(WATER, 1).add(PLANT, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(Core.pearls, 1, PearlColor.SILVER), new AspectList().add(WATER, 1).add(LIGHT, 1));

        if (Modules.isActive(Modules.fishery)) {
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.bait, 1, BaitMeta.ANT), new AspectList().add(BEAST, 1));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.bait, 1, BaitMeta.HOPPER), new AspectList().add(BEAST, 1).add(PLANT, 1));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.bait, 1, BaitMeta.MAGGOT), new AspectList().add(BEAST, 1).add(FLESH, 1));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.bait, 1, BaitMeta.WORM), new AspectList().add(BEAST, 1).add(EARTH, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.bait, 1, BaitMeta.BEE), new AspectList().add(BEAST, 1).add(AIR, 2));

            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.WATER), new AspectList().add(WATER, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.ATTACK), new AspectList().add(ENTROPY, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.ELECTRIC), new AspectList().add(ENERGY, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.ENDER), new AspectList().add(ELDRITCH, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.HEALTH), new AspectList().add(HEAL, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.MAGIC), new AspectList().add(MAGIC, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.NETHER), new AspectList().add(FIRE, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.POISON), new AspectList().add(POISON, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.WATER), new AspectList().add(WATER, 1));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.EARTH), new AspectList().add(EARTH, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.FROZEN), new AspectList().add(COLD, 2));
            ThaumcraftApi.registerObjectTag(new ItemStack(Fishery.droplet, 1, DropletMeta.PLANT), new AspectList().add(PLANT, 2));
        }

        if (Modules.isActive(Modules.worldplus)) {
            ThaumcraftApi.registerObjectTag("coralLightBlue", new AspectList().add(WATER, 1).add(BEAST, 1));
            ThaumcraftApi.registerObjectTag("coralRed", new AspectList().add(WATER, 1).add(BEAST, 1));
            ThaumcraftApi.registerObjectTag("coralOrange", new AspectList().add(WATER, 1).add(BEAST, 1));
            ThaumcraftApi.registerObjectTag("coralPink", new AspectList().add(WATER, 1).add(BEAST, 1));
            ThaumcraftApi.registerObjectTag("coralPurple", new AspectList().add(WATER, 1).add(BEAST, 1));
            ThaumcraftApi.registerObjectTag("coralMagenta", new AspectList().add(WATER, 1).add(BEAST, 1));
            ThaumcraftApi.registerObjectTag("coralYellow", new AspectList().add(WATER, 1).add(BEAST, 1));
            ThaumcraftApi.registerObjectTag("plantKelp", new AspectList().add(WATER, 1).add(PLANT, 2));
        }
    }

    @Override
    public void postInit() {
        return;
    }
}
