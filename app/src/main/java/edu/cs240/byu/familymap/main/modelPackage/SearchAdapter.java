package edu.cs240.byu.familymap.main.modelPackage;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.activities.MapActivity;
import edu.cs240.byu.familymap.main.activities.PersonActivity;

/**
 * Created by zachhalvorsen on 4/4/16
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>
{
    String[] results;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView title;
        TextView subtitle;
        TextView genderIcon;
        ImageView markerImage;
        Person person;
        Event event;
        boolean isPerson;

        public ViewHolder(final View itemView)
        {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.search_card);
            title = (TextView)itemView.findViewById(R.id.search_title);
            subtitle = (TextView)itemView.findViewById(R.id.search_subtitle);
            genderIcon = (TextView)itemView.findViewById(R.id.search_gender_icon);
            markerImage = (ImageView)itemView.findViewById(R.id.search_event_icon);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(isPerson)
                    {
                        Intent intent = new Intent(itemView.getContext(), PersonActivity.class);
                        Model.getSINGLETON().setChosenPerson(person);
                        itemView.getContext().startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(itemView.getContext(), MapActivity.class);
                        Model.getSINGLETON().setChosenEvent(event);
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public SearchAdapter(String[] incomingResults)
    {
        results = incomingResults;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position)
    {
        Model model = Model.getSINGLETON();
        if(model.getPeople().containsKey(results[position]))
        {
            holder.isPerson = true;
            holder.markerImage.setVisibility(View.INVISIBLE);
            holder.person = model.getPeople().get(results[position]);
            if(holder.person.getGender().equals("m"))
            {
                holder.genderIcon.setText("\u2642");
                holder.genderIcon.setTextColor(Color.BLUE);
            }
            else
            {
                holder.genderIcon.setText("\u2640");
                holder.genderIcon.setTextColor(Color.RED);
            }
            holder.title.setText(holder.person.getFirstName() + " " + holder.person.getLastName());
            holder.subtitle.setText("");
        }
        else
        {
            holder.event = model.getEvents().get(results[position]);
            holder.isPerson = false;
            holder.markerImage.setVisibility(View.VISIBLE);
            holder.genderIcon.setText("");
            holder.title.setText(holder.event.getDescription()
                    + ": " + holder.event.getCity()
                    + " " + holder.event.getCountry()
                    + " (" + holder.event.getYear() + ")");
            Person currentPerson = model.getPeople().get(holder.event.getPersonId());
            holder.subtitle.setText(currentPerson.getFirstName() + " " + currentPerson.getLastName());
        }
    }

    @Override
    public int getItemCount()
    {
        return results.length;
    }
}
