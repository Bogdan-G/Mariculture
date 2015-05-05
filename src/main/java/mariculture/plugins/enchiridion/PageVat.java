package mariculture.plugins.enchiridion;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

import enchiridion.api.DisplayRegistry;
import enchiridion.api.Formatting;
import enchiridion.api.XMLHelper;
import enchiridion.api.pages.PageParser;

public class PageVat extends PageParser {
    String input, fluid1, fluid2, fluid3, output, colorIn, colorOut, fluid1Type, fluid2Type, fluid3Type;
    int numInput, vol1, vol2, vol3, numOutput, time;

    @Override
    public void read(Element xml) {
        input = XMLHelper.getElement(xml, "input");
        fluid1 = XMLHelper.getElement(xml, "fluid1");
        fluid2 = XMLHelper.getElement(xml, "fluid2");
        fluid3 = XMLHelper.getElement(xml, "fluid3");
        output = XMLHelper.getElement(xml, "output");

        if (!input.equals("")) {
            Element e = XMLHelper.getNode(xml, "input");
            colorIn = Formatting.getColor(XMLHelper.getAttribute(e, "color"));
            numInput = XMLHelper.getAttribAsInteger(e, "num", 1);
        }

        if (!output.equals("")) {
            Element e = XMLHelper.getNode(xml, "output");
            colorOut = Formatting.getColor(XMLHelper.getAttribute(e, "color"));
            numOutput = XMLHelper.getAttribAsInteger(e, "num", 1);
        }

        if (!fluid1.equals("")) {
            Element e = XMLHelper.getNode(xml, "fluid1");
            vol1 = XMLHelper.getAttribAsInteger(e, "vol", 1000);
            fluid1Type = XMLHelper.getAttribute(e, "type").equals("") ? "mB" : " " + XMLHelper.getAttribute(e, "type");
        }

        if (!fluid2.equals("")) {
            Element e = XMLHelper.getNode(xml, "fluid2");
            vol2 = XMLHelper.getAttribAsInteger(e, "vol", 1000);
            fluid2Type = XMLHelper.getAttribute(e, "type").equals("") ? "mB" : " " + XMLHelper.getAttribute(e, "type");
        }

        if (!fluid3.equals("")) {
            Element e = XMLHelper.getNode(xml, "fluid3");
            vol3 = XMLHelper.getAttribAsInteger(e, "vol", 1000);
            fluid3Type = XMLHelper.getAttribute(e, "type").equals("") ? "mB" : " " + XMLHelper.getAttribute(e, "type");
        }

        time = XMLHelper.getElementAsInteger(xml, "time", 10);
    }

    @Override
    public void parse() {
        gui.getMC().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        /** Drawing the Tank on the left **/

        //Fluid 1
        if (!fluid1.equals("")) {
            IIcon icon = DisplayRegistry.getFluidIcon(fluid1);
            drawFluidStack(x + 6, y + 28, icon, 7, 16);
            drawFluidStack(x + 13, y + 28, icon, 16, 16);
            drawFluidStack(x + 6, y + 12, icon, 7, 16);
            drawFluidStack(x + 13, y + 12, icon, 16, 16);

            if (!fluid2.equals("")) {
                icon = DisplayRegistry.getFluidIcon(fluid2);
            }

            drawFluidStack(x + 29, y + 28, icon, 16, 16);
            drawFluidStack(x + 45, y + 28, icon, 7, 16);
            drawFluidStack(x + 29, y + 12, icon, 16, 16);
            drawFluidStack(x + 45, y + 12, icon, 7, 16);
        }

        if (!fluid3.equals("")) {
            IIcon icon = DisplayRegistry.getFluidIcon(fluid3);
            drawFluidStack(x + 97, y + 28, icon, 16, 16);
            drawFluidStack(x + 90, y + 28, icon, 7, 16);
            drawFluidStack(x + 113, y + 28, icon, 16, 16);
            drawFluidStack(x + 129, y + 28, icon, 7, 16);
            drawFluidStack(x + 97, y + 12, icon, 16, 16);
            drawFluidStack(x + 90, y + 12, icon, 7, 16);
            drawFluidStack(x + 113, y + 12, icon, 16, 16);
            drawFluidStack(x + 129, y + 12, icon, 7, 16);
        }

        /** End Left Tank Begin Right Tank **/

        gui.getMC().getTextureManager().bindTexture(elements);
        gui.drawTexturedModalRect(x, y, 58, 0, 58, 62);
        gui.drawTexturedModalRect(x + 84, y, 58, 0, 58, 62);
        gui.drawTexturedModalRect(x + 62, y + 20, 2, 63, 22, 14);

        if (!input.equals("")) {
            drawItemStack(DisplayRegistry.getIcon(input), x + 20, y + 20);
            gui.getMC().fontRenderer.drawString(colorIn + "x" + numInput, x + 36, y + 27, 4210752);
        }

        if (!output.equals("")) {
            drawItemStack(DisplayRegistry.getIcon(output), x + 104, y + 20);
            gui.getMC().fontRenderer.drawString(colorOut + "x" + numOutput, x + 120, y + 27, 4210752);
        }

        if (!fluid1.equals("")) {
            int xPlus = fluid2.equals("") ? 12 : 10;
            if (vol1 > 9999) {
                xPlus -= 3;
            }
            if (!fluid1Type.equals("mB")) {
                xPlus -= 2;
            }
            if (!fluid2.equals("")) {
                if (fluid2Type == null) {
                    fluid2Type = fluid1Type;
                }
                if (!fluid2Type.equals("mB")) {
                    xPlus -= 2;
                }
                gui.getMC().fontRenderer.drawString("" + vol1 + fluid1Type, x + xPlus - 25, y - 10, 4210752);
                gui.getMC().fontRenderer.drawString("" + vol2 + fluid2Type, x + xPlus + 30, y - 10, 4210752);
                gui.getMC().fontRenderer.drawString("+", x + xPlus + 20, y - 10, 4210752);
            } else {
                gui.getMC().fontRenderer.drawString("" + vol1 + fluid1Type, x + xPlus, y + 2, 4210752);
            }
        }

        if (!fluid3.equals("")) {
            int xPlus = 0;
            if (!fluid3Type.equals("mB")) {
                xPlus += 2;
            }
            gui.getMC().fontRenderer.drawString("" + vol3 + fluid3Type, x + 92 + xPlus, y + 2, 4210752);
        }

        gui.getMC().fontRenderer.drawString("" + time + "s", x + 62, y + 11, 4210752);
    }
}