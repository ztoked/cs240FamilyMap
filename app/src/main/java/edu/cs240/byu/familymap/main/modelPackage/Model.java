package edu.cs240.byu.familymap.main.modelPackage;


import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    Map<String, Person> people;
    Map<String, Set<Event>> personEvents;
    Map<String, Event> events;
    Map<String, Float> eventColors;
    Map<String, Set<String>> FamilyTree;
    Set<String> MaternalAncestors;
    Set<String> PaternalAncestors;
    Settings settings;
    Set<String> filter; //Set of event descriptions
    Set<String> eventTypes; //Set that holds all event descriptions


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

    public Map<String, Set<Event>> getPersonEvents()
    {
        return personEvents;
    }

    public void setPersonEvents(Map<String, Set<Event>> personEvents)
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

    public Map<String, Set<String>> getFamilyTree()
    {
        return FamilyTree;
    }

    public void setFamilyTree(Map<String, Set<String>> familyTree)
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
        Iterator<String> it = people.keySet().iterator();
        while (it.hasNext())
        {
            String personID = it.next();
            populateHelper(personID, personID);
        }
        MaternalAncestors.add(user.getMotherID());
        populateUsersSides(user.getMotherID(), MaternalAncestors);
        MaternalAncestors.add(user.getFatherID());
        populateUsersSides(user.getFatherID(), PaternalAncestors);
    }

    private void populateHelper(String originalPerson, String personID)
    {
        if(personID == null || !people.containsKey(personID))
        {
            return;
        }
        if(!FamilyTree.containsKey(personID))
        {
            FamilyTree.put(personID, new HashSet<String>());
        }
        //Log.d("personID = ", personID);
        if(people.get(personID).getFatherID() != null)
        {
            String father = people.get(personID).getFatherID();
            FamilyTree.get(originalPerson).add(father);
            populateHelper(originalPerson, father);
        }

        if(people.get(personID).getMotherID() != null)
        {
            String mother = people.get(personID).getMotherID();
            FamilyTree.get(originalPerson).add(mother);
            populateHelper(originalPerson, mother);
        }
    }

    private void populateUsersSides(String personID, Set<String> ancestors)
    {
        if(personID == null || !people.containsKey(personID))
        {
            return;
        }

        if(people.get(personID).getFatherID() != null)
        {
            String father = people.get(personID).getFatherID();
            ancestors.add(father);
            populateUsersSides(father, ancestors);
        }
        //Log.d("populateUsersSides", "finished some fathers");
        if(people.get(personID).getMotherID() != null)
        {
            String mother = people.get(personID).getMotherID();
            ancestors.add(mother);
            populateUsersSides(mother, ancestors);
        }
    }
}
