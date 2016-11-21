package edu.cs240.byu.familymap.main.modelPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.activities.MapActivity;
import edu.cs240.byu.familymap.main.activities.PersonActivity;

/**
 * Created by zachhalvorsen on 4/1/16
 */
public class LifeEventAdapter extends RecyclerView.Adapter<LifeEventAdapter.ViewHolder>
{
    String[] events;
    Person person;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView eventDescription;
        TextView name;
        ImageView icon;
        Event event;

        public ViewHolder(final View itemView)
        {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.event_card);
            eventDescription = (TextView)itemView.findViewById(R.id.event_card_description);
            name = (TextView)itemView.findViewById(R.id.event_person_name);
            icon = (ImageView)itemView.findViewById(R.id.event_icon);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(itemView.getContext(), MapActivity.class);
                    Model.getSINGLETON().setChosenEvent(event);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public LifeEventAdapter(String[] eventData, Person person)
    {
        events = eventData;
        this.person = person;
        Log.d("LifeEventAdapter", String.valueOf(getItemCount()));
    }

    @Override
    public LifeEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LifeEventAdapter.ViewHolder holder, int position)
    {
        holder.event = Model.getSINGLETON().getEvents().get(events[position]);
        holder.eventDescription.setText(holder.event.getDescription()
                + ": " + holder.event.getCity()
                + " " + holder.event.getCountry()
                + " (" + holder.event.getYear() + ")");
        holder.eventDescription.setTextColor(Color.BLACK);
        holder.name.setText(person.getFirstName() + " " + person.getLastName());
        holder.name.setTextColor(Color.BLACK);
    }

    @Override
    public int getItemCount()
    {
        return events.length;
    }
}