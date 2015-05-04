package mariculture.magic;

import static mariculture.core.helpers.RecipeHelper.addShaped;
import static mariculture.core.helpers.RecipeHelper.asStack;
import static mariculture.core.lib.MCLib.bucket;
import static mariculture.core.lib.MCLib.enchant;
import static mariculture.core.lib.MCLib.enderPearl;
import static mariculture.core.lib.MCLib.goldThread;
import static mariculture.core.lib.MCLib.magicDrop;
import static mariculture.core.lib.MCLib.netherStar;
import static mariculture.core.lib.MCLib.pearls;
import static mariculture.core.lib.MCLib.storageBookshelf;
import static mariculture.core.lib.MCLib.waterBottle;

import java.util.Map.Entry;

import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.MaricultureTab;
import mariculture.core.Core;
import mariculture.core.config.Enchantments.EnchantIds;
import mariculture.core.config.Enchantments.Jewelry;
import mariculture.core.helpers.EnchantHelper;
import mariculture.core.lib.Modules.RegistrationModule;
import mariculture.magic.enchantments.EnchantmentBlink;
import mariculture.magic.enchantments.EnchantmentElemental;
import mariculture.magic.enchantments.EnchantmentFallDamage;
import mariculture.magic.enchantments.EnchantmentFlight;
import mariculture.magic.enchantments.EnchantmentGlide;
import mariculture.magic.enchantments.EnchantmentHealth;
import mariculture.magic.enchantments.EnchantmentJump;
import mariculture.magic.enchantments.EnchantmentNeverHungry;
import mariculture.magic.enchantments.EnchantmentOneRing;
import mariculture.magic.enchantments.EnchantmentRestore;
import mariculture.magic.enchantments.EnchantmentResurrection;
import mariculture.magic.enchantments.EnchantmentSpeed;
import mariculture.magic.enchantments.EnchantmentSpider;
import mariculture.magic.enchantments.EnchantmentStepUp;
import mariculture.magic.jewelry.ItemBracelet;
import mariculture.magic.jewelry.ItemJewelry;
import mariculture.magic.jewelry.ItemJewelry.JewelryType;
import mariculture.magic.jewelry.ItemNecklace;
import mariculture.magic.jewelry.ItemRing;
import mariculture.magic.jewelry.parts.BindingBasic;
import mariculture.magic.jewelry.parts.BindingDummy;
import mariculture.magic.jewelry.parts.BindingGold;
import mariculture.magic.jewelry.parts.JewelryBinding;
import mariculture.magic.jewelry.parts.JewelryMaterial;
import mariculture.magic.jewelry.parts.MaterialDummy;
import mariculture.magic.jewelry.parts.MaterialPearlBlack;
import mariculture.magic.jewelry.parts.MaterialPearlBlue;
import mariculture.magic.jewelry.parts.MaterialPearlBrown;
import mariculture.magic.jewelry.parts.MaterialPearlGold;
import mariculture.magic.jewelry.parts.MaterialPearlGreen;
import mariculture.magic.jewelry.parts.MaterialPearlOrange;
import mariculture.magic.jewelry.parts.MaterialPearlPink;
import mariculture.magic.jewelry.parts.MaterialPearlPurple;
import mariculture.magic.jewelry.parts.MaterialPearlRed;
import mariculture.magic.jewelry.parts.MaterialPearlSilver;
import mariculture.magic.jewelry.parts.MaterialPearlWhite;
import mariculture.magic.jewelry.parts.MaterialPearlYellow;
import mariculture.plugins.bloodmagic.ItemMobMagnetBloodEdition;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import cpw.mods.fml.common.Loader;

public class Magic extends RegistrationModule {
    public static JewelryMaterial dummyMaterial;
    public static JewelryMaterial pearlBlack;
    public static JewelryMaterial pearlBlue;
    public static JewelryMaterial pearlBrown;
    public static JewelryMaterial pearlGold;
    public static JewelryMaterial pearlGreen;
    public static JewelryMaterial pearlOrange;
    public static JewelryMaterial pearlPink;
    public static JewelryMaterial pearlPurple;
    public static JewelryMaterial pearlRed;
    public static JewelryMaterial pearlSilver;
    public static JewelryMaterial pearlWhite;
    public static JewelryMaterial pearlYellow;
    public static JewelryBinding bindingGold;
    public static JewelryBinding bindingBasic;
    public static JewelryBinding dummyBinding;

