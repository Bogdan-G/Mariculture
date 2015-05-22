package mariculture.core.helpers;

import java.util.Random;

import mariculture.core.tile.base.TileMultiBlock;
import mariculture.core.util.Fluids;
import mariculture.core.util.IItemDropBlacklist;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHelper {

    public static boolean isWater(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) == Blocks.water;
    }

    public static boolean isHPWater(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) == Fluids.getFluidBlock("hp_water");
    }

    public static boolean isLava(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) == Blocks.lava;
    }

    public static boolean isFishLiveable(World world, int x, int y, int z) {
        if (world.provider.isHellWorld) return isLava(world, x, y, z);
        return isWater(world, x, y, z);
    }

    public static boolean isFishable(World world, int x, int y, int z) {
        return isWater(world, x, y, z) || isLava(world, x, y, z) && world.provider.isHellWorld;
    }

    public static boolean isAir(World world, int x, int y, int z) {
        if (world.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
            try {
                return world.isAirBlock(x, y, z);
            } catch (Exception e) {}
        }

        return false;
    }

    public static void setBlock(World world, int x, int y, int z, Block block, int meta) {
        if (world.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
            try {
                world.setBlock(x, y, z, block, meta, 3);
            } catch (Exception e) {}
        }
    }

    public static Block getBlock(World world, int x, int y, int z) {
        if (world.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
            try {
                return world.getBlock(x, y, z);
            } catch (Exception e) {}
        }

        return Blocks.stone;
    }

    public static int getMeta(World world, int x, int y, int z) {
        if (world.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
            try {
                return world.getBlockMetadata(x, y, z);
            } catch (Exception e) {}
        }

        return -1;
    }

    public static boolean chunkExists(World world, int x, int z) {
        return world.getChunkProvider().chunkExists(x >> 4, z >> 4);
    }

    public static String getName(ItemStack stack) {
        return stack != null ? stack.getItem().getItemStackDisplayName(stack) : "";
    }

    public static String getName(TileEntity tile) {
        return tile != null ? getName(new ItemStack(tile.getBlockType(), 1, tile.getBlockMetadata())) : "";
    }

    public static ForgeDirection rotate(ForgeDirection dir, int num) {
        if (dir == ForgeDirection.NORTH) return ForgeDirection.EAST;
        if (dir == ForgeDirection.EAST) return num == 2 ? ForgeDirection.NORTH : ForgeDirection.SOUTH;
        if (dir == ForgeDirection.SOUTH) return ForgeDirection.WEST;
        if (dir == ForgeDirection.WEST) return num == 6 ? ForgeDirection.UP : ForgeDirection.NORTH;
        if (dir == ForgeDirection.UP) return ForgeDirection.DOWN;
        return ForgeDirection.NORTH;
    }

    public static ForgeDirection rotate(ForgeDirection dir) {
        return rotate(dir, 6);
    }

    public static void dropItems(World world, int x, int y, int z) {
        Random rand = world.rand;
        TileEntity tile = world.getTileEntity(x, y, z);

        if (!(tile instanceof IInventory)) return;

        if (tile instanceof TileMultiBlock) {
            TileMultiBlock multi = (TileMultiBlock) tile;
            if (multi.master != null) if (!multi.isMaster()) return;
        }

        IInventory inventory = (IInventory) tile;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            boolean drop = true;
            if (tile instanceof IItemDropBlacklist) {
                drop = ((IItemDropBlacklist) tile).doesDrop(i);
            }

            if (drop) {
                ItemStack item = inventory.getStackInSlot(i);
                if (item != null && item.stackSize > 0) {
                    item = item.copy();
                    inventory.decrStackSize(i, 1);
                    float rx = rand.nextFloat() * 0.6F + 0.1F;
                    float ry = rand.nextFloat() * 0.6F + 0.1F;
                    float rz = rand.nextFloat() * 0.6F + 0.1F;

                    EntityItem entity_item = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

                    if (item.hasTagCompound()) {
                        entity_item.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                    }

                    float factor = 0.05F;

                    entity_item.motionX = rand.nextGaussian() * factor;
                    entity_item.motionY = rand.nextGaussian() * factor + 0.2F;
                    entity_item.motionZ = rand.nextGaussian() * factor;
                    world.spawnEntityInWorld(entity_item);
                    item.stackSize = 0;
                }
            }
        }
    }

    private static boolean removeBlock(World world, EntityPlayerMP player, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int l = world.getBlockMetadata(x, y, z);
        block.onBlockHarvested(world, x, y, z, l, player);
        boolean flag = block.removedByPlayer(world, player, x, y, z, true);

        if (flag) {
            block.onBlockDestroyedByPlayer(world, x, y, z, l);
        }

        return flag;
    }

    public static void destroyBlock(World world, int x, int y, int z) {
        destroyBlock(world, x, y, z, null, null);
    }

    public static void destroyBlock(World world, int x, int y, int z, Block required, ItemStack held) {
        if (!(world instanceof WorldServer)) return;
        Block block = world.getBlock(x, y, z);
        if (required != null && block != required) return;
        if (block.getBlockHardness(world, x, y, z) < 0.0F) return;
        FakePlayer player = PlayerHelper.getFakePlayer(world);
        if (player != null && held != null) {
            player.setCurrentItemOrArmor(0, held.copy());
        }

        int l = world.getBlockMetadata(x, y, z);
        world.playAuxSFXAtEntity(player, 2001, x, y, z, Block.getIdFromBlock(block) + (world.getBlockMetadata(x, y, z) << 12));
        boolean flag = removeBlock(world, player, x, y, z);
        if (flag) {
            block.harvestBlock(world, player, x, y, z, l);
        }

        return;
    }
}
