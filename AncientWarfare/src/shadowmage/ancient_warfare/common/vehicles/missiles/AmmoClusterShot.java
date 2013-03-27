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
package shadowmage.ancient_warfare.common.vehicles.missiles;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class AmmoClusterShot extends Ammo
{

/**
 * @param ammoType
 */
public AmmoClusterShot(int ammoType, int weight)
  {
  super(ammoType);    
  this.displayName = "Cluster Shot " + weight +"kg";
  this.displayTooltip = weight+"kg of small ammunitions with an explosive charge.";
  this.ammoWeight = weight;
  }

@Override
public void onImpactWorld(World world, float x, float y, float z, MissileBase missile, MovingObjectPosition hit)
  {   
  double px = hit.hitVec.xCoord - missile.motionX;
  double py = hit.hitVec.yCoord - missile.motionY;
  double pz = hit.hitVec.zCoord - missile.motionZ;
  spawnGroundBurst(world, (float)px, (float)py, (float)pz, 10, Ammo.ammoBallShot, (int)ammoWeight, 35, hit.sideHit);
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile)
  {
  spawnAirBurst(world, (float)ent.posX, (float)ent.posY+ent.height, (float)ent.posZ, 10, Ammo.ammoBallShot, (int)ammoWeight);
  }

}
