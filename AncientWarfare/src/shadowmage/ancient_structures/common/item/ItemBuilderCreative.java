/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
package shadowmage.ancient_structures.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.item.AWItemClickable;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_structures.common.manager.StructureTemplateManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBuilder;


public class ItemBuilderCreative extends AWItemClickable
{

/**
 * @param itemID
 */
public ItemBuilderCreative(int itemID)
  {
  super(itemID);
  this.setCreativeTab(AWStructuresItemLoader.structureTab);
  this.hasLeftClick = true;
  this.setMaxStackSize(1);  
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {  
//  if(!world.isRemote && hit!=null && Statics.DEBUG && player.isSneaking())
//    {
//    int id = world.getBlockId(hit.x, hit.y, hit.z);
//    int meta = world.getBlockMetadata(hit.x, hit.y, hit.z);
//    Block block = Block.blocksList[id];
//    if(block!=null && Statics.DEBUG)
//      {
//      player.addChatMessage("block hit is: "+id+" :: "+meta + " :: "+block.getUnlocalizedName());      
//      }
//    } 
  if(!world.isRemote && !player.isSneaking())
    {
    GUIHandler.instance().openGUI(Statics.guiStructureBuilderCreative, player, 0, 0, 0);    
    return true;
    }    
  return true;
  }


ItemStructureSettings buildSettings = new ItemStructureSettings();
@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(player==null || hit==null || world.isRemote)
    {
    return false;
    }
  buildSettings.getSettingsFor(stack, buildSettings);
  if(buildSettings.hasName())
    {
    StructureTemplate template = StructureTemplateManager.instance().getTemplate(buildSettings.name);
    if(template==null)
      {
      player.addChatMessage("no structure found to build...");
      return true;
      }
    hit.offsetForMCSide(side);
    AWLog.logDebug("constructing template: "+template);    
    StructureBuilder builder = new StructureBuilder(world, template, BlockTools.getPlayerFacingFromYaw(player.rotationYaw), hit.x, hit.y, hit.z);
    builder.instantConstruction();
    }  
  else
    {
    player.addChatMessage("no structure found to build...");
    return true;
    }
  return true;
  }

ItemStructureSettings viewSettings = new ItemStructureSettings();
@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  super.addInformation(stack, player, list, par4);
  String structure = "none";
  viewSettings.getSettingsFor(stack, viewSettings);
  if(viewSettings.hasName())
    {
    structure = viewSettings.name;
    }
  list.add("Current Structure: "+structure);
  }

@Override
public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
  {
  return false;
  }

}
