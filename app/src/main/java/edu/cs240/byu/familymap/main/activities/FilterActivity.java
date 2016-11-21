package edu.cs240.byu.familymap.main.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    private void initializeData()
    {
        Set<String> eventTypes = Model.getSINGLETON().getEventTypes();
        eventData = new ArrayList<>();
        for (String eventType : eventTypes)
        {
            eventData.add(eventType);
        }
        eventData.add("male");
        eventData.add("female");
        eventData.add("paternal");
        eventData.add("maternal");
    }

    private void initializeAdapter()
    {
        FilterAdapter adapter = new FilterAdapter(eventData);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.settings_back_button)
        {
            finish();
            return true;
        }
        return false;
    }
}
