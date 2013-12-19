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
package shadowmage.ancient_structures.common.template.plugin.default_plugins;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.IDPairCount;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.block.BlockDataManager;
import shadowmage.ancient_structures.common.structures.data.BlockInfo;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleBlock;

public class TemplateRuleVanillaBlocks extends TemplateRuleBlock
{

String blockName;
int meta;

/**
 * constructor for dynamic construction.  passed world and coords so that the rule can handle its own logic internally
 * @param world
 * @param x
 * @param y
 * @param z
 * @param block
 * @param meta
 */
public TemplateRuleVanillaBlocks(World world, int x, int y, int z, Block block, int meta, int turns)
  {
  super(world, x, y, z, block, meta, turns);
  this.blockName = block.getUnlocalizedName();
  this.meta = BlockDataManager.getRotatedMeta(block, meta, turns);
  }

public TemplateRuleVanillaBlocks()
  {  
  
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  Block block = BlockDataManager.getBlockByName(blockName);
  int localMeta = BlockDataManager.getRotatedMeta(block, this.meta, turns);  
  world.setBlock(x, y, z, block.blockID, localMeta, 2);//using flag=2 -- no block update, but send still send to clients (should help with issues of things popping off)
  }

@Override
public void writeRuleData(BufferedWriter out) throws IOException
  {
  out.write("blockName="+this.blockName);
  out.newLine();
  out.write("meta="+this.meta);
  out.newLine();
  }
  
@Override
public boolean shouldReuseRule(World world, Block block, int meta, int turns, TileEntity te, int x, int y, int z)
  {
  return block!=null && blockName.equals(block.getUnlocalizedName()) && BlockDataManager.getRotatedMeta(block, meta, turns) == this.meta;
  }

@Override
public void parseRuleData(List<String> ruleData)
  {
  if(ruleData.size()<2){throw new IllegalArgumentException("not enough data for block rule");}
  this.blockName = StringTools.safeParseString("=", ruleData.get(0));
  this.meta = StringTools.safeParseInt("=", ruleData.get(1));
  }

@Override
public void addResources(List<ItemStack> resources)
  {
  Block block = BlockDataManager.getBlockByName(blockName);
  IDPairCount count = BlockInfo.getInventoryBlock(block.blockID, meta);
  if(count!=null && count.id>0)
    {
    resources.add(new ItemStack(count.id, count.count, count.meta));    
    }
  }

}