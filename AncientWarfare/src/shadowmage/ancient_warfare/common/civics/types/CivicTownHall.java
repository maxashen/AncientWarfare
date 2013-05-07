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
package shadowmage.ancient_warfare.common.civics.types;

import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.civics.TECivic;

public class CivicTownHall extends Civic
{

/**
 * @param id
 */
public CivicTownHall(int id, String name, String tooltip, int inventorySize, Class<? extends TECivic> teClass)
  {
  super(id);
  this.name = name;
  this.tooltip = tooltip;
  this.isDepository = true;
  this.isWorkSite = false;
  this.isDwelling = false;
  this.maxWorkers = 0;
  this.workSizeMaxHeight = 0;
  this.workSizeMaxHorizontal = 0;
  this.workType = CivicWorkType.UPKEEP;
  this.inventorySize = inventorySize;
  this.teClass = teClass;
  }

}