package edu.cs240.byu.familymap.main.activities;

import junit.framework.TestCase;

import edu.cs240.byu.familymap.main.modelPackage.Model;


/**
 * Created by zachhalvorsen on 4/8/16
 */
public class SettingsActivityTest extends TestCase
{
    public void testSwitchesSettingsApply()
    {
        SettingsActivity sa = new SettingsActivity();
        sa.setLifeSwitch(false);
        assertThat(Model.getSINGLETON().getSettings().isLifeStoryLines(), is(false));
        sa.setLifeSwitch(true);
        assertThat(Model.getSINGLETON().getSettings().isLifeStoryLines(), is(true));
        sa.setSpouseSwitch(false);
        assertThat(Model.getSINGLETON().getSettings().isSpouseLines(), is(false));
        sa.setSpouseSwitch(true);
        assertThat(Model.getSINGLETON().getSettings().isSpouseLines(), is(true));
        sa.setFamilySwitch(false);
        assertThat(Model.getSINGLETON().getSettings().isFamilyTreeLines(), is(false));
        sa.setFamilySwitch(true);
        assertThat(Model.getSINGLETON().getSettings().isFamilyTreeLines(), is(true));
    }
}