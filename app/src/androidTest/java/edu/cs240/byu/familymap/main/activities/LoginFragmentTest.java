package edu.cs240.byu.familymap.main.activities;

import junit.framework.TestCase;

import edu.cs240.byu.familymap.main.modelPackage.LoginInfo;

/**
 * Created by zachhalvorsen on 4/8/16.
 */
public class LoginFragmentTest extends TestCase
{
    public void returnsRightInfoToMainActivity()
    {
        LoginInfo li = new LoginInfo();
        LoginFragment lf = new LoginFragment();
        lf.setUserName("a");
        lf.setPassword("a");
        lf.setServerHost("1.1.1.1");
        lf.setServerPort("1");
        li = lf.getLoginInfo();
        assertThat(li.getUsername().equals("a"), is(true));
        assertThat(li.getPassword().equals("a"), is(true));
        assertThat(li.getServerHost().equals("1.1.1.1"), is(true));
        assertThat(li.getServerPort().equals("1"), is(true));
    }
}