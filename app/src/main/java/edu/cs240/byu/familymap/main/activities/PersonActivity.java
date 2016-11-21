package edu.cs240.byu.familymap.main.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.FamilyAdapter;
import edu.cs240.byu.familymap.main.modelPackage.LifeEventAdapter;
import edu.cs240.byu.familymap.main.modelPackage.Model;
import edu.cs240.byu.familymap.main.modelPackage.Person;

public class PersonActivity extends AppCompatActivity
{
    private RecyclerView eventRV;
    private RecyclerView familyRV;
    private String[] eventData;
    private List<String> familyMembers;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        person = Model.getSINGLETON().getChosenPerson();

        TextView firstName = (TextView)findViewById(R.id.person_firstName);
        if (firstName != null)
        {
            firstName.setText(person.getFirstName());
        }

        TextView lastName = (TextView)findViewById(R.id.person_lastName);
        if (lastName != null)
        {
            lastName.setText(person.getLastName());
        }

        TextView gender = (TextView) findViewById(R.id.person_gender);
        if(gender != null)
        {
            gender.setText("Female");
            if (person.getGender().equals("m"))
            {
                gender.setText("Male");
            }
        }

        eventRV = (RecyclerView)findViewById(R.id.person_lifeEventRecycler);
        LinearLayoutManager eventLLM = new LinearLayoutManager(this);
        if (eventRV != null)
        {
            eventRV.setLayoutManager(eventLLM);
        }

        initializeEventData();
        initializeEventAdapter();
        final CheckBox eventButton = (CheckBox)findViewById(R.id.person_lifeEventButton);
        if(eventButton != null)
        {
            eventButton.setChecked(true);
            eventButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        eventRV.setVisibility(View.VISIBLE);
                        return;
                    }
                    eventRV.setVisibility(View.INVISIBLE);
                }
            });
        }

        familyRV = (RecyclerView)findViewById(R.id.person_familyRecycler);
        LinearLayoutManager familyLLM = new LinearLayoutManager(this);
        if (familyRV != null)
        {
            familyRV.setLayoutManager(familyLLM);
        }

        initializeFamilyData();
        initializeFamilyAdapter();
        final CheckBox familyButton = (CheckBox)findViewById(R.id.person_familyButton);
        if(familyButton != null)
        {
            familyButton.setChecked(true);
            familyButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        familyRV.setVisibility(View.VISIBLE);
                        return;
                    }
                    familyRV.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.generic_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.back_button:
                finish();
                return true;
            case R.id.go_to_top:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    private void initializeEventData()
    {
        Set<String> lifeEvents = Model.getSINGLETON().getPersonEvents().get(person.getPersonId());
        eventData = new String[lifeEvents.size()];
        int i = 0;
        for (String event : lifeEvents)
        {
            eventData[i] = event;
            ++i;
        }
        Log.d("PersonActivity", String.valueOf(eventData.length));
        if(eventData.length == 0)
        {
            eventRV.setVisibility(View.INVISIBLE);
            Log.d("PersonActivity", "there are not enough events");
        }
        Model.getSINGLETON().orderEvents(eventData);
    }

    private void initializeEventAdapter()
    {
        LifeEventAdapter adapter = new LifeEventAdapter(eventData, person);
        eventRV.setAdapter(adapter);
        Log.d("PersonActivity", "initialized event adapter with " + eventData.length + " events");
    }

    private void initializeFamilyData()
    {
        Model model = Model.getSINGLETON();
        familyMembers =  new ArrayList<>();
        for (String s : model.getPeople().keySet())
        {
            Person currentPerson = model.getPeople().get(s);
            if (currentPerson.getFatherID().equals(person.getPersonId()))
            {
                familyMembers.add(currentPerson.getPersonId());
            }
            else if (currentPerson.getMotherID().equals(person.getPersonId()))
            {
                familyMembers.add(currentPerson.getPersonId());
            }
            else if (person.getFatherID().equals(currentPerson.getPersonId()) || person.getMotherID().equals(currentPerson.getPersonId()))
            {
                familyMembers.add(currentPerson.getPersonId());
            }
        }
        if(!person.getSpouseID().equals(""))
        {
            familyMembers.add(person.getSpouseID());
        }
        if(familyMembers.size() == 0)
        {
            familyRV.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeFamilyAdapter()
    {
        FamilyAdapter adapter = new FamilyAdapter(familyMembers, person);
        familyRV.setAdapter(adapter);
        Log.d("PersonActivity", "initialized family adapter");
    }
}
