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
package shadowmage.ancient_warfare.common.vehicles.types;

import shadowmage.ancient_warfare.common.utils.Trig;

public class VehicleTypeCatapultMobileTurret extends VehicleTypeCatapult
{

  /**
   * @param typeNum
   */
  public VehicleTypeCatapultMobileTurret(int typeNum)
    {
    super(typeNum);
    this.width = 2;
    this.height = 2; 
    this.baseMissileVelocityMax = 30.f;  
    this.missileVerticalOffset = 2.70f* Trig.sinDegrees(70) + 0.4f;
    this.missileForwardsOffset = -2.70f* Trig.cosDegrees(70);
    this.riderForwardsOffset = 1.2f;
    this.riderVerticalOffset = 0.8f;
    this.displayName = "Catapult Mobile Turret";
    this.displayTooltip = "A turret-mounted catapult on a wheeled frame.";
    this.storageBaySize = 0;
    this.armorBaySize = 3;
    this.upgradeBaySize = 3;
    this.canAdjustYaw = true;
    this.isDrivable = true;
    this.shouldRiderSit = true;
    this.moveRiderWithTurret = true;
    }

}