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
package shadowmage.ancient_warfare.client.gui.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiElement;
import shadowmage.ancient_warfare.client.gui.elements.GuiItemStack;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.gui.info.GuiRecipeDetails;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerCivilEngineering;
import shadowmage.ancient_warfare.common.container.ContainerEngineeringStation;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterAZ;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterTextFilter;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class GuiCivilEngineering extends GuiContainerAdvanced
{

GuiTab activeTab = null;
HashSet<GuiTab> tabs = new HashSet<GuiTab>();
HashMap<GuiButtonSimple, ResourceListRecipe> recipes = new HashMap<GuiButtonSimple, ResourceListRecipe>();
GuiScrollableArea area;
GuiScrollableArea area2;
int buttonWidth = 176-10-12-10;
ContainerCivilEngineering container;
RecipeSorterAZ sorterAZ = new RecipeSorterAZ();
RecipeSorterTextFilter sorterFilter = new RecipeSorterTextFilter();
GuiTextInputLine searchBox;
ResourceListRecipe currentRecipe;

/**
 * @param container
 */
public GuiCivilEngineering(Container container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  this.container = (ContainerCivilEngineering)container;
  }

@Override
public int getXSize()
  {
  return 176;
  }

@Override
public int getYSize()
  {
  return 240;
  }

@Override
protected void renderBackgroundImage(String tex)
  {
  if(tex!=null)
    {
    RenderTools.drawQuadedTexture(guiLeft, guiTop+21, this.xSize, this.ySize-21, 256, 240, tex, 0, 0);
    }
  }

@Override
public String getGuiBackGroundTexture()
  {
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  switch(activeTab.getElementNumber())
    {
    case 1001:
    break;
    case 1000:
    case 1002:
    this.drawCurrentRecipeBackground();
    break;
    }
  }

public void drawCurrentRecipeBackground()
  {
	/**
	 * todo..move to foreground rendering
	 */
  if(this.container.currentRecipe!=null)
    {
    this.drawStringGui(this.container.currentRecipe.getDisplayName(), 8+18+2, 24+3+4, 0xffffffff);
    this.renderItemStack(this.container.currentRecipe.getResult(), guiLeft+8, guiTop+24+3, mouseX, mouseY, true);
    }
  else if(this.currentRecipe!=null)
    {
    this.drawStringGui(this.currentRecipe.getDisplayName(), 8+18+2, 24+3+4, 0xffffffff);
    this.renderItemStack(this.currentRecipe.getResult(), guiLeft+8, guiTop+24+3, mouseX, mouseY, true);
    }
  }

@Override
public void updateScreenContents()
  {
  area.updateGuiPos(guiLeft, guiTop);
  area2.updateGuiPos(guiLeft, guiTop);
  if(this.currentRecipe!=this.container.currentRecipe)
    {
    if(this.container.currentRecipe!=null)
      {
      this.currentRecipe = this.container.currentRecipe;
      this.forceUpdate = true;
      }
    }
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(this.tabs.contains(element))
    {
    GuiTab selected = (GuiTab) element;
    for(GuiTab tab : this.tabs)
      {
      tab.enabled = false;
      }
    selected.enabled = true;
    this.activeTab = selected;
    this.forceUpdate = true;
    this.searchBox.selected = false;
    }
  switch(activeTab.getElementNumber())
  {
  case 1000:
  case 1001:
  if(element==this.searchBox)
    {
    this.handleSearchBoxUpdate();
    }
  else if(recipes.containsKey(element) && container.currentRecipe==null)
    {
    this.handleRecipeClick(element);
    }
  break;
  
  case 1002:
  if(element.getElementNumber()==1 && this.container.isWorking)//clear
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("stop", true);
    this.sendDataToServer(tag);
    }
  else if(element.getElementNumber()==3 && this.currentRecipe!=null && !this.container.isWorking)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("set", this.currentRecipe.getDisplayName());
    this.sendDataToServer(tag);
    }
  break;
  }
  }

protected void handleRecipeClick(IGuiElement element)
  {  
  if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
    {
    mc.displayGuiScreen(new GuiRecipeDetails(this, recipes.get(element)));      
    }
  else
    {
    if(container.isWorking)
      {
      return;
      }
    this.currentRecipe = recipes.get(element);   
    }
  }

