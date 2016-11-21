package edu.cs240.byu.familymap.main.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.cs240.byu.familymap.R;
import edu.cs240.byu.familymap.main.modelPackage.Event;
import edu.cs240.byu.familymap.main.modelPackage.Model;
import edu.cs240.byu.familymap.main.modelPackage.Person;
import edu.cs240.byu.familymap.main.modelPackage.SearchAdapter;

public class SearchActivity extends AppCompatActivity
{
    String[] results;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d("SEARCH ACTIVITY", "on create");
        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        if (searchView != null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    doSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText)
                {
                    //Log.d("SEARCH ACTIVITY", "text change = " + newText);
                    return false;
                }
            });
        }
    }

    private void handleIntent(Intent intent)
    {
        Log.d("SEARCH ACTIVITY", "handle intent");
        if(Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String query)
    {
        Log.d("SEARCH ACTIVITY", "do search");
        rv = (RecyclerView)findViewById(R.id.search_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        if (rv != null)
        {
            rv.setLayoutManager(llm);
        }
        getResults(query);
        initializeSearchAdapter();
    }

    private void initializeSearchAdapter()
    {
        Log.d("SEARCH ACTIVITY", "initialize search adapter");
        SearchAdapter adapter = new SearchAdapter(results);
        rv.setAdapter(adapter);
    }

    private void getResults(String query)
    {
        Log.d("SEARCH ACTIVITY", "get results");
        Model model = Model.getSINGLETON();
        List<String> resultList = new ArrayList<>();
        for(String personID : model.getPeople().keySet())
        {
            Person person = model.getPeople().get(personID);
            if(person.getFirstName().toLowerCase().contains(query.toLowerCase()))
            {
                resultList.add(personID);
            }
            else if(person.getLastName().toLowerCase().contains(query.toLowerCase()))
            {
                resultList.add(personID);
            }
        }
        for(String eventID : model.getEvents().keySet())
        {
            Event event = model.getEvents().get(eventID);
            if(event.getYear().contains(query))
            {
                resultList.add(eventID);
            }
            else if(event.getDescription().toLowerCase().contains(query.toLowerCase()))
            {
                resultList.add(eventID);
            }
            else if(event.getCity().toLowerCase().contains(query.toLowerCase()))
            {
                resultList.add(eventID);
            }
            else if(event.getCountry().toLowerCase().contains(query.toLowerCase()))
            {
                resultList.add(eventID);
            }
        }
        results = new String[resultList.size()];
        for(int i = 0; i < resultList.size(); i++)
        {
            results[i] = resultList.get(i);
        }
        Log.d("SEARCH ACTIVITY", "number of results = " + results.length);
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
