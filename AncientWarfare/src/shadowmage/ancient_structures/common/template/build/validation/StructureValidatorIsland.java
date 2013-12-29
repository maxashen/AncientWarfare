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
package shadowmage.ancient_structures.common.template.build.validation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;

public class StructureValidatorIsland extends StructureValidator
{

int minWaterDepth;
int maxWaterDepth;
Set<String> validTargetBlocks;

public StructureValidatorIsland()
  {
  super(StructureValidationType.ISLAND);
  minWaterDepth = 1;
  maxWaterDepth = 40;
  validTargetBlocks = new HashSet<String>();
  }

@Override
protected void readFromLines(List<String> lines)
  {
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("minwaterdepth=")){minWaterDepth = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("maxwaterdepth=")){maxWaterDepth = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("validtargetblocks=")){StringTools.safeParseStringsToSet(validTargetBlocks, "=", line, false);}
    }
  }

@Override
protected void write(BufferedWriter out) throws IOException
  {
  out.write("minWaterDepth="+minWaterDepth);
  out.newLine();
  out.write("minWaterDepth="+maxWaterDepth);
  out.newLine();  
  out.write("validTargetBlocks="+StringTools.getCSVfor(validTargetBlocks));
  out.newLine();
  }

@Override
protected void setDefaultSettings(StructureTemplate template)
  {
  this.validTargetBlocks.addAll(WorldStructureGenerator.defaultTargetBlocks);
  this.minWaterDepth = template.yOffset/2;
  this.maxWaterDepth = template.yOffset;      
  }

@Override
public boolean shouldIncludeForSelection(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  int id;
  int water = 0;
  int startY = y-1;
  y = WorldStructureGenerator.getSeaFloorHeight(world, x, z, startY)+1;
  water = startY-y+1;
  if(water<minWaterDepth || water>maxWaterDepth)
    {   
    return false;
    }
  return true;
  }

@Override
public int getAdjustedSpawnY(World world, int x, int y, int z, int face,  StructureTemplate template, StructureBB bb)
  {
  return y;
  }

@Override
public boolean validatePlacement(World world, int x, int y, int z, int face,  StructureTemplate template, StructureBB bb)
  {
  int bx, bz;    
  for(bx = bb.min.x; bx<=bb.max.x; bx++)
    {
    bz = bb.min.z;
    if(!validateBlock(world, bx, bz, template, bb)){return false;}
    
    bz = bb.max.z;
    if(!validateBlock(world, bx, bz, template, bb)){return false;}    
    }
  for(bz = bb.min.z+1; bz<=bb.max.z-1; bz++)
    {
    bx = bb.min.x;
    if(!validateBlock(world, bx, bz, template, bb)){return false;}
    
    bx = bb.max.x;
    if(!validateBlock(world, bx, bz, template, bb)){return false;}    
    }
  return true;
  }

private boolean validateBlock(World world, int x, int z, StructureTemplate template, StructureBB bb)
  {
  int lowestBlock = bb.min.y+template.yOffset - maxWaterDepth;
  int highestBlock = bb.min.y+template.yOffset - minWaterDepth;  
  int seaFloorHeight = WorldStructureGenerator.getSeaFloorHeight(world, x, z, bb.max.y);  
  if(seaFloorHeight<lowestBlock || seaFloorHeight>highestBlock)
    {
    return false;
    }
  Block block = Block.blocksList[world.getBlockId(x, seaFloorHeight, z)];
  if(block==null || !validTargetBlocks.contains(block.getUnlocalizedName()))
    {
    return false;
    }
  return true;
  }

@Override
public void preGeneration(World world, int x, int y, int z, int face,  StructureTemplate template, StructureBB bb)
  {
  int id;  
  Block block;
  for(int bx = bb.min.x; bx<=bb.max.x; bx++)
    {
    for(int bz = bb.min.z; bz<=bb.max.z; bz++)
      {
      for(int by = bb.min.y-1; by>0; by--)
        {        
        block = Block.blocksList[world.getBlockId(bx, by, bz)];
        if(block!=null && validTargetBlocks.contains(block.getUnlocalizedName()))
          {
          break;
          }
        else
          {
          world.setBlock(bx, by, bz, Block.dirt.blockID);
          }        
        }
      }
    }
  }


}