@Override
public void setupControls()
  {
  GuiTab tab = this.addGuiTab(1000, 5+60+60, 0, 40, 24, "Select");
  this.tabs.add(tab);
  tab.enabled = false;
  tab = this.addGuiTab(1001, 5+60, 0, 60, 24, "Search");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(1002, 5, 0, 60, 24, "Progress");
  this.tabs.add(tab);
  this.activeTab = tab;
  
  
  this.searchBox = (GuiTextInputLine) new GuiTextInputLine(2, this, 160, 12, 30, "").updateRenderPos(5, 24);
  searchBox.selected = false;
  this.area = new GuiScrollableArea(0, this, 5, 21+18+10+5, 176-10, 240-21-10-18-5-8, 0);
  int x = 134;
  int y = 26; 
  int w = 37;
  int h = 124;
  this.area2 = new GuiScrollableArea(4, this, x, y, w, h, 0);
  }

@Override
public void updateControls()
  {
  this.guiElements.clear();
  this.area.elements.clear();
  this.recipes.clear();
  for(GuiTab tab : this.tabs)
    {
    this.guiElements.put(tab.getElementNumber(), tab);
    }
  this.container.removeSlots();
  if(this.activeTab!=null)
    {
    switch(activeTab.getElementNumber())
    {
    case 1000://select
    this.addRecipeButtons(AWCraftingManager.instance().getStructureRecipesClient(), sorterAZ);
    break;
    
    case 1001://search
    this.guiElements.put(2, searchBox);    
    this.handleSearchBoxUpdate();
    break;
    
    case 1002://progress    
    container.addSlots();
    this.addRecipeMaterialList();
    int x =  8;
    int y =  21 + 4 + 4*18 + 8;
    this.addGuiButton(3, 35, 16, "Start").updateRenderPos(x, y);
    this.addGuiButton(1, 35, 16, "Clear").updateRenderPos(x+37, y);  
    break;
       
    }
    }  
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }
  
  }

protected void addRecipeMaterialList()
  {
  this.guiElements.put(4, area2);
  area2.elements.clear();
  if(this.currentRecipe!=null)
    {
    GuiItemStack element;
    ItemStack stack;
    int y = 0;
    for(ItemStackWrapperCrafting item : this.currentRecipe.getResourceList())
      {
      stack = item.getFilter().copy();
      stack.stackSize = item.getRemainingNeeded();
      element = new GuiItemStack(9000, this, 0 ,y).setItemStack(stack);  
      element.renderTooltip = false;
      area2.addGuiElement(element);      
      y+=18;
      }
    area2.updateTotalHeight(y);
    }  
  }

protected void handleSearchBoxUpdate()
  {  
  if(this.activeTab!=null && this.activeTab.getElementNumber()==1001)
    {
    String text = this.searchBox.getText();
    this.recipes.clear();
    this.area.elements.clear();
    this.sorterFilter.setFilterText(text);
    this.addRecipeButtons(AWCraftingManager.instance().getStructureRecipesClient(), sorterFilter);
    }
  }

protected void addRecipeButtons(List<ResourceListRecipe> recipes, Comparator sorter)
  {
  this.guiElements.put(0, area);
  Collections.sort(recipes, sorter);
  area.updateTotalHeight(recipes.size()*18);
  GuiButtonSimple button;
  int x = 0;
  int y = 0;
  ArrayList<String> tooltip = new ArrayList<String>();
  tooltip.add("Hold (shift) while clicking to");
  tooltip.add("view detailed recipe information");
  int num = 100;
  for(ResourceListRecipe recipe : recipes)
    {      
    button = new GuiButtonSimple(num, area, buttonWidth, 16, recipe.getDisplayName());
    button.updateRenderPos(x, y);
    button.setTooltip(tooltip);
    area.addGuiElement(button);
    this.recipes.put(button, recipe);
    y+=18;
    num++;
    }
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  for(Integer i : this.guiElements.keySet())
    {
    GuiElement el = this.guiElements.get(i);
    el.onKeyTyped(par1, par2);
    }
  if(!this.searchBox.selected)
    {
    super.keyTyped(par1, par2);
    }
  else
    {
    this.handleSearchBoxUpdate();
    }
  }
}