    public static Enchantment elemental;
    public static Enchantment spider;
    public static Enchantment blink;
    public static Enchantment fall;
    public static Enchantment flight;
    public static Enchantment glide;
    public static Enchantment health;
    public static Enchantment jump;
    public static Enchantment hungry;
    public static Enchantment oneRing;
    public static Enchantment repair;
    public static Enchantment resurrection;
    public static Enchantment speed;
    public static Enchantment stepUp;

    public static Item basicMirror;
    public static Item magicMirror;
    public static Item celestialMirror;
    public static Item ring;
    public static Item bracelet;
    public static Item necklace;
    public static Item magnet;
    public static Item placer;
    public static Item water;

    @Override
    public void registerHandlers() {
        MaricultureHandlers.mirror = new MirrorHandler();
        MinecraftForge.EVENT_BUS.register(new MagicEventHandler());
    }

    @Override
    public void registerBlocks() {
        return;
    }

    @Override
    public void registerItems() {
        basicMirror = new ItemMirror("mirror").setUnlocalizedName("mirror.basic");
        magicMirror = new ItemMagicMirror(1, 30, "magicMirror", 15, 1000).setUnlocalizedName("mirror.magic");
        celestialMirror = new ItemMagicMirror(31, 60, "celestialMirror", 20, 10000).setUnlocalizedName("mirror.celestial");
        ring = new ItemRing().setUnlocalizedName("ring");
        bracelet = new ItemBracelet().setUnlocalizedName("bracelet");
        necklace = new ItemNecklace().setUnlocalizedName("necklace");
        if (Loader.isModLoaded("AWWayofTime")) {
            magnet = new ItemMobMagnetBloodEdition(0).setUnlocalizedName("mobMagnet");
        } else {
            magnet = new ItemMobMagnet(0).setUnlocalizedName("mobMagnet");
        }
        
        placer = new ItemWaterPlacer(0).setUnlocalizedName("placer");
        water = new ItemInfiwater().setUnlocalizedName("infiwater");
    }

