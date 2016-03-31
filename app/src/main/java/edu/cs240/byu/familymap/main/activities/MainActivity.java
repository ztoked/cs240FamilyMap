package edu.cs240.byu.familymap.main.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.Event;
import edu.cs240.byu.familymap.main.modelPackage.LoginInfo;
import edu.cs240.byu.familymap.main.modelPackage.Model;
import edu.cs240.byu.familymap.main.modelPackage.Person;

public class MainActivity extends AppCompatActivity
{
    LoginFragment loginFragment;
    SupportMapFragment mapFragment;
    Button button;
    String postData;
    Model model;
    boolean isGettingFamily;

    private final boolean autoLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Model.initialize();
        model = Model.getSINGLETON();

        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = (LoginFragment)fm.findFragmentById(R.id.loginLayout);
        if(loginFragment == null)
        {
            loginFragment = new LoginFragment();
            fm.beginTransaction().add(R.id.loginLayout, loginFragment).commit();
        }
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClicked();
            }
        });
    }

    private void onButtonClicked()
    {
        model.setLogin(loginFragment.getLoginInfo());
        LoginInfo login = model.getLogin();
        if(autoLogin)
        {
            login.setUsername("a");
            login.setPassword("a");
            login.setServerHost("10.24.214.231");
            login.setServerPort("8080");
        }
        if(login.getUsername().length() > 0 && login.getPassword().length() > 0
                && login.getServerHost().length() > 0 && login.getServerPort().length() > 0)
        {
            Toast.makeText(MainActivity.this, "Attempting Login...", Toast.LENGTH_SHORT).show();
            postData = "{ username:\"" + login.getUsername() + "\", " +
                    "password:\"" + login.getPassword() + "\" }";
            try
            {
                URL url = new URL("http://" + login.getServerHost() + ":" + login.getServerPort() + "/user/login");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                AsyncLogin doInBackground = new AsyncLogin();
                doInBackground.execute(connection);
            }
            catch (IOException e)
            {
                // IO ERROR
                Toast.makeText(MainActivity.this, "Unable to open connection", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "Please enter all of the information before signing in", Toast.LENGTH_SHORT).show();
        }
    }

    private void finishedLogin(String result)
    {
        if(result == null)
        {
            Toast.makeText(MainActivity.this, "Failed Login... ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Getting family...", Toast.LENGTH_SHORT).show();
            Model.getSINGLETON().setAuthCode(getAuthCode(result));
            //Toast.makeText(MainActivity.this, "authCode = " + Model.getSINGLETON().getAuthCode(), Toast.LENGTH_SHORT).show();
            try
            {
                isGettingFamily = true;
                URL url = new URL("http://" + model.getLogin().getServerHost() + ":" + model.getLogin().getServerPort() + "/person/");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                // Set HTTP request headers, if necessary
                connection.addRequestProperty("Authorization", Model.getSINGLETON().getAuthCode());

                connection.setRequestMethod("GET");

                AsyncLogin doInBackground = new AsyncLogin();
                doInBackground.execute(connection);
            }
            catch (IOException e)
            {
                // IO ERROR
                Toast.makeText(MainActivity.this, "Unable to open connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getAuthCode(String loginResult)
    {
        try
        {
            JSONObject rootObj = new JSONObject(loginResult);
            return rootObj.getString("Authorization");
        }
        catch (JSONException e)
        {
            Toast.makeText(MainActivity.this, loginResult, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void finishedGettingFamily(String result)
    {
        if(result == null)
        {
            Toast.makeText(MainActivity.this, "Failed to get family...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isGettingFamily)
        {
            addEvents(result);
        }
        else
        {
            try
            {
                JSONObject rootObj = new JSONObject(result);
                //Log.d("Turned result into JSON", result);
                JSONArray dataArray = rootObj.getJSONArray("data");
                //Log.d("Got data", " array");
                for(int i = 0; i < dataArray.length(); i++)
                {
                    JSONObject obj = dataArray.getJSONObject(i);

                    String descendant = obj.getString("descendant");
                    String personId = obj.getString("personID");
                    String firstName = obj.getString("firstName");
                    String lastName = obj.getString("lastName");
                    String gender = obj.getString("gender");
                    String fatherID = "";
                    if(obj.has("father"))
                    {
                        fatherID = obj.getString("father");
                    }
                    String motherID = "";
                    if(obj.has("mother"))
                    {
                        motherID = obj.getString("mother");
                    }
                    String spouseID = "";
                    if(obj.has("spouse"))
                    {
                        spouseID = obj.getString("spouse");
                    }
                    Person p = new Person(descendant, personId, firstName, lastName, gender, fatherID, motherID, spouseID);
                    model.getPeople().put(personId, p);
                    if(i == dataArray.length()-1)
                    {
                        model.setUser(p);
                    }
                }
                model.populateFamily();
                Toast.makeText(MainActivity.this, "First name: " + model.getUser().getFirstName()
                        +"\nLast name: " + model.getUser().getLastName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Getting events...", Toast.LENGTH_SHORT).show();
                try
                {
                    isGettingFamily = false;
                    URL url = new URL("http://" + model.getLogin().getServerHost() + ":" + model.getLogin().getServerPort() + "/event/");

                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                    // Set HTTP request headers, if necessary
                    connection.addRequestProperty("Authorization", Model.getSINGLETON().getAuthCode());

                    connection.setRequestMethod("GET");

                    AsyncLogin doInBackground = new AsyncLogin();
                    doInBackground.execute(connection);
                }
                catch (IOException e)
                {
                    // IO ERROR
                    Toast.makeText(MainActivity.this, "Unable to open connection", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addEvents(String result)
    {
        try
        {
            JSONObject rootObj = new JSONObject(result);
            //Log.d("Turned result into JSON", result);
            JSONArray dataArray = rootObj.getJSONArray("data");
            //Log.d("Got data", " array");
            for(int i = 0; i < dataArray.length(); i++)
            {
                JSONObject obj = dataArray.getJSONObject(i);

                String eventId = obj.getString("eventID");
                String personId = obj.getString("personID");
                double latitude = obj.getDouble("latitude");
                double longitude = obj.getDouble("longitude");
                String country = obj.getString("country");
                String city = obj.getString("city");
                String description = obj.getString("description");
                String year = obj.getString("year");
                String descendant = obj.getString("descendant");

                Event e = new Event(eventId, personId, latitude, longitude, country, city, description, year, descendant);
                //Add to colors
                Map<String, Float> colors = model.getEventColors();
                if(!colors.containsKey(e.getDescription()))
                {
                    colors.put(e.getDescription(), (float)new Random().nextInt(360));
                }
                e.setColor(colors.get(e.getDescription()));
                //Add event to events
                Map<String, Set<Event>> map =  model.getPersonEvents();
                if(!map.containsKey(personId))
                {
                    map.put(personId, new HashSet<Event>());
                }
                map.get(personId).add(e);
                model.getFilter().add(e.getDescription());
                model.getEventTypes().add(e.getDescription());
                model.getEvents().put(eventId, e);
            }
            FragmentManager fm = this.getSupportFragmentManager();
            mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.mapFragmentLayout);
            Toast.makeText(MainActivity.this, "Opening map...", Toast.LENGTH_SHORT).show();
            mapFragment = new MapFragment();
            fm.beginTransaction().remove(loginFragment).commit();
            fm.beginTransaction().add(R.id.mapFragmentLayout, mapFragment).commit();
        }
        catch (JSONException e)
        {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncLogin extends AsyncTask<HttpURLConnection,Void,String>
    {
        HttpURLConnection connection;
        @Override
        protected String doInBackground(HttpURLConnection... connections)
        {
            connection = connections[0];
            try
            {
                connection.connect();

                if(connection.getRequestMethod().equals("POST"))
                {
                    // Write post data to request body
                    OutputStream requestBody = connection.getOutputStream();
                    requestBody.write(postData.getBytes());
                    requestBody.close();
                }

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    // Get HTTP response headers, if necessary
                    // Map<String, List<String>> headers = connection.getHeaderFields();

                    // Get response body input stream
                    InputStream responseBody = connection.getInputStream();

                    // Read response body bytes
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = responseBody.read(buffer)) != -1)
                    {
                        baos.write(buffer, 0, length);
                    }

                    // Convert response body bytes to a string
                    return baos.toString();
                }
                else
                {
                    return null;
                }
            }
            catch (IOException e)
            {
                // IO ERROR
                return null;
            }
        }

        protected void onPostExecute(String result)
        {
            if(connection.getRequestMethod().equals("POST"))
            {
                finishedLogin(result);
            }
            else
            {
                finishedGettingFamily(result);
            }
        }
    }
}
