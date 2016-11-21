package edu.cs240.byu.familymap.main.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.Model;

public class MapActivity extends AppCompatActivity
{
    SupportMapFragment mapFragment;
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        model = Model.getSINGLETON();

        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.mapFragmentLayout);
        LatLng position = model.getChosenEvent().getPosition();
        mapFragment = MapFragment.newInstance(position.latitude, position.longitude);
        fm.beginTransaction().add(R.id.mapFragmentLayout, mapFragment).commit();
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
}
