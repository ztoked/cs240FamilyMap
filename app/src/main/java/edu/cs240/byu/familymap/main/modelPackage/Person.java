package edu.cs240.byu.familymap.main.modelPackage;

import java.util.Set;

/**
 * Created by zachhalvorsen on 3/21/16.
 */
public class Person
{
    private String descendant;
    private String personId;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    Person() {}

    public Person(String descendant, String personId, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID)
    {
        this.descendant = descendant;
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getDescendant()
    {
        return descendant;
    }

    public void setDescendant(String descendant)
    {
        this.descendant = descendant;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getFatherID()
    {
        return fatherID;
    }

    public void setFatherID(String fatherID)
    {
        this.fatherID = fatherID;
    }

    public String getMotherID()
    {
        return motherID;
    }

    public void setMotherID(String motherID)
    {
        this.motherID = motherID;
    }

    public String getSpouseID()
    {
        return spouseID;
    }

    public void setSpouseID(String spouseID)
    {
        this.spouseID = spouseID;
    }

    public Event firstEvent()
    {
        Set<String> events = Model.getSINGLETON().getPersonEvents().get(personId);
        String[] eventArray = new String[events.size()];
        int i = 0;
        for (String s : events)
        {
            eventArray[i] = s;
            ++i;
        }
        Model.getSINGLETON().orderEvents(eventArray);
        return Model.getSINGLETON().getEvents().get(eventArray[0]);
    }
}
