package edu.cs240.byu.familymap.main.modelPackage;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Member;
import java.util.Iterator;
import java.util.List;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.activities.MapActivity;
import edu.cs240.byu.familymap.main.activities.PersonActivity;

/**
 * Created by zachhalvorsen on 4/1/16.
 */
public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder>
{
    List<String> familyMembers;
    Person mainPerson;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView name;
        TextView relationship;
        TextView gender;
        Person person;


        public ViewHolder(final View itemView)
        {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.family_card);
            name = (TextView)itemView.findViewById(R.id.family_name);
            relationship = (TextView)itemView.findViewById(R.id.family_relationship);
            gender = (TextView)itemView.findViewById(R.id.family_gender_char);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(itemView.getContext(), PersonActivity.class);
                    Model.getSINGLETON().setChosenPerson(person);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public FamilyAdapter(List<String> familyMembers, Person mainPerson)
    {
        this.familyMembers = familyMembers;
        this.mainPerson = mainPerson;
        Log.d("FamilyAdapter", String.valueOf(getItemCount()));
    }

    @Override
    public FamilyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_family_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FamilyAdapter.ViewHolder holder, int position)
    {
        Model model = Model.getSINGLETON();
        holder.person = model.getPeople().get(familyMembers.get(position));
        if(holder.person.getGender().equals("m"))
        {
            holder.gender.setText("\u2642");
            holder.gender.setTextColor(Color.BLUE);
        }
        else
        {
            holder.gender.setText("\u2640");
            holder.gender.setTextColor(Color.RED);
        }
        holder.name.setText(holder.person.getFirstName() + " " + holder.person.getLastName());
        holder.name.setTextColor(Color.BLACK);
        holder.relationship.setTextColor(Color.BLACK);
        if(mainPerson.getSpouseID().equals(holder.person.getPersonId()))
        {
            holder.relationship.setText("Spouse");
        }
        else if(mainPerson.getFatherID().equals(holder.person.getPersonId()))
        {
            holder.relationship.setText("Father");
        }
        else if(mainPerson.getMotherID().equals(holder.person.getPersonId()))
        {
            holder.relationship.setText("Mother");
        }
        else
        {
            holder.relationship.setText("Child");
        }

    }

    @Override
    public int getItemCount()
    {
        return familyMembers.size();
    }
}
