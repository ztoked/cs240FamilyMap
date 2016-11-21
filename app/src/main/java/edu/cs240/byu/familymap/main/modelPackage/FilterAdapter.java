package edu.cs240.byu.familymap.main.modelPackage;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import edu.cs240.byu.familymap.R;

/**
 * Created by zachhalvorsen on 3/30/16.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder>
{
    List<String> eventTypeList;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView filterDescription;
        Switch filterSwitch;

        public TextView textView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.filter_card);
            filterDescription = (TextView)itemView.findViewById(R.id.filter_description);
            filterSwitch = (Switch)itemView.findViewById(R.id.filter_switch);
        }
    }

    public FilterAdapter(List<String> incomingData)
    {
        eventTypeList = incomingData;
    }

    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_card, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        holder.filterDescription.setText("View " + eventTypeList.get(position) + " events");
        if(Model.getSINGLETON().getFilter().contains(eventTypeList.get(position)))
        {
            holder.filterSwitch.setChecked(true);
        }
        else if(eventTypeList.get(position).equals("male"))
        {
            holder.filterSwitch.setChecked(Model.getSINGLETON().getShowMaleEvents());
        }
        else if(eventTypeList.get(position).equals("female"))
        {
            holder.filterSwitch.setChecked(Model.getSINGLETON().getShowFemaleEvents());
        }
        else if(eventTypeList.get(position).equals("paternal"))
        {
            holder.filterSwitch.setChecked(Model.getSINGLETON().getShowPaternalEvents());
        }
        else if(eventTypeList.get(position).equals("maternal"))
        {
            holder.filterSwitch.setChecked(Model.getSINGLETON().getShowMaternalEvents());
        }
        holder.filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(Model.getSINGLETON().getEventTypes().contains(eventTypeList.get(position)))
                {
                    if (isChecked)
                    {
                        Model.getSINGLETON().getFilter().add(eventTypeList.get(position));
                    }
                    else
                    {
                        Model.getSINGLETON().getFilter().remove(eventTypeList.get(position));
                    }
                }
                else
                {
                    if(eventTypeList.get(position).equals("male"))
                    {
                        Model.getSINGLETON().setShowMaleEvents(isChecked);
                    }
                    else if(eventTypeList.get(position).equals("female"))
                    {
                        Model.getSINGLETON().setShowFemaleEvents(isChecked);
                    }
                    else if(eventTypeList.get(position).equals("paternal"))
                    {
                        Model.getSINGLETON().setShowPaternalEvents(isChecked);
                    }
                    else if(eventTypeList.get(position).equals("maternal"))
                    {
                        Model.getSINGLETON().setShowMaternalEvents(isChecked);
                    }
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return eventTypeList.size();
    }
}
