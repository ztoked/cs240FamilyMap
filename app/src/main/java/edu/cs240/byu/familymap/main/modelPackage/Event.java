package edu.cs240.byu.familymap.main.modelPackage;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by zachhalvorsen on 3/21/16.
 */
public class Event
{
    private String eventId;
    private String personId;
    private LatLng position;
    private String country;
    private String city;
    private String description;
    private String year;
    private String descendant;
    private float color;

    Event() {}

    public Event(String eventId, String personId, double latitude, double longitude, String country, String city, String description, String year, String descendant)
    {
        this.eventId = eventId;
        this.personId = personId;
        position = new LatLng(latitude, longitude);
        this.country = country;
        this.city = city;
        this.description = description;
        this.year = year;
        this.descendant = descendant;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public LatLng getPosition()
    {
        return position;
    }

    public void setPosition(LatLng position)
    {
        this.position = position;
    }

    public void setColor(float color)
    {
        this.color = color;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getDescendant()
    {
        return descendant;
    }

    public void setDescendant(String descendant)
    {
        this.descendant = descendant;
    }

    public float getColor()
    {
        return color;
    }

    public void setColor(Float color)
    {
        this.color = color;
    }
}
