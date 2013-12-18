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
package shadowmage.ancient_structures.common.template.load;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureValidationSettings;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class TemplateParser
{

private TemplateParser(){}

private static TemplateParser instance = new TemplateParser(){};
public static TemplateParser instance(){return instance;}
private TemplateFormatConverter converter = new TemplateFormatConverter();

public StructureTemplate parseTemplate(File file)
  {  
  FileReader reader = null;
  Scanner scan = null;
  try
    {
    List<String> templateLines = new ArrayList<String>();
    reader = new FileReader(file);
    scan = new Scanner(reader);
    String line;
    while(scan.hasNextLine())
      {
      templateLines.add(scan.nextLine());
      }
    return parseTemplateLines(templateLines); 
    } 
  catch (FileNotFoundException e)
    {
    e.printStackTrace();
    }
  finally
    {
    if(reader!=null)
      {
      try
        {
        reader.close();
        } 
      catch (IOException e)
        {
        e.printStackTrace();
        }
      }
    if(scan!=null)
      {
      scan.close();
      }
    }
  return null;
  }

private StructureTemplate parseTemplateLines(List<String> lines) throws IllegalArgumentException
  {
  Iterator<String> it = lines.iterator();
  String line;
 
  List<TemplateRule> parsedRules = new ArrayList<TemplateRule>();  
  TemplateRule[] ruleArray = null;
  StructureValidationSettings validation = null;  
  List<String> groupedLines = new ArrayList<String>();
  
  
  int parsedLayers = 0;
  
  String name = null;
  int xSize = 0, ySize = 0, zSize = 0, xOffset = 0, yOffset = 0, zOffset = 0;
  short[] templateData = null;
  boolean newVersion = false;
  boolean[] initData = new boolean[4];
    
  while(it.hasNext())
    {
    line = it.next();    
    
    if(line.startsWith("header:"))
      {
      while(it.hasNext())
        {
        line = it.next();
        if(line.startsWith(":endheader"))
          {          
          break;
          }
        if(line.startsWith("version="))
          {
          newVersion = true;
          initData[0] = true;
          }
        if(line.startsWith("name="))
          {
          name = StringTools.safeParseString("=", line);
          initData[1] = true;
          }
        if(line.startsWith("size="))
          {
          int[] sizes = StringTools.safeParseIntArray("=", line);
          xSize = sizes[0];
          ySize = sizes[1];
          zSize = sizes[2];          
          initData[2] = true;
          }
        if(line.startsWith("offset="))
          {
          int[] offsets = StringTools.safeParseIntArray("=", line);
          xOffset = offsets[0];
          yOffset = offsets[1];
          zOffset = offsets[2];
          initData[3] = true;
          }        
        } 
      if(!newVersion)
        {
        return converter.convertOldTemplate(lines);
        }
      for(int i = 0; i < 4; i++)
        {
        if(initData[i]==false)
          {
          AWLog.logError("error parsing structure for: "+name);
          return null;
          }
        }
      templateData = new short[xSize*ySize*zSize];
      }
    
    /**
     * parse out validation data
     */
    if(line.startsWith("validation:"))
      {
      groupedLines.add(line);
      while(it.hasNext())
        {
        line = it.next();
        groupedLines.add(line);
        if(line.startsWith(":endvalidation"))
          {          
          break;
          }
        }
      parseValidation(groupedLines);
      /**
       * parse out validation data
       */   
      groupedLines.clear();
      }
    
    /**
     * parse out rule data
     */
    if(line.startsWith("rule:"))
      {
      groupedLines.add(line);
      while(it.hasNext())
        {
        line = it.next();
        groupedLines.add(line);
        if(line.startsWith(":endrule"))
          {          
          break;
          }
        }
      TemplateRule rule = parseRule(groupedLines);
      if(rule!=null)
        {
        parsedRules.add(rule);
        }
      groupedLines.clear();
      }    
    
    /**
     * parse out layer data
     */
    if(line.startsWith("layer:"))
      {      
      groupedLines.add(line);
      while(it.hasNext())
        {
        line = it.next();
        groupedLines.add(line);
        if(line.startsWith(":endlayer"))
          {          
          break;
          }
        }
      parseLayer(groupedLines, parsedLayers, xSize, ySize, zSize, templateData);
      parsedLayers++;
      groupedLines.clear();
      }
    }
  
  /**
   * initialze data for construction of template -- put rules into array
   */
  ruleArray = new TemplateRule[parsedRules.size()+1];
  for(TemplateRule rule : parsedRules)
    {
    ruleArray[rule.ruleNumber] = rule;
    }
  return constructTemplate(name, xSize, ySize, zSize, xOffset, yOffset, zOffset, templateData, ruleArray, validation);  
  }

private TemplateRule parseRule(List<String> templateLines)
  {
  return null;
  }

private StructureTemplate constructTemplate(String name, int x, int y, int z, int xo, int yo, int zo, short[] templateData, TemplateRule[] rules, StructureValidationSettings validation)
  {
  StructureTemplate template = new StructureTemplate(name, x, y, z, xo, yo, zo);
  template.setRuleArray(rules);
  template.setTemplateData(templateData);
  template.setValidationSettings(validation);
  return template;
  }

/**
 * should parse layer and insert direcly into templateData
 */
private void parseLayer(List<String> templateLines, int yLayer, int xSize, int ySize, int zSize, short[] templateData)
  {
  int z = 0;
  for(String st : templateLines)
    {
    if(st.startsWith("layer:") || st.startsWith(":endlayer"))
      {
      continue;
      }
    short[] data = StringTools.safeParseShortArray("=", st);
    for(int x = 0; x < xSize && x < data.length; x++)
      {
      templateData[StructureTemplate.getIndex(x, yLayer, z, xSize, ySize, zSize)]=data[x];
      }
    z++;
    }
  }

private Object parseValidation(List<String> lines)
  {
  return null;
  }

}
