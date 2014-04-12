package shadowmage.ancient_structures.client.gui.elements;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import shadowmage.ancient_structures.client.gui.GuiContainerBase;
import shadowmage.ancient_structures.client.gui.GuiContainerBase.ActivationEvent;
import shadowmage.ancient_structures.client.gui.Listener;
import shadowmage.ancient_structures.client.render.RenderTools;


/**
 * base class for gui elements that contain other elements
 * E.G. Scrollable area, Tabbed area, Multi-button controls
 * 
 * @author John
 *
 */
public class Composite extends GuiElement
{

protected List<GuiElement> elements = new ArrayList<GuiElement>();

public Composite(int topLeftX, int topLeftY, int width, int height)
  {
  super(topLeftX, topLeftY, width, height); 
  addDefaultListeners();
  this.keyboardInterface = true;
  this.mouseInterface = true;
  }

/**
 * sub-classes should override this method to add their custom default listeners. * 
 */
protected void addDefaultListeners()
  {
  this.addNewListener(new Listener(Listener.ALL_EVENTS)
    {
    @Override
    public boolean onEvent(GuiElement widget, ActivationEvent evt)
      {
      if((evt.type & Listener.KEY_TYPES) != 0)
        {
        for(GuiElement element : elements)
          {
          element.handleKeyboardInput(evt);
          }
        }
      else if((evt.type & Listener.MOUSE_TYPES) != 0)
        {
        if(isMouseOverElement(evt.mx, evt.my))
          {
          /**
           * adjust mouse event position for relative to composite
           */
          int x = evt.mx;
          int y = evt.my;
          for(GuiElement element : elements)
            {
            element.handleMouseInput(evt);
            }
          evt.mx = x;
          evt.my = y;
          }
        else if(evt.type==Listener.MOUSE_UP)
          {
          for(GuiElement element : elements)
            {
            element.setSelected(false);
            }
          }
        }
      return true;
      }
    });
  }

public void clearElements()
  {
  this.elements.clear();
  }

@Override
public void render(int mouseX, int mouseY, float partialTick)
  {
  if(!isMouseOverElement(mouseX, mouseY))
    {
    mouseX = Integer.MIN_VALUE;
    mouseY = Integer.MIN_VALUE;
    }
  else
    {
    }    
  setViewport();
  Minecraft.getMinecraft().renderEngine.bindTexture(backgroundTextureLocation);
  RenderTools.renderQuarteredTexture(256, 256, 0, 0, 256, 240, renderX, renderY, width, height);
  for(GuiElement element : this.elements)
    {
    element.render(mouseX, mouseY, partialTick);
    }    
  popViewport();
  }

public void addGuiElement(GuiElement element)
  {
  this.elements.add(element);
  }

protected void setViewport()
  {    
  int x, y, w, h;
  x = renderX+3;
  y = renderY+3;
  w = width-6;
  h = height-6;  
  GuiContainerBase.pushViewport(x, y, w, h);  
  }

protected void popViewport()
  {
  GuiContainerBase.popViewport();
  }

/**
 * sub-classes of Composite should override this method
 * to feed their current scrolled position into their elements
 * (e.g. scrollbar composite will use scrollbar pos)
 */
@Override
public void updateGuiPosition(int guiLeft, int guiTop)
  {
  super.updateGuiPosition(guiLeft, guiTop);
  updateElementPositions();
  }

protected void updateElementPositions()
  {
  for(GuiElement element : this.elements)
    {
    element.updateGuiPosition(renderX, renderY);
    }
  }

public void setSize(int width, int height)
  {
  this.width = width;
  this.height = height;
  }


}
