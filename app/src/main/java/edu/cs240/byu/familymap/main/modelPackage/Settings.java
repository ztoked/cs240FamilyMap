package edu.cs240.byu.familymap.main.modelPackage;

import android.graphics.Color;

/**
 * Created by zachhalvorsen on 3/21/16.
 */
public class Settings
{
    private boolean lifeStoryLines;
    private float lifeStoryColor;
    private boolean familyTreeLines;
    private float familyTreeColor;
    private boolean spouseLines;
    private float spouseLineColor;
    private int mapType;

    public final int NORMAL_MAP = 0;
    public final int SATELITE_MAP = 1;
    public final int HYBRID_MAP = 2;
    public final int TERRAIN_MAP = 3;


    public Settings()
    {
        lifeStoryLines = true;
        lifeStoryColor = Color.GREEN;
        familyTreeLines = true;
        familyTreeColor = Color.RED;
        spouseLines = true;
        spouseLineColor = Color.BLUE;
        mapType = NORMAL_MAP;
    }

    public void setMapNormal()
    {
        mapType = NORMAL_MAP;
    }

    public void setMapSatelite()
    {
        mapType = SATELITE_MAP;
    }

    public void setMapHybrid()
    {
        mapType = HYBRID_MAP;
    }

    public boolean isLifeStoryLines()
    {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines)
    {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines()
    {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines)
    {
        this.familyTreeLines = familyTreeLines;
    }

    public int getMapType()
    {
        return mapType;
    }

    public void setMapType(int mapType)
    {
        this.mapType = mapType;
    }

    public String getLifeStoryStringColor()
    {
        return getColorFromFloat(lifeStoryColor);
    }

    public float getLifeStoryColor()
    {
        return lifeStoryColor;
    }

    public void setLifeStoryColor(String color)
    {
        lifeStoryColor = getColorFromString(color);
    }

    public float getFamilyTreeColor()
    {
        return familyTreeColor;
    }

    public String getFamilyTreeStringColor()
    {
        return getColorFromFloat(familyTreeColor);
    }

    public void setFamilyTreeColor(String color)
    {
        familyTreeColor = getColorFromString(color);
    }

    public boolean isSpouseLines()
    {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines)
    {
        this.spouseLines = spouseLines;
    }

    public float getSpouseLineColor()
    {
        return spouseLineColor;
    }

    public String getSpouseLineStringColor()
    {
        return getColorFromFloat(spouseLineColor);
    }

    public void setSpouseLineColor(String color)
    {
        spouseLineColor = getColorFromString(color);
    }

    private float getColorFromString(String color)
    {
        switch (color)
        {
            case "Green":
            {
                return Color.GREEN;
            }
            case "Red":
            {
                return Color.RED;
            }
            case "Blue":
            {
                return Color.BLUE;
            }
            case "Yellow":
            {
                return Color.YELLOW;
            }
            case "Purple":
            {
                return Color.MAGENTA;
            }
            default:
            {
                return Color.GREEN;
            }
        }
    }

    private String getColorFromFloat(float color)
    {
        if(color == Color.GREEN)
        {
            return "Green";
        }
        else if(color == Color.RED)
        {
            return "Red";
        }
        else if(color == Color.BLUE)
        {
            return "Blue";
        }
        else if(color == Color.YELLOW)
        {
            return "Yellow";
        }
        else if(color == Color.MAGENTA)
        {
            return "Purple";
        }
        return "Green";
    }
}
