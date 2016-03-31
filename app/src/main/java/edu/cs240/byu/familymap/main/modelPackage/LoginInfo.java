package edu.cs240.byu.familymap.main.modelPackage;

/**
 * Created by zachhalvorsen on 3/16/16.
 */
public class LoginInfo
{
    private String username;
    private String password;
    private String serverHost;
    private String serverPort;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getServerHost()
    {
        return serverHost;
    }

    public void setServerHost(String serverHost)
    {
        this.serverHost = serverHost;
    }

    public String getServerPort()
    {
        return serverPort;
    }

    public void setServerPort(String serverPort)
    {
        this.serverPort = serverPort;
    }
}
