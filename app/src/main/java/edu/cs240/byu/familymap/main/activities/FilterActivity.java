package edu.cs240.byu.familymap.main.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.FilterAdapter;
import edu.cs240.byu.familymap.main.modelPackage.Model;

public class FilterActivity extends AppCompatActivity
{
    private RecyclerView rv;
    private List<String> eventData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        rv = (RecyclerView)findViewById(R.id.filter_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        if (rv != null)
        {
            rv.setLayoutManager(llm);
        }

        initializeData();
        initializeAdapter();
    }

    private void initializeAdapter()
    {
        FilterAdapter adapter = new FilterAdapter(eventData);
        rv.setAdapter(adapter);
    }

    private void initializeData()
    {
        Set<String> eventTypes = Model.getSINGLETON().getEventTypes();
        eventData = new ArrayList<>();
        for (String eventType : eventTypes)
        {
            eventData.add(eventType);
        }
    }

}
