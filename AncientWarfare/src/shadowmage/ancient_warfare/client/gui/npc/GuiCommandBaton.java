/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
   Please see COPYING for precise license information.

   This file is part of Ancient Warfare.

   Ancient Warfare is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Ancient Warfare is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadowmage.ancient_warfare.client.gui.npc;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.container.ContainerCommandBaton;
import shadowmage.ancient_warfare.common.item.ItemNpcCommandBaton;
import shadowmage.ancient_warfare.common.npcs.commands.NpcCommand;

public class GuiCommandBaton extends GuiContainerAdvanced
{

ContainerCommandBaton container;
NpcCommand[] batonCommands;
GuiNumberInputLine rangeBox;

GuiScrollableArea controlArea;
/**
 * @param container
 */
public GuiCommandBaton(Container container)
  {
  super(container);
  this.container = (ContainerCommandBaton)container;  
  this.shouldCloseOnVanillaKeys = true;
  this.batonCommands = ItemNpcCommandBaton.getApplicableCommands(player.getCurrentEquippedItem());
  }


@Override
public int getXSize()
  {
  return 256;
  }

@Override
public int getYSize()
  {
  return 240;
  }

@Override
public String getGuiBackGroundTexture()
  {
  return "/shadowmage/ancient_warfare/resources/gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawStringGui("Has Entity Assigned "+container.settings.hasEntity(), 10, 10, 0xffffffff);
  this.drawStringGui("Current Command: "+container.settings.command.getCommandName(), 10, 20, 0xffffffff);
  if(container.batonRank>0)
    {
    this.drawStringGui("Range:", 10, 30, 0xffffffff);
    }
  }

@Override
public void updateScreenContents()
  {
  this.controlArea.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  int id = element.getElementNumber();
  if(id==0)
    {
    this.closeGUI();
    }
//  else if (id==1)
//    {
//    this.container.settings.range = rangeBox.getIntVal();
//    this.container.saveSettings();
//    }
  else if(id >= 10 && id < this.batonCommands.length+10)
    {
    NpcCommand cmd = this.batonCommands[element.getElementNumber()-10];
    this.container.settings.command = cmd;
    this.container.saveSettings();
    }  
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 45, 12, "Done").updateRenderPos(getXSize()-45-5, 5);
//  if(container.batonRank>0)
//    {
//    rangeBox = (GuiNumberInputLine) this.addNumberField(1, 35, 12, 3, "0").setIntegerValue().setMinMax(0, 140).updateRenderPos(50, 30);
//    }
  this.controlArea = new GuiScrollableArea(1, this, 5, 50, 256-10, 240-55, this.batonCommands.length*14);
  this.guiElements.put(1, controlArea);
  for(int i = 0; i < this.batonCommands.length; i ++)
    {
    controlArea.addGuiElement(new GuiButtonSimple(i+10, controlArea, 140, 12, this.batonCommands[i].getCommandName()).updateRenderPos(5, i * 14));
//    this.addGuiButton(i+10, 10, 40 + i*14, 140, 12, this.batonCommands[i].getCommandName());
    }  
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
