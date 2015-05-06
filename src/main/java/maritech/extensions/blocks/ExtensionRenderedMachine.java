package maritech.extensions.blocks;

import mariculture.api.util.CachedCoords;
import mariculture.core.Core;
import mariculture.core.lib.MachineRenderedMeta;
import mariculture.core.lib.MetalMeta;
import mariculture.core.network.PacketHandler;
import mariculture.core.util.Fluids;
import mariculture.lib.helpers.DirectionHelper;
import mariculture.lib.helpers.ItemHelper;
import maritech.extensions.modules.ExtensionFactory;
import maritech.items.ItemFLUDD;
import maritech.tile.TileFLUDDStand;
import maritech.tile.TileGenerator;
import maritech.tile.TileRotor;
import maritech.tile.TileRotorAluminum;
import maritech.tile.TileRotorCopper;
import maritech.tile.TileRotorTitanium;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class ExtensionRenderedMachine extends ExtensionBlocksBase {
    @Override
    public String getName(int meta, String name) {
        switch (meta) {
            case MachineRenderedMeta.FLUDD_STAND:
                return "fludd";
            case MachineRenderedMeta.ROTOR_COPPER:
                return "rotorCopper";
            case MachineRenderedMeta.ROTOR_ALUMINUM:
                return "rotorAluminum";
            case MachineRenderedMeta.ROTOR_TITANIUM:
                return "rotorTitanium";
        }

        return name;
    }

    @Override
    public String getMod(int meta, String name) {
        switch (meta) {
            case MachineRenderedMeta.FLUDD_STAND:
            case MachineRenderedMeta.ROTOR_COPPER:
            case MachineRenderedMeta.ROTOR_ALUMINUM:
            case MachineRenderedMeta.ROTOR_TITANIUM:
                return "maritech";
        }

        return name;
    }

    @Override
    public int getToolLevel(int meta, int level) {
        switch (meta) {
            case MachineRenderedMeta.ROTOR_COPPER:
            case MachineRenderedMeta.ROTOR_ALUMINUM:
                return 1;
            case MachineRenderedMeta.ROTOR_TITANIUM:
                return 2;
        }

        return level;
    }

    @Override
    public float getHardness(int meta, float hardness) {
        switch (meta) {
            case MachineRenderedMeta.FLUDD_STAND:
                return 1F;
            case MachineRenderedMeta.ROTOR_COPPER:
                return 5F;
            case MachineRenderedMeta.ROTOR_ALUMINUM:
                return 6.5F;
            case MachineRenderedMeta.ROTOR_TITANIUM:
                return 15F;
        }

        return hardness;
    }

    @Override
    public TileEntity getTileEntity(int meta, TileEntity tile) {
        switch (meta) {
            case MachineRenderedMeta.FLUDD_STAND:
                return new TileFLUDDStand();
            case MachineRenderedMeta.ROTOR_COPPER:
                return new TileRotorCopper();
            case MachineRenderedMeta.ROTOR_ALUMINUM:
                return new TileRotorAluminum();
            case MachineRenderedMeta.ROTOR_TITANIUM:
                return new TileRotorTitanium();
        }

        return tile;
    }

    @Override
    public void onTilePlaced(ItemStack stack, TileEntity tile, EntityLivingBase entity, int direction) {
        if (tile instanceof TileFLUDDStand) {
            TileFLUDDStand fludd = (TileFLUDDStand) tile;
            fludd.setFacing(ForgeDirection.getOrientation(direction));

            int water = 0;
            if (stack.hasTagCompound()) {
                water = stack.stackTagCompound.getInteger("water");
            }

            fludd.tank.setCapacity(ItemFLUDD.STORAGE);
            fludd.tank.setFluid(new FluidStack(Fluids.getFluid("hp_water"), water));

            if (!tile.getWorldObj().isRemote) {
                PacketHandler.updateRender(fludd);
            }
        }

        if (tile instanceof TileRotor) {
            ((TileRotor) tile).setFacing(DirectionHelper.getFacingFromEntity(entity));
        }
    }

    @Override
    public boolean onBlockBroken(int meta, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFLUDDStand) {
            TileFLUDDStand stand = (TileFLUDDStand) tile;
            ItemStack fludd = new ItemStack(ExtensionFactory.fludd);
            fludd.setTagCompound(new NBTTagCompound());
            fludd.stackTagCompound.setInteger("water", stand.tank.getFluidAmount());
            ItemHelper.spawnItem(world, x, y, z, fludd);

            return world.setBlockToAir(x, y, z);
        } else if (tile instanceof TileRotor) {
            CachedCoords cord = ((TileRotor) tile).master;
            if (cord != null) {
                world.setBlock(x, y, z, Core.metals, MetalMeta.BASE_IRON, 2);
                TileEntity gen = world.getTileEntity(cord.x, cord.y, cord.z);
                if (gen instanceof TileGenerator) {
                    ((TileGenerator) gen).reset();
                }

                ItemHelper.spawnItem(world, x, y + 1, z, ((TileRotor) tile).getDrop());
                return true;
            } else {
                world.setBlock(x, y, z, Core.metals, MetalMeta.BASE_IRON, 2);
                ItemHelper.spawnItem(world, x, y + 1, z, ((TileRotor) tile).getDrop());
                return true;
            }
        }

        return false;
    }
}
