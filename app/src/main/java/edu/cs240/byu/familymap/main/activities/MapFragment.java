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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.Event;
import edu.cs240.byu.familymap.main.modelPackage.Model;
import edu.cs240.byu.familymap.main.modelPackage.Person;

/**
 * Created by zachhalvorsen on 3/25/16
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
    private Polyline currentLifeEventLine;
    private Polyline currentSpouseLine;
    private List<Polyline> currentFamilyLines;
    private List<PolylineOptions> familyLineOptions;

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
                        if (gender.getText().equals("\u2642"))
                        {
                            gender.setTextColor(Color.BLUE);
                        }

                        TextView name = (TextView) v.findViewById(R.id.name);
                        name.setText(marker.getTitle().substring(1, marker.getTitle().length()));
                        name.setTextColor(Color.BLACK);

                        TextView description = (TextView) v.findViewById(R.id.event_info);
                        description.setText(marker.getSnippet());
                        description.setTextColor(Color.BLACK);

                        return v;
                    }
                });
                myGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                {
                    @Override
                    public void onInfoWindowClick(Marker marker)
                    {
                        Model model = Model.getSINGLETON();
                        Intent intent = new Intent(getActivity(), PersonActivity.class);
                        model.setChosenPerson(model.getPeople().get(model.getSelectedEvent(marker).getPersonId()));
                        startActivity(intent);
                    }
                });
                myGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(Marker marker)
                    {
                        Model model = Model.getSINGLETON();
                        Event e = model.getSelectedEvent(marker);
                        if (e != null)
                        {
                            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(e.getPosition()), 500, null);
                            marker.showInfoWindow();

                            //Making life event lines
                            if (model.getSettings().isLifeStoryLines())
                            {
                                if (currentLifeEventLine != null)
                                {
                                    currentLifeEventLine.setVisible(false);
                                }
                                Set<String> events = model.getPersonEvents().get(e.getPersonId());
                                String[] eventArray = new String[events.size()];
                                int i = 0;
                                for (String s : events)
                                {
                                    eventArray[i] = s;
                                    ++i;
                                }
                                model.orderEvents(eventArray);
                                PolylineOptions plo = new PolylineOptions();
                                for (i = 0; i < eventArray.length; ++i)
                                {
                                    plo.add(model.getEvents().get(eventArray[i]).getPosition());
                                }
                                plo.color((int) model.getSettings().getLifeStoryColor());
                                currentLifeEventLine = myGoogleMap.addPolyline(plo);
                            }

                            //Making family lines
                            if (model.getSettings().isFamilyTreeLines())
                            {
                                if (currentFamilyLines != null)
                                {
                                    for (int i = 0; i < currentFamilyLines.size(); i++)
                                    {
                                        currentFamilyLines.get(i).setVisible(false);
                                    }
                                }
                                List<String> family = model.getFamilyTree().get(e.getPersonId());
                                PolylineOptions plo = new PolylineOptions();
                                currentFamilyLines = new ArrayList<>();
                                familyLineOptions = new ArrayList<>();
                                getFamilyTree(Model.getSINGLETON().getPeople().get(e.getPersonId()), e);
                                plo.color((int) model.getSettings().getFamilyTreeColor());
                                for (int i = 0; i < familyLineOptions.size(); i++)
                                {
                                    currentFamilyLines.add(myGoogleMap.addPolyline(familyLineOptions.get(i)));
                                }
                            }

                            //Making spouse line
                            if (model.getSettings().isSpouseLines())
                            {
                                if (currentSpouseLine != null)
                                {
                                    currentSpouseLine.setVisible(false);
                                }
                                String spouseID = model.getPeople().get(e.getPersonId()).getSpouseID();
                                if (!spouseID.equals(""))
                                {
                                    Event firstEvent = model.getPeople().get(spouseID).firstEvent();
                                    if (firstEvent != null)
                                    {
                                        //Log.d("MapFrag", "Drawing spouse line");
                                        PolylineOptions plo = new PolylineOptions()
                                                .add(firstEvent.getPosition())
                                                .add(e.getPosition())
                                                .color((int) model.getSettings().getSpouseLineColor());
                                        currentSpouseLine = myGoogleMap.addPolyline(plo);
                                    }
                                }
                            }
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

    private void getFamilyTree(Person originalPerson, Event selectedEvent)
    {
        int depth = 0;
        getFamilyTreeHelper(originalPerson, selectedEvent, depth);
    }

    private void getFamilyTreeHelper(Person person, Event previousEvent, int depth)
    {
        if(person == null || !Model.getSINGLETON().getPeople().containsKey(person.getPersonId()))
        {
            return;
        }
        if(!person.getFatherID().equals(""))
        {
            Person father = Model.getSINGLETON().getPeople().get(person.getFatherID());
            PolylineOptions plo = new PolylineOptions()
                    .add(previousEvent.getPosition())
                    .add(father.firstEvent().getPosition())
                    .color((int) Model.getSINGLETON().getSettings().getFamilyTreeColor());
            if(depth > 0)
            {
                plo.width(plo.getWidth() / depth);
            }
            familyLineOptions.add(plo);
            getFamilyTreeHelper(father, father.firstEvent(), depth + 1);
        }

        if(!person.getMotherID().equals(""))
        {
            Person mother = Model.getSINGLETON().getPeople().get(person.getMotherID());
            PolylineOptions plo = new PolylineOptions()
                    .add(previousEvent.getPosition())
                    .add(mother.firstEvent().getPosition())
                    .color((int) Model.getSINGLETON().getSettings().getFamilyTreeColor());
            if(depth > 0)
            {
                plo.width(plo.getWidth() / depth);
            }
            familyLineOptions.add(plo);
            getFamilyTreeHelper(mother, mother.firstEvent(), depth + 1);
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
        //Log.d("", "STARTING MAP");
        apiClient.connect();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //Log.d("", "RESUMING MAP");
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
        Log.d("", "CREATING OPTIONS MENU");
        if(getActivity().getClass().equals(MainActivity.class))
        {
            inflater.inflate(R.menu.toolbar, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        switch (item.getItemId())
        {
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

    private void updateUI()
    {
        //Log.d("","UPDATING UI");
        if (mapIsReady)
        {
            myGoogleMap.clear();
            switch(Model.getSINGLETON().getSettings().getMapType())
            {
                case 0:
                {
                    myGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                }
                case 1:
                {
                    myGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                }
                case 2:
                {
                    myGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                }
                case 3:
                {
                    myGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                }
            }
            Map<String, Event> events = Model.getSINGLETON().getEvents();
            Set<String> eventIDs = events.keySet();
            for (String eventID : eventIDs)
            {
                Event currentEvent = events.get(eventID);
                if (Model.getSINGLETON().getFilter().contains(currentEvent.getDescription()))
                {
                    Map<String, Person> personMap = Model.getSINGLETON().getPeople();
                    //Log.d("Person map size = ", String.valueOf(personMap.size()));
                    Person currentPerson = personMap.get(currentEvent.getPersonId());
                    if (currentPerson != null)
                    {
                        if ((Model.getSINGLETON().getShowPaternalEvents() && Model.getSINGLETON().getPaternalAncestors().contains(currentPerson.getPersonId()))
                                || (Model.getSINGLETON().getShowMaternalEvents() && Model.getSINGLETON().getMaternalAncestors().contains(currentPerson.getPersonId()))
                                || currentPerson.getPersonId().equals(Model.getSINGLETON().getUser().getPersonId()))
                        {
                            String gender = "\u2640";
                            if (currentPerson.getGender().equals("m"))
                            {
                                gender = "\u2642";
                            }
                            if ((gender.equals("\u2642") && Model.getSINGLETON().getShowMaleEvents())
                                    || (gender.equals("\u2640") && Model.getSINGLETON().getShowFemaleEvents()))
                            {
                                Marker marker = myGoogleMap.addMarker(new MarkerOptions()
                                        .position(currentEvent.getPosition())
                                        .icon(BitmapDescriptorFactory.defaultMarker(currentEvent.getColor()))
                                        .title(gender + " " + currentPerson.getFirstName() + " " + currentPerson.getLastName())
                                        .snippet(
                                                currentEvent.getDescription()
                                                        + ": " + currentEvent.getCity()
                                                        + " " + currentEvent.getCountry()
                                                        + " (" + currentEvent.getYear() + ")"));
                                if(getActivity().getClass().equals(MapActivity.class))
                                {
                                    if (currentEvent.getEventId().equals(Model.getSINGLETON().getChosenEvent().getEventId()))
                                    {
                                        marker.showInfoWindow();
                                    }
                                }
                            }
                        }
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
