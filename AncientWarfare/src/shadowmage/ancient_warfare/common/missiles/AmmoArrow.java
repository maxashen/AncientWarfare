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
package shadowmage.ancient_warfare.common.missiles;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class AmmoArrow extends AmmoBase
{

public AmmoArrow(int ammoType)
  {
  super(ammoType);
  }

@Override
public String getEntityName()
  {
  return "AWArrow";
  }

@Override
public String getDisplayName()
  {
  return "Arrow";
  }

@Override
public String getDisplayTooltip()
  {
  return "Arrow";
  }

@Override
public String getModelTexture()
  {
  return "foo.png";
  }

@Override
public int getItemID()
  {
  return 0;
  }

@Override
public int getItemMeta()
  {
  return 0;
  }

@Override
public boolean isRocket()
  {
  return false;
  }

@Override
public boolean isPersistent()
  {
  return true;
  }

@Override
public float getDragFactor()
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public float getWeightFactor()
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public void onImpactWorld(World world, float x, float y, float z)
  {
  // TODO Auto-generated method stub
  }

@Override
public void onImpactEntity(World world, Entity ent, float x, float y, float z)
  {
  // TODO Auto-generated method stub
  }

}
