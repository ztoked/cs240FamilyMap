package edu.cs240.byu.familymap.main.modelPackage;


import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zachhalvorsen on 3/21/16.
 */
public class Model
{
    //Singleton pattern
    private static Model SINGLETON;
    public static void initialize()
    {
        SINGLETON = new Model();
    }

/*
    People:Map<PersonID, Person>
    Events:Map<EventID, Event>
    Person Events:Map<PersonID, List<Event>>
    Event Types: Set<EventType>
    Event type colors: Map<EventType, MapColor>
    User:Person
    Family Tree: Map<PersonID, Set<PersonID>> (Map parents to their children
    Maternal Ancestors: Set<PersonID> (All ancestors on mother's side)
    Paternal Ancestors: Set<PersonID> (All ancestors on father's side)
    Model(400)
    Model Test(300)
    Map Fragment(445)
    Map Activity(64)
    Person Activity(190) / Person Recycler Adapter(279)
    Settings Activity(267)
    Search Activity(127) / Search Recycler Adapter(77)
    Filter Activity(122) / Filter Recycler Adapter(139)
 */

    LoginInfo login;
    String authCode;
    Person user;
    String userPersonID;
    Map<String, Person> people;
    Map<String, Set<String>> personEvents;
    Map<String, Event> events;
    Map<String, Float> eventColors;
    Map<String, List<String>> FamilyTree;
    Set<String> MaternalAncestors;
    Set<String> PaternalAncestors;
    Settings settings;
    Set<String> filter; //Set of event descriptions
    Set<String> eventTypes; //Set that holds all event descriptions
    Person chosenPerson;
    Event chosenEvent;
    boolean showMaleEvents = true;
    boolean showFemaleEvents = true;
    boolean showPaternalEvents = true;
    boolean showMaternalEvents = true;


    Model()
    {
        people = new HashMap<>();
        FamilyTree = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        MaternalAncestors = new HashSet<>();
        PaternalAncestors = new HashSet<>();
        eventColors = new HashMap<>();
        settings = new Settings();
        filter = new HashSet<>();
        eventTypes = new HashSet<>();
    }

    public String getUserPersonID()
    {
        return userPersonID;
    }

    public void setUserPersonID(String userPersonID)
    {
        this.userPersonID = userPersonID;
    }

    public static Model getSINGLETON()
    {
        return SINGLETON;
    }

    public static void setSINGLETON(Model SINGLETON)
    {
        Model.SINGLETON = SINGLETON;
    }

    public LoginInfo getLogin()
    {
        return login;
    }

    public void setLogin(LoginInfo login)
    {
        this.login = login;
    }

    public String getAuthCode()
    {
        return authCode;
    }

    public void setAuthCode(String authCode)
    {
        this.authCode = authCode;
    }

    public Person getUser()
    {
        return user;
    }

    public void setUser(Person user)
    {
        this.user = user;
    }

    public Map<String, Person> getPeople()
    {
        return people;
    }

    public void setPeople(Map<String, Person> people)
    {
        this.people = people;
    }

    public Map<String, Set<String>> getPersonEvents()
    {
        return personEvents;
    }

    public void setPersonEvents(Map<String, Set<String>> personEvents)
    {
        this.personEvents = personEvents;
    }

    public Map<String, Event> getEvents()
    {
        return events;
    }

    public void setEvents(Map<String, Event> events)
    {
        this.events = events;
    }

    public Map<String, List<String>> getFamilyTree()
    {
        return FamilyTree;
    }

    public void setFamilyTree(Map<String, List<String>> familyTree)
    {
        FamilyTree = familyTree;
    }

    public Set<String> getMaternalAncestors()
    {
        return MaternalAncestors;
    }

    public void setMaternalAncestors(Set<String> maternalAncestors)
    {
        MaternalAncestors = maternalAncestors;
    }

    public Set<String> getPaternalAncestors()
    {
        return PaternalAncestors;
    }

    public void setPaternalAncestors(Set<String> paternalAncestors)
    {
        PaternalAncestors = paternalAncestors;
    }

