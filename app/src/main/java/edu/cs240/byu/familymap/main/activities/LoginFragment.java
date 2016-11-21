package edu.cs240.byu.familymap.main.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.LoginInfo;


public class LoginFragment extends Fragment
{
    EditText username;
    EditText password;
    EditText serverHost;
    EditText serverPort;

    public LoginFragment()
    {
        // Required empty public constructor
    }

    public static LoginFragment newInstance()
    {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        username = (EditText)v.findViewById(R.id.username);

        password = (EditText)v.findViewById(R.id.password);

        serverHost = (EditText)v.findViewById(R.id.serverHost);

        serverPort = (EditText)v.findViewById(R.id.serverPort);

        return v;
    }

    public LoginInfo getLoginInfo()
    {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username.getText().toString());
        loginInfo.setPassword(password.getText().toString());
        loginInfo.setServerHost(serverHost.getText().toString());
        loginInfo.setServerPort(serverPort.getText().toString());
        return loginInfo;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
}
