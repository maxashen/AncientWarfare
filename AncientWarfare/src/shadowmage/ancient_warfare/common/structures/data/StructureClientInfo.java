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
package shadowmage.ancient_warfare.common.structures.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class StructureClientInfo
{
public final String name;
public final int xSize;
public final int ySize;
public final int zSize;
public final int xOffset;
public final int yOffset;
public final int zOffset;
public final int maxLeveling;
public final int maxClearing;
public final int levelingBuffer;
public final int clearingBuffer;
public boolean creative = true;
public boolean worldGen = false;
public boolean survival = false;

public StructureClientInfo(NBTTagCompound tag)
  {
  this.name = tag.getString("name");
  this.xSize = tag.getShort("x");
  this.ySize = tag.getShort("y");
  this.zSize = tag.getShort("z");
  this.xOffset = tag.getShort("xO");
  this.yOffset = tag.getShort("yO");
  this.zOffset = tag.getShort("zO");
  this.creative = tag.getBoolean("creat");
  this.worldGen = tag.getBoolean("world");
  this.survival = tag.getBoolean("surv");
  this.maxLeveling = tag.getInteger("mL");
  this.maxClearing = tag.getInteger("mC");
  this.levelingBuffer = tag.getInteger("lB");
  this.clearingBuffer = tag.getInteger("cB");
  }

public static NBTTagCompound getClientTag(AWStructure struct)
 {
 NBTTagCompound structTag = new NBTTagCompound();
 if(struct!=null)
   {
   structTag.setString("name", String.valueOf(struct.name));
   structTag.setShort("x", (short)struct.xSize);
   structTag.setShort("y", (short)struct.ySize);
   structTag.setShort("z", (short)struct.zSize);
   structTag.setShort("xO", (short)struct.xOffset);
   structTag.setShort("yO", (short)struct.verticalOffset);
   structTag.setShort("zO", (short)struct.zOffset);
   structTag.setBoolean("creat", struct.creative);
   structTag.setBoolean("world", struct.worldGen);
   structTag.setBoolean("surv", struct.survival);
   structTag.setInteger("mL", struct.maxLeveling);
   structTag.setInteger("mC", struct.maxVerticalClear);
   structTag.setInteger("lB", struct.levelingBuffer);
   structTag.setInteger("cB", struct.clearingBuffer);
   }
 return structTag;
 }

/**
 * returns a world-coordinate bounding box for a given hitPosition
 * and facing
 * @param hit
 * @param face
 * @return
 */
public AxisAlignedBB getBBForRender(BlockPosition hit, int face)
  {
  BlockPosition size = new BlockPosition(xSize, ySize, zSize);
  BlockPosition offset = new BlockPosition (xOffset, yOffset, zOffset);
    
  BlockPosition p1 = hit.copy();
  p1.moveLeft(face, offset.x);
  p1.moveForward(face, offset.z);

  BlockPosition p2 = p1.copy();
  p2.moveRight(face, size.x);
  p2.moveForward(face, size.z);
  p2.y += size.y;
    
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
  return bb;
  }

public AxisAlignedBB getLevelingBBForRender(BlockPosition hit, int face)
  {
  AxisAlignedBB bb = this.getBBForRender(hit, face);
  bb.maxY = bb.minY;
  bb.minY -= maxLeveling;
  
  
  boolean xNorm = bb.minX < bb.maxX;
  boolean zNorm = bb.minZ < bb.maxZ;
  bb.minX = xNorm? bb.minX - levelingBuffer : bb.minX + levelingBuffer;
  bb.minZ = zNorm? bb.minZ - levelingBuffer : bb.minZ + levelingBuffer;
  bb.maxX = xNorm? bb.maxX + levelingBuffer : bb.maxX - levelingBuffer;
  bb.maxZ = zNorm? bb.maxZ + levelingBuffer : bb.maxZ - levelingBuffer;
//  bb.minX -= levelingBuffer;
//  bb.minZ -= levelingBuffer;
//  bb.maxX += levelingBuffer;
//  bb.maxZ += levelingBuffer;
  return bb;
  }

public AxisAlignedBB getClearingBBForRender(BlockPosition hit, int face)
  {
  AxisAlignedBB bb = this.getBBForRender(hit, face);
  boolean xNorm = bb.minX < bb.maxX;
  boolean zNorm = bb.minZ < bb.maxZ;
  bb.minX = xNorm? bb.minX - clearingBuffer : bb.minX + clearingBuffer;
  bb.minZ = zNorm? bb.minZ - clearingBuffer : bb.minZ + clearingBuffer;
  bb.maxX = xNorm? bb.maxX + clearingBuffer : bb.maxX - clearingBuffer;
  bb.maxZ = zNorm? bb.maxZ + clearingBuffer : bb.maxZ - clearingBuffer;  
  bb.maxY += maxClearing;
  return bb;
  }



}
