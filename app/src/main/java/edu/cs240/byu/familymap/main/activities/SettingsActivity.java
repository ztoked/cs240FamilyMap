package edu.cs240.byu.familymap.main.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
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
import edu.cs240.byu.familymap.main.modelPackage.Model;
import edu.cs240.byu.familymap.main.modelPackage.Person;

public class SettingsActivity extends AppCompatActivity
{
    Model model;
    Spinner lifeSpinner;
    Switch lifeSwitch;
    Spinner familySpinner;
    Switch familySwitch;
    Spinner spouseSpinner;
    Switch spouseSwitch;
    Spinner mapType;
    String postData;
    private boolean isGettingFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        model = Model.getSINGLETON();

        final String[] arrayColorSpinner = new String[]{"Green", "Red", "Blue", "Yellow", "Purple"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayColorSpinner);

        lifeSpinner = (Spinner)findViewById(R.id.lifeSpinner);
        if (lifeSpinner != null)
        {
            lifeSpinner.setAdapter(adapter);
            for(int i = 0; i < arrayColorSpinner.length; ++i)
            {
                if(arrayColorSpinner[i].equals(Model.getSINGLETON().getSettings().getLifeStoryStringColor()))
                {
                    lifeSpinner.setSelection(i);
                }
            }
        }
        lifeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Model.getSINGLETON().getSettings().setLifeStoryColor(arrayColorSpinner[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        lifeSwitch = (Switch)findViewById(R.id.lifeSwitch);
        if (lifeSwitch != null)
        {
            lifeSwitch.setChecked(Model.getSINGLETON().getSettings().isLifeStoryLines());
        }
        lifeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Model.getSINGLETON().getSettings().setLifeStoryLines(isChecked);
            }
        });

        familySpinner = (Spinner)findViewById(R.id.familyTreeSpinner);
        if (familySpinner != null)
        {
            familySpinner.setAdapter(adapter);
            for(int i = 0; i < arrayColorSpinner.length; ++i)
            {
                if(arrayColorSpinner[i].equals(Model.getSINGLETON().getSettings().getFamilyTreeStringColor()))
                {
                    familySpinner.setSelection(i);
                }
            }
        }
        familySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Model.getSINGLETON().getSettings().setFamilyTreeColor(arrayColorSpinner[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        familySwitch = (Switch)findViewById(R.id.familyTreeSwitch);
        if (familySwitch != null)
        {
            familySwitch.setChecked(Model.getSINGLETON().getSettings().isFamilyTreeLines());
        }
        familySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Model.getSINGLETON().getSettings().setFamilyTreeLines(isChecked);
            }
        });

        spouseSpinner = (Spinner)findViewById(R.id.spouseSpinner);
        if (spouseSpinner != null)
        {
            spouseSpinner.setAdapter(adapter);
            for(int i = 0; i < arrayColorSpinner.length; ++i)
            {
                if(arrayColorSpinner[i].equals(Model.getSINGLETON().getSettings().getSpouseLineStringColor()))
                {
                    spouseSpinner.setSelection(i);
                }
            }
        }
        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Model.getSINGLETON().getSettings().setSpouseLineColor(arrayColorSpinner[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        spouseSwitch = (Switch)findViewById(R.id.spouseSwitch);
        if (spouseSwitch != null)
        {
            spouseSwitch.setChecked(Model.getSINGLETON().getSettings().isSpouseLines());
        }
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Model.getSINGLETON().getSettings().setSpouseLines(isChecked);
            }
        });

        final String[] arrayMapSpinner = new String[]{"Normal", "Satellite", "Hybrid", "Terrain"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayMapSpinner);

        mapType = (Spinner)findViewById(R.id.mapSpinner);
        if (mapType != null)
        {
            mapType.setAdapter(adapter);
            mapType.setSelection(Model.getSINGLETON().getSettings().getMapType());
        }
        mapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Model.getSINGLETON().getSettings().setMapType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        TableRow resync = (TableRow)findViewById(R.id.resyncRow);
        if(resync != null)
        {
            resync.setClickable(true);
            resync.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    model.clear();
                    getFamily();
                }
            });
        }
        TableRow logout = (TableRow)findViewById(R.id.logoutRow);
        if(logout != null)
        {
            logout.setClickable(true);
            logout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Model.setSINGLETON(null);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.settings_back_button)
        {
            finish();
            return true;
        }
        return false;
    }

    public void getFamily()
    {
        Toast.makeText(this, "Re-Getting family...", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Unable to open connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void finishedGettingFamily(String result)
    {
        if(result == null)
        {
            Toast.makeText(this, "Failed to get family...", Toast.LENGTH_SHORT).show();
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
                    if(personId.equals(Model.getSINGLETON().getUserPersonID()))
                    {
                        model.setUser(p);
                    }
                }
                model.populateFamily();
                Toast.makeText(this, "Getting events...", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Unable to open connection", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
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
                Map<String, Set<String>> map =  model.getPersonEvents();
                if(!map.containsKey(personId))
                {
                    map.put(personId, new HashSet<String>());
                }
                map.get(personId).add(eventId);
                model.getFilter().add(e.getDescription());
                model.getEventTypes().add(e.getDescription());
                model.getEvents().put(eventId, e);
            }
            finish();
        }
        catch (JSONException e)
        {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
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
            if(!connection.getRequestMethod().equals("POST"))
            {
                finishedGettingFamily(result);
            }
        }
    }
}