    public Map<String, Float> getEventColors()
    {
        return eventColors;
    }

    public Settings getSettings()
    {
        return settings;
    }

    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    public void setEventColors(Map<String, Float> eventColors)
    {
        this.eventColors = eventColors;
    }

    public Set<String> getFilter()
    {
        return filter;
    }

    public void setFilter(Set<String> filter)
    {
        this.filter = filter;
    }

    public Set<String> getEventTypes()
    {
        return eventTypes;
    }

    public void setEventTypes(Set<String> eventTypes)
    {
        this.eventTypes = eventTypes;
    }

    public Person getChosenPerson()
    {
        return chosenPerson;
    }

    public void setChosenPerson(Person chosenPerson)
    {
        this.chosenPerson = chosenPerson;
    }

    public Event getChosenEvent()
    {
        return chosenEvent;
    }

    public void setChosenEvent(Event chosenEvent)
    {
        this.chosenEvent = chosenEvent;
    }

    public boolean getShowMaleEvents()
    {
        return showMaleEvents;
    }

    public void setShowMaleEvents(boolean showMaleEvents)
    {
        this.showMaleEvents = showMaleEvents;
    }

    public boolean getShowFemaleEvents()
    {
        return showFemaleEvents;
    }

    public void setShowFemaleEvents(boolean showFemaleEvents)
    {
        this.showFemaleEvents = showFemaleEvents;
    }

    public boolean getShowPaternalEvents()
    {
        return showPaternalEvents;
    }

    public void setShowPaternalEvents(boolean showPaternalEvents)
    {
        this.showPaternalEvents = showPaternalEvents;
    }

    public boolean getShowMaternalEvents()
    {
        return showMaternalEvents;
    }

    public void setShowMaternalEvents(boolean showMaternalEvents)
    {
        this.showMaternalEvents = showMaternalEvents;
    }

    public Event getSelectedEvent(Marker marker)
    {
        LatLng position = marker.getPosition();
        Set<String> eventIDs = events.keySet();
        Iterator<String> idIterator = eventIDs.iterator();
        while(idIterator.hasNext())
        {
            Event currentEvent = events.get(idIterator.next());
            if(position.equals(currentEvent.getPosition()))
            {
                return currentEvent;
            }
        }
        return null;
    }

    public void populateFamily()
    {
        makeUserYoungestMember();
        if(!user.getMotherID().equals(""))
        {
            MaternalAncestors.add(user.getMotherID());
            populateUsersSides(user.getMotherID(), MaternalAncestors);
        }
        if(!user.getFatherID().equals(""))
        {
            MaternalAncestors.add(user.getFatherID());
            populateUsersSides(user.getFatherID(), PaternalAncestors);
        }
    }

    private void populateUsersSides(String personID, Set<String> ancestors)
    {
        if(!people.get(personID).getFatherID().equals(""))
        {
            String father = people.get(personID).getFatherID();
            ancestors.add(father);
            populateUsersSides(father, ancestors);
        }
        if(!people.get(personID).getMotherID().equals(""))
        {
            String mother = people.get(personID).getMotherID();
            ancestors.add(mother);
            populateUsersSides(mother, ancestors);
        }
    }

    private void makeUserYoungestMember()
    {

    }

    public void orderEvents(String[] arr)
    {
        int i, j;
        String newValue;
        for (i = 1; i < arr.length; i++)
        {
            newValue = arr[i];
            j = i;
            while (j > 0 && Integer.parseInt(events.get(arr[j - 1]).getYear()) > Integer.parseInt(events.get(newValue).getYear()))
            {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = newValue;
        }
    }

    public void clear()
    {
        people = new HashMap<>();
        FamilyTree = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        MaternalAncestors = new HashSet<>();
        PaternalAncestors = new HashSet<>();
        eventColors = new HashMap<>();
        settings = new Settings();
        filter = new HashSet<>();
        eventTypes = new HashSet<>();
    }
}
