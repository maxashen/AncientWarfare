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

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class ItemLoader
{

/**
 * Items
 */
public static final AWItemBase vehicleUpgrade = new ItemVehicleUpgrade(Config.getItemID("itemMulti.vehicleUpgrade", 13001, "Base item for all vehicle upgrades"));
public static final AWItemBase ammoItem = new ItemAmmo(Config.getItemID("itemMulti.vehicleAmmo", 13002, "Base item for all vehicle ammunition types"));
public static final AWItemBase vehicleSpawner = new ItemVehicleSpawner(Config.getItemID("itemMulti.vehicleSpawner", 13003, "Base item for all vehicle-spawning items"));
public static final AWItemBase componentItem = new ItemComponent(Config.getItemID("itemMulti.component", 13004, "Base item for all components and misc items"), true);
public static final AWItemBase structureScanner = new ItemStructureScanner(Config.getItemID("itemSingle.structureScanner", 13005, "Item used to scan structures"));
public static final AWItemBase structureCreativeBuilder = new ItemBuilderInstant(Config.getItemID("itemSingle.structureBuilderCreative", 13006, "Creative-Mode Selectable Structure Builder"));
public static final AWItemBase structureBuilderDirect = new ItemBuilderDirect(Config.getItemID("itemSingle.builderDirect", 13007, "Survival mode builder, uses blocks from inventory"));
public static final AWItemBase structureCreativeBuilderTicked = new ItemBuilderTicked(Config.getItemID("itemSingle.structureBuilderCreativeTicked", 13008, "Creative-mode slow (ticked) builder"));
public static final AWItemBase structureEditor = new ItemCreativeEditor(Config.getItemID("itemSingle.templateEditor", 13009, "Creative-mode template editor"));
public static final AWItemBase armorItem = new ItemVehicleArmor(Config.getItemID("itemMulti.vehicleArmor", 13010, "Vehicle Armor Component"));
public static final AWItemBase npcSpawner = new ItemNpcSpawner(Config.getItemID("itemMulti.npcSpawner", 13011, "Npc Spawning Item"));
public static final AWItemBase npcCommandBaton = new ItemNpcCommandBaton(Config.getItemID("itemMulti.commandBaton", 13012, "Npc Command Batons"));
public static final AWItemBase civicPlacer = new ItemCivicPlacer(Config.getItemID("itemMulti.civiPlacer", 13013, "Constructs Civic Buildings/Sites"));

/**
 * debug items, will only be given instances if debug is enabled in Config
 * e.g. will be null unless debug mode is on
 */
public static AWItemBase blockScanner;


private static ItemLoader INSTANCE;
private ItemLoader(){}
public static ItemLoader instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new ItemLoader();
    }
  return INSTANCE;
  }


/**
 * initial load, called during pre-init from mod core file
 */
public void load()
  {
  this.loadItems();
  this.loadRecipes();  
  this.loadDebugItems();
  }

private void loadItems()
  {  
  this.registerItemSingle(structureCreativeBuilderTicked, "Creative Builder Ticked", "Creative Mode Building Tool with Builder Block", "Right-Click to Use, Sneak+Right-Click to open GUI").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(structureScanner, "Structure Scanner", "Structure Scanner", "Structure Scanning Item, Right-Click to Use").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
  this.registerItemSingle(structureCreativeBuilder, "Creative Builder", "Creative Mode Building Tool", "Right-Click to Build, Sneak+Right-Click to open GUI").setIconTexture("ancientwarfare:builder/structureBuilder1", 0);
  this.registerItemSingle(structureBuilderDirect, "Structure Builder Direct", "Survival Mode Quick Building Tool", "Right-Click to Scan, and then Build").setIconTexture("ancientwarfare:builder/structureScanner1", 0);
  this.registerItemSingle(structureEditor, "Structure Editor", "Structure Template Editor", "Right-Click to open editor selection GUI").setIconTexture("ancientwarfare:builder/testIcon1", 0);
  this.registerItemSubtyped(componentItem);
  this.registerItemSubtyped(ammoItem);
  this.registerItemSubtyped(vehicleSpawner);
  this.registerItemSubtyped(vehicleUpgrade);
  this.registerItemSubtyped(armorItem);
  this.registerItemSubtyped(npcSpawner);
  this.registerItemSubtyped(npcCommandBaton);
  this.addSubtypeInfoToItem(npcCommandBaton, 0, "Simple Command Baton", "","Issues simple commands to a single npc");
  this.addSubtypeInfoToItem(npcCommandBaton, 1, "Adept Command Baton", "","Issues advanced commands to a single npc");
  this.addSubtypeInfoToItem(npcCommandBaton, 2, "Expert Command Baton", "","Issues simple commands to several npcs, or advanced commands to a single npc");
  this.addSubtypeInfoToItem(npcCommandBaton, 3, "Master Command Baton", "","Issues advanced commands to several npcs");
  this.registerItemSubtyped(civicPlacer);
  }

private void loadRecipes()
  {
  //TODO create recipes..figure out crafting..blahblah..
  }

private void loadDebugItems()
  {
  if(!Config.DEBUG)
    {
    return;
    }  
  blockScanner = new ItemBlockScanner(Config.getItemID("debug.blockScanner", 9000));
  this.registerItemSingle(blockScanner, "Block Scanner", "Block Scanning Tool","Sneak-Right-Click to get BlockID/Meta from clicked-on block").setIconTexture("ancientwarfare:testIcon1", 0);
  }

public Description registerItemSubtyped(AWItemBase item)
  {
  Description d = DescriptionRegistry2.instance().registerItem(item, false);
  return d;
  }

public Description registerItemSingle(AWItemBase item, String name, String desc, String tip)
  {
  Description d = DescriptionRegistry2.instance().registerItem(item, true);
  d.setName(name, 0);
  d.setDescription(desc, 0);
  d.setTooltip(tip, 0);  
  return d;
  }

public Description addSubtypeInfoToItem(AWItemBase item, int damage, String name, String desc, String tooltip)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(item.itemID);
  d.setName(name, damage);
  d.setDescription(desc, damage);
  d.setTooltip(tooltip, damage);  
  return d;
  }

}