    @Override
    public void registerOther() {
        RecipeSorter.register("mariculture:jewelryshaped", ShapedJewelryRecipe.class, Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register("mariculture:jewelryshapeless", ShapelessJewelryRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");

        registerJewelry();
        registerEnchants();
        if (MaricultureTab.tabMagic != null) {
            MaricultureTab.tabMagic.setIcon(new ItemStack(basicMirror), false);
        }
    }

    private void registerJewelry() {
        dummyBinding = new BindingDummy();
        dummyMaterial = new MaterialDummy();
        //Real parts
        pearlBlack = new MaterialPearlBlack();
        pearlBlue = new MaterialPearlBlue();
        pearlBrown = new MaterialPearlBrown();
        pearlGold = new MaterialPearlGold();
        pearlGreen = new MaterialPearlGreen();
        pearlOrange = new MaterialPearlOrange();
        pearlPink = new MaterialPearlPink();
        pearlPurple = new MaterialPearlPurple();
        pearlRed = new MaterialPearlRed();
        pearlSilver = new MaterialPearlSilver();
        pearlWhite = new MaterialPearlWhite();
        pearlYellow = new MaterialPearlYellow();
        bindingBasic = new BindingBasic();
        bindingGold = new BindingGold();
    }

    private void registerEnchants() {
        if (EnchantIds.spider > 0) {
            spider = new EnchantmentSpider(EnchantIds.spider, 3, EnumEnchantmentType.all);
        }
        if (EnchantIds.blink > 0) {
            blink = new EnchantmentBlink(EnchantIds.blink, 5, EnumEnchantmentType.all);
        }
        if (EnchantIds.fall > 0) {
            fall = new EnchantmentFallDamage(EnchantIds.fall, 5, EnumEnchantmentType.all);
        }
        if (EnchantIds.flight > 0) {
            flight = new EnchantmentFlight(EnchantIds.flight, 1, EnumEnchantmentType.all);
        }
        if (EnchantIds.glide > 0) {
            glide = new EnchantmentGlide(EnchantIds.glide, 1, EnumEnchantmentType.all);
        }
        if (EnchantIds.health > 0) {
            health = new EnchantmentHealth(EnchantIds.health, 4, EnumEnchantmentType.all);
        }
        if (EnchantIds.jump > 0) {
            jump = new EnchantmentJump(EnchantIds.jump, 6, EnumEnchantmentType.all);
        }
        if (EnchantIds.hungry > 0) {
            hungry = new EnchantmentNeverHungry(EnchantIds.hungry, 2, EnumEnchantmentType.all);
        }
        if (EnchantIds.oneRing > 0) {
            oneRing = new EnchantmentOneRing(EnchantIds.oneRing, 0, EnumEnchantmentType.all);
        }
        if (EnchantIds.repair > 0) {
            repair = new EnchantmentRestore(EnchantIds.repair, 3, EnumEnchantmentType.all);
        }
        if (EnchantIds.resurrection > 0) {
            resurrection = new EnchantmentResurrection(EnchantIds.resurrection, 1, EnumEnchantmentType.all);
        }
        if (EnchantIds.speed > 0) {
            speed = new EnchantmentSpeed(EnchantIds.speed, 6, EnumEnchantmentType.all);
        }
        if (EnchantIds.stepUp > 0) {
            stepUp = new EnchantmentStepUp(EnchantIds.stepUp, 5, EnumEnchantmentType.all);
        }
        if (EnchantIds.elemental > 0) {
            elemental = new EnchantmentElemental(EnchantIds.elemental, 4, EnumEnchantmentType.all);
        }
    }

    @Override
    public void registerRecipes() {
        addShaped(asStack(water), new Object[] { "PWP", "WBW", "PWP", 'P', pearls, 'W', waterBottle, 'B', bucket });
        addShaped(asStack(magnet), new Object[] { "III", "I I", "M M", 'I', "ingotIron", 'M', enderPearl });
        addShaped(asStack(basicMirror), new Object[] { " AA", "APA", "SA ", 'A', "ingotAluminum", 'P', Blocks.glass_pane, 'S', "ingotIron" });
        addShaped(asStack(magicMirror), new Object[] { "PMP", "BEB", "PBP", 'B', storageBookshelf, 'M', basicMirror, 'E', enchant, 'P', pearls });
        addShaped(asStack(celestialMirror), new Object[] { "TST", "BMB", "GBG", 'B', storageBookshelf, 'M', magicMirror, 'S', netherStar, 'T', magicDrop, 'G', goldThread });
        addShaped(asStack(placer), new Object[] { " L ", "LPL", " L ", 'L', Blocks.waterlily, 'P', asStack(Core.pearls, OreDictionary.WILDCARD_VALUE) });
        addJewelry((ItemJewelry) ring);
        addJewelry((ItemJewelry) bracelet);
        addJewelry((ItemJewelry) necklace);
        
        if (Jewelry.ALTERNATIVE_CELESTIAL_RECIPE) {
            addShaped(asStack(celestialMirror), new Object[] { "RWN", "BMB", "GBG", 'B', storageBookshelf, 'M', magicMirror, 
                'R', JewelryHandler.createJewelry((ItemJewelry)ring, bindingBasic, pearlRed), 
                'W', JewelryHandler.createJewelry((ItemJewelry)bracelet, bindingBasic, pearlBlack), 
                'N', JewelryHandler.createJewelry((ItemJewelry)necklace, bindingBasic, pearlGold), 
                'G', goldThread });
        }
        
        addDungeonChestLoot();
    }

    private void addJewelry(ItemJewelry item) {
        JewelryType type = item.getType();
        for (Entry<String, JewelryBinding> binding : JewelryBinding.list.entrySet()) {
            if (binding.getValue().ignore) {
                continue;
            }
            for (Entry<String, JewelryMaterial> material : JewelryMaterial.list.entrySet()) {
                if (material.getValue().ignore) {
                    continue;
                }
                JewelryBinding bind = binding.getValue();
                JewelryMaterial mat = material.getValue();
                ItemStack worked = JewelryHandler.createJewelry(item, bind, mat);
                JewelryHandler.addJewelry(type, worked, bind.getCraftingItem(type), mat.getCraftingItem(type), (int) (bind.getHitsBase(type) * mat.getHitsModifier(type)));
            }
        }
    }

    private void addDungeonChestLoot() {
        if (EnchantHelper.exists(oneRing)) {
            ItemStack oneRing = JewelryHandler.createSpecial((ItemJewelry) ring, JewelryType.RING, mariculture.lib.util.Text.YELLOW + "One Ring");
            if (oneRing != null) {
                oneRing.addEnchantment(Magic.oneRing, 1);
                ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(oneRing, 1, 1, 1));
            }
        }
    }
}
