package com.example.oliveiras.findpic.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.oliveiras.findpic.Activities.Adapter.ImageResultsAdapter;
import com.example.oliveiras.findpic.FilterDialog;
import com.example.oliveiras.findpic.Models.ImageResult;
import com.example.oliveiras.findpic.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    private String searchExtraParams = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        //1. creates the new data source
        imageResults = new ArrayList<ImageResult>();
        //2. attaches the data to the adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        //3. link the adapter to the result view
        gvResults.setAdapter(aImageResults);
        gvResults.setOnScrollListener(new EndlessScroller() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    //call the api to get more results when scrolling
    public void customLoadMoreDataFromApi(int offset) {
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8&start=" + (8 * offset) + searchExtraParams;
        Log.d("debug", searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    //imageResults.clear(); //clear the past results in the array (to start a new search only!)
                    //this way gets the image result inside the array, but...
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    //we can update the array directly from the adapter!
                    //this way, when updating the adapter, it already modifies the data inside the array
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //connecting variables with layout
    private void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the Full Image activity:
                //1. Create an Intent
                Intent i = new Intent(MainActivity.this, FullImageActivity.class);
                //2. Get an image result to display
                ImageResult result = imageResults.get(position);
                //Pass the image result to the intent
                i.putExtra("url", result.fullUrl);
                //Display the new activity
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //function to trigger when search button is pressed
    public void onImageSearch(View v){
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8" + searchExtraParams;
        client.get(searchUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); //clear the past results in the array (to start a new search only!)
                    //this way gets the image result inside the array, but...
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    //we can update the array directly from the adapter!
                    //this way, when updating the adapter, it already modifies the data inside the array
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.filters) {
            //here I will launch the dialog
            showFiltersDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFiltersDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        FilterDialog newFragment = new FilterDialog();
        newFragment.show(ft, "dialog");
    }

    private final static String[] API_COLOR_PARAMS = new String[] {
            null,
            "imgcolor=black",
            "imgcolor=blue",
            "imgcolor=brown",
            "imgcolor=gray",
            "imgcolor=green",
            "imgcolor=orange",
            "imgcolor=pink",
            "imgcolor=purple",
            "imgcolor=red",
            "imgcolor=teal",
            "imgcolor=white",
            "imgcolor=yellow"
    };

    private final static String[] API_SIZE_PARAMS = new String[] {
            null,
            "imgsz=small",
            "imgsz=medium",
            "imgsz=large",
            "imgsz=xlarge"
    };

    private final static String[] API_TYPE_PARAMS = new String[] {
            null,
            "imgtype=face",
            "imgtype=photo",
            "imgtype=clipart",
            "imgtype=lineart"
    };


    // Called from FilterDialog when user confirms selection of filters
    public void onParametersChanged(int color, int size, int type) {
        searchExtraParams = "&";
        if (color > 0) {
            searchExtraParams += API_COLOR_PARAMS[color] + "&";
        }
        if (size > 0) {
            searchExtraParams += API_SIZE_PARAMS[size] + "&";
        }
        if (type > 0) {
            searchExtraParams += API_TYPE_PARAMS[type] + "&";
        }

        searchExtraParams = searchExtraParams.substring(0, searchExtraParams.length() - 1);
        onImageSearch(null);
    }
}
