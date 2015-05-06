package mariculture.core.items;

import java.util.List;

import mariculture.Mariculture;
import mariculture.core.Core;
import mariculture.core.helpers.BlockHelper;
import mariculture.core.helpers.FluidHelper;
import mariculture.core.network.PacketHandler;
import mariculture.core.util.ITank;
import mariculture.lib.util.Text;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFluidStorage extends ItemMCBaseSingle implements IFluidContainerItem {
    public int capacity;
    private IIcon filledIcon;

    public ItemFluidStorage(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(world, player, true);
        if (movingobjectposition == null) return item;
        else if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
            IFluidContainerItem container = (IFluidContainerItem) item.getItem();
            int x = movingobjectposition.blockX;
            int y = movingobjectposition.blockY;
            int z = movingobjectposition.blockZ;
            int side = movingobjectposition.sideHit;

            Block block = world.getBlock(x, y, z);
            if (block instanceof BlockFluidBase || block instanceof BlockLiquid) {
                FluidStack fluid = null;
                if (block instanceof BlockFluidBase) {
                    fluid = ((BlockFluidBase) block).drain(world, x, y, z, false);
                }

                if (BlockHelper.isWater(world, x, y, z)) {
                    fluid = FluidRegistry.getFluidStack("water", 1000);
                }

                if (BlockHelper.isLava(world, x, y, z)) {
                    fluid = FluidRegistry.getFluidStack("lava", 1000);
                }

                if (fluid != null) {
                    if (container.fill(item, fluid, false) >= fluid.amount) {
                        if (block instanceof BlockFluidBase) {
                            ((BlockFluidBase) block).drain(world, x, y, z, true);
                        } else {
                            world.setBlockToAir(x, y, z);
                        }

                        container.fill(item, fluid, true);
                        return item;
                    }
                }
            }

            movingobjectposition = getMovingObjectPositionFromPlayer(world, player, false);
            if (movingobjectposition == null) return item;
            x = movingobjectposition.blockX;
            y = movingobjectposition.blockY;
            z = movingobjectposition.blockZ;
            side = movingobjectposition.sideHit;
            
            if (((IFluidContainerItem) item.getItem()).getFluid(item) != null) {
                FluidStack stack = ((IFluidContainerItem) item.getItem()).getFluid(item);
                Fluid fluid = stack.getFluid();
                if (!fluid.canBePlacedInWorld()) return item;

                int drain = FluidHelper.getRequiredVolumeForBlock(fluid);
                FluidStack result = ((IFluidContainerItem) item.getItem()).drain(item, drain, false);
                if (result == null || result.amount < drain) return item;

                if (!result.getFluid().getName().equals("lava") && world.provider.isHellWorld) return item;

                if (side == 1 && (block instanceof BlockFluidBase || block instanceof BlockLiquid)) {
                    --y;
                } else if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) {
                    side = 1;
                } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush) {
                    if (side == 0) {
                        --y;
                    }

                    if (side == 1) {
                        ++y;
                    }

                    if (side == 2) {
                        --z;
                    }

                    if (side == 3) {
                        ++z;
                    }

                    if (side == 4) {
                        --x;
                    }

                    if (side == 5) {
                        ++x;
                    }
                }

                if (!player.canPlayerEdit(x, y, z, side, item)) return item;
                else if (item.stackSize == 0) return item;
                else {
                    Block theBlock = fluid.getBlock();
                    if (world.setBlock(x, y, z, theBlock, 0, 2)) {
                        if (world.getBlock(x, y, z) == theBlock) {
                            theBlock.onBlockPlacedBy(world, x, y, z, player, item);
                            theBlock.onPostBlockPlaced(world, x, y, z, 0);
                        }

                        world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, theBlock.stepSound.func_150496_b(), (theBlock.stepSound.getVolume() + 1.0F) / 2.0F, theBlock.stepSound.getPitch() * 0.8F);

                        ((IFluidContainerItem) item.getItem()).drain(item, drain, true);
                        return item;
                    }
                }
            }
        }

        return item;
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (FluidHelper.isIContainer(item)) if (world.getTileEntity(x, y, z) instanceof IFluidHandler) {
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            IFluidHandler tank = (IFluidHandler) world.getTileEntity(x, y, z);
            IFluidContainerItem container = (IFluidContainerItem) item.getItem();
            FluidStack fluid = container.getFluid(item);
            if (fluid != null && fluid.amount > 0) {
                FluidStack stack = container.drain(item, 1000000, false);
                if (stack != null) {
                    stack.amount = tank.fill(dir, stack, false);
                    if (stack.amount > 0) {
                        container.drain(item, stack.amount, true);
                        tank.fill(dir, stack, true);
                        if (tank instanceof ITank && !world.isRemote) {
                            PacketHandler.syncFluids(world.getTileEntity(x, y, z), ((ITank) tank).getFluid());
                        }

                        return true;
                    }
                }
            } else {
                FluidStack stack = tank.drain(dir, capacity, false);
                if (stack != null && stack.amount > 0) {
                    tank.drain(dir, stack.amount, true);
                    container.fill(item, stack, true);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.ORANGE + super.getItemStackDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        FluidStack fluid = getFluid(stack);
        int amount = fluid == null ? 0 : fluid.amount;
        list.add(FluidHelper.getFluidName(fluid));
        list.add("" + amount + "/" + capacity + "mB");
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if (stack.getItem() == Core.bucketTitanium) {
            if (pass == 0) {
                if (stack.hasTagCompound() && getFluid(stack) != null) {
                    FluidStack fake = getFluid(stack).copy();
                    fake.amount = OreDictionary.WILDCARD_VALUE;
                    ItemStack bucket = FluidContainerRegistry.fillFluidContainer(fake, new ItemStack(Items.bucket));
                    if (bucket != null && bucket.getIconIndex() != null) return bucket.getIconIndex();
                }

                return itemIcon;
            } else return filledIcon;
        }

        return itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        if (this == Core.bucketTitanium) {
            String name = super.getUnlocalizedName().replace("item.", "").toLowerCase();
            filledIcon = iconRegister.registerIcon(Mariculture.modid + ":bucketTitaniumFilled");
        }
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return capacity;
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid")) return null;
        return FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (resource == null) return 0;

        if (!doFill) {
            if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid")) return Math.min(capacity, resource.amount);

            FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));

            if (stack == null) return Math.min(capacity, resource.amount);

            if (!stack.isFluidEqual(resource) && stack.amount > 0) return 0;

            return Math.min(capacity - stack.amount, resource.amount);
        }

        if (container.stackTagCompound == null) {
            container.stackTagCompound = new NBTTagCompound();
        }

        if (!container.stackTagCompound.hasKey("Fluid")) {
            NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

            if (capacity < resource.amount) {
                fluidTag.setInteger("Amount", capacity);
                container.stackTagCompound.setTag("Fluid", fluidTag);
                return capacity;
            }

            container.stackTagCompound.setTag("Fluid", fluidTag);
            return resource.amount;
        }

        NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
        FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

        if (!stack.isFluidEqual(resource) && stack.amount > 0) return 0;

        if (!stack.isFluidEqual(resource)) {
            stack = new FluidStack(resource.getFluid(), stack.amount);
        }

        int filled = capacity - stack.amount;
        if (resource.amount < filled) {
            stack.amount += resource.amount;
            filled = resource.amount;
        } else {
            stack.amount = capacity;
        }

        container.stackTagCompound.setTag("Fluid", stack.writeToNBT(fluidTag));
        return filled;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid")) return null;

        FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
        if (stack == null) return null;

        stack.amount = Math.min(stack.amount, maxDrain);
        if (doDrain) {
            NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
            int newAmount = fluidTag.getInteger("Amount") - stack.amount;
            if (newAmount == 0) {
                container.setTagCompound(null);
                return stack;
            }

            fluidTag.setInteger("Amount", newAmount);
            container.stackTagCompound.setTag("Fluid", fluidTag);
        }

        return stack;
    }
}
