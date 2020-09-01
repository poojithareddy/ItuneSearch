package com.poojitha.itunes;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poojitha.itunes.model.ItuneResponse;
import com.poojitha.itunes.model.Ituns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private ItunesAdapter itunesAdapter;
    private  List<Ituns> itunes=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.itunes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itunesAdapter = new ItunesAdapter(itunes, R.layout.list_item_itune, getApplicationContext());
        recyclerView.setAdapter(itunesAdapter);
        Call<ItuneResponse> call = apiService.getItunes("all");
        call.enqueue(new Callback<ItuneResponse>() {
            @Override
            public void onResponse(Call<ItuneResponse> call, Response<ItuneResponse> response) {
                List<Ituns> itunesTemp = response.body().getResults();
                itunes.clear();
                itunes.addAll(itunesTemp);
                Collections.sort(itunes, new FishNameComparator());
                itunesAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<ItuneResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e("Main", t.toString());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                itunesAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                itunesAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FishNameComparator implements Comparator<Ituns>
    {
        public int compare(Ituns left, Ituns right) {
            return left.getReleaseDate().compareTo(right.getReleaseDate());
        }
    }

}