package edu.cs240.byu.familymap.main.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.Model;

public class SettingsActivity extends AppCompatActivity
{
    Spinner lifeSpinner;
    Switch lifeSwitch;
    Spinner familySpinner;
    Switch familySwitch;
    Spinner spouseSpinner;
    Switch spouseSwitch;
    Spinner mapType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        final String[] arrayColorSpinner = new String[]{"Green", "Red", "Blue", "Yellow", "Purple"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayColorSpinner);

        lifeSpinner = (Spinner)findViewById(R.id.lifeSpinner);
        if (lifeSpinner != null)
        {
            lifeSpinner.setAdapter(adapter);
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

        final String[] arrayMapSpinner = new String[]{"Normal", "Satellite", "Hybrid"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayMapSpinner);

        mapType = (Spinner)findViewById(R.id.mapSpinner);
        if (mapType != null)
        {
            mapType.setAdapter(adapter);
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
    }
}
