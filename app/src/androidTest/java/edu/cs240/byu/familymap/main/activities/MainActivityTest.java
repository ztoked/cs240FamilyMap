package edu.cs240.byu.familymap.main.activities;

import junit.framework.TestCase;

import org.json.JSONObject;

import edu.cs240.byu.familymap.main.modelPackage.LoginInfo;
import edu.cs240.byu.familymap.main.modelPackage.Model;

/**
 * Created by zachhalvorsen on 4/8/16
 */
public class MainActivityTest extends TestCase
{
    public void getsAuthCodeAndUserIDCorrectly()
    {
        LoginInfo li = new LoginInfo();
        li.setServerHost("sheila");
        li.setPassword("parker");
        li.setServerHost("10.0.0.5");
        li.setServerPort("8080");
        Model.getSINGLETON().setLogin(li);
        MainActivity.onButtonClicked();
        JSONObject rootObj = new JSONObject(loginResult);
        assertThat(rootObj.getString("personId").equals("Sheila_Parker"), is(true));
        Model.getSINGLETON().setUserPersonID(rootObj.getString("personId"));
        return rootObj.getString("Authorization");
    }
}