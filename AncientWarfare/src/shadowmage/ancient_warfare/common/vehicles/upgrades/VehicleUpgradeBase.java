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
package shadowmage.ancient_warfare.common.vehicles.upgrades;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public abstract class VehicleUpgradeBase implements IVehicleUpgradeType
{

int typeNum = 0;
String displayName = "";
String tooltip = "";
String iconTexture = "foo";

public VehicleUpgradeBase(int num)
  {
  this.typeNum = num;
  }

@Override
public int getUpgradeGlobalTypeNum()
  {
  return typeNum;
  }

@Override
public String getDisplayName()
  {
  return displayName;
  }

@Override
public String getDisplayTooltip()
  {
  return tooltip;
  }

@Override
public ItemStack getUpgradeStack(int qty)
  {
  return new ItemStack(ItemLoader.vehicleUpgrade.itemID, qty, this.typeNum);
  }

@Override
public String getIconTexture()
  {
  return "ancientwarfare:upgrade/"+iconTexture;
  }
}
