package edu.cs240.byu.familymap.main.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.Event;
import edu.cs240.byu.familymap.main.modelPackage.Model;
import edu.cs240.byu.familymap.main.modelPackage.Person;
import edu.cs240.byu.familymap.main.modelPackage.Search;

/**
 * Created by zachhalvorsen on 3/25/16.
 */
public class MapFragment extends SupportMapFragment
{
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private GoogleApiClient apiClient;

    private double latitude;
    private double longitude;
    GoogleMap myGoogleMap;
    private boolean mapIsReady = false;

    public MapFragment()
    {
        // Required empty public constructor
        if(getArguments() == null)
        {
            latitude = 12.0;
            longitude = -30.0;
            Bundle args = new Bundle();
            args.putDouble(LATITUDE, latitude);
            args.putDouble(LONGITUDE, longitude);
            this.setArguments(args);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lat Parameter 1.
     * @param lon Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SupportMapFragment newInstance(Double lat, Double lon)
    {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putDouble(LATITUDE, lat);
        args.putDouble(LONGITUDE, lon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        apiClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks()
                {
                    @Override
                    public void onConnected(Bundle bundle)
                    {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i)
                    {

                    }
                }).build();

        getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                mapIsReady = true;
                myGoogleMap = googleMap;
                myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                myGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
                {
                    @Override
                    public View getInfoWindow(Marker marker)
                    {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker)
                    {
                        // Getting view from the layout file
                        View v = getActivity().getLayoutInflater().inflate(R.layout.layout_marker_popup, null);

                        TextView gender = (TextView) v.findViewById(R.id.gender_char);
                        gender.setText(marker.getTitle().substring(0, 1));
                        gender.setTextColor(Color.RED);
                        if(gender.getText().equals("\u2642"))
                        {
                            gender.setTextColor(Color.BLUE);
                        }

                        TextView name = (TextView) v.findViewById(R.id.name);
                        name.setText(marker.getTitle().substring(1, marker.getTitle().length()));
                        name.setTextColor(Color.BLACK);

                        TextView description = (TextView) v.findViewById(R.id.event_info);
                        description.setText(marker.getSnippet());
                        description.setTextColor(Color.BLACK);
                        description.isClickable();

                        LinearLayout layout = (LinearLayout) v.findViewById(R.id.popup_layout);
                        layout.isClickable();
                        layout.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(getActivity(), PersonActivity.class);
                                startActivity(intent);
                            }
                        });

                        return v;
                    }
                });
                myGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(Marker marker)
                    {
                        Event e = Model.getSINGLETON().getSelectedEvent(marker);
                        if (e != null)
                        {
                            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(e.getPosition()), 500, null);
                            //myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(e.getPosition()));
                            marker.showInfoWindow();
                            return true;
                        }
                        else
                        {
                            Log.d("!!UNRECOGNIZED EVENT!!", "");
                            return false;
                        }
                    }
                });
                updateUI();
            }
        });
        if (getArguments() != null)
        {
            latitude = getArguments().getDouble(LATITUDE);
            longitude = getArguments().getDouble(LONGITUDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = super.onCreateView(inflater, container, savedInstanceState);
        getActivity();
        return v;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        apiClient.connect();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        apiClient.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        Log.d("In options menu", "");
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.home:
                finish();
                return true;
            case R.id.search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.filter:
                intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private void finish()
    {

    }

    private void updateUI()
    {
        if (mapIsReady)
        {
            myGoogleMap.clear();
            Map<String, Event> events = Model.getSINGLETON().getEvents();
            Set<String> eventIDs = events.keySet();
            Iterator<String> idIterator = eventIDs.iterator();
            while (idIterator.hasNext())
            {
                Event currentEvent = events.get(idIterator.next());
                if (Model.getSINGLETON().getFilter().contains(currentEvent.getDescription()))
                {
                    Map<String, Person> personMap = Model.getSINGLETON().getPeople();
                    //Log.d("Person map size = ", String.valueOf(personMap.size()));
                    Person currentPerson = personMap.get(currentEvent.getPersonId());
                    if (currentPerson != null)
                    {
                        String gender = "\u2640";
                        if (currentPerson.getGender().equals("m"))
                        {
                            gender = "\u2642";
                        }
                        MarkerOptions mo = new MarkerOptions()
                                .position(currentEvent.getPosition())
                                .icon(BitmapDescriptorFactory.defaultMarker(currentEvent.getColor()))
                                .title(gender + " " + currentPerson.getFirstName() + " " + currentPerson.getLastName())
                                .snippet(
                                        currentEvent.getDescription()
                                                + ": " + currentEvent.getCity()
                                                + " " + currentEvent.getCountry()
                                                + " (" + currentEvent.getYear() + ")");
                        myGoogleMap.addMarker(mo);

                    }
                    else
                    {
                        Log.d("!!UNKNOWN PERSON!!", currentEvent.getPersonId());
                    }
                }
            }
        }
    }
}
