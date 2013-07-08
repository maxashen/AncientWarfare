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
package shadowmage.ancient_warfare.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class ItemResearchNote extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemResearchNote(int itemID)
  {
  super(itemID, true);
  this.hasLeftClick = false;
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  super.addInformation(stack, player, list, par4);
  list.add("Right Click to learn research.");
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(world.isRemote){return false;}
  player.addChatMessage("Learning research from notes: "+ResearchGoal.getGoalByID(stack.getItemDamage()).getDisplayName());
  PlayerTracker.instance().addResearchToPlayer(world, player.getEntityName(), stack.getItemDamage());
  if(!player.capabilities.isCreativeMode)
    {
    stack.stackSize--;
    if(stack.stackSize<=0)
      {
      player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
      }
    return false;
    }
  return false;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

}