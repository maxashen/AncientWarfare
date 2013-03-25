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
package shadowmage.ancient_warfare.common.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.missiles.MissileBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

/**
 * interfaced used by ammo types, implemented for possible future API use and 
 * ease of future expansion without necessitating extension/inheritance
 * @author Shadowmage
 */
public interface IAmmoType
{

/**
 * get this ammo types global reference/ID number (used by vehicles to determine usability)
 * @return
 */
int getAmmoType();//the global unique ammo type, used by structure spawning to fill ammo bays
int getEntityDamage();
int getVehicleDamage();

String getDisplayName();//the displayed item-name/ammo name for this ammo
String getDisplayTooltip();//the display tooltip for this ammo
String getModelTexture();//get the display texture
ItemStack getDisplayStack();//should be a persistent stack in the ammo instance, used to display ammo...
ItemStack getAmmoStack(int qty);//used to create a stack of this ammo.  used in structure spawning

boolean isFlaming();//used by client-side rendering to render the missile on-fire, does nothing else
boolean isAmmoValidFor(VehicleBase vehicle);//can be used for per-upgrade compatibility.  vehicle will check this before firing or adding ammo to the vehicle
boolean updateAsArrow();//should update pitch like an arrow (relative to flight direction)
boolean isRocket();//determines flight characteristics
boolean isPersistent();//should die on impact, or stay on ground(arrows)
boolean isPenetrating();//if persistent, and penetrating==true, will not bounce off of stuff, but instead go through it (heavy projectiles)
float getGravityFactor();//statically set..should techincally be depricated in favor of a const
float getAmmoWeight();//weight of the missile in KG
float getRenderScale();//get relative render scale of the ammo compared to the model default scale...(varies per ammo/model)

void onImpactWorld(World world, float x, float y, float z, MissileBase missile);//called when the entity impacts a world block
void onImpactEntity(World world, Entity ent, float x, float y, float z, MissileBase missile);//called when the entity impacts another entity

}
