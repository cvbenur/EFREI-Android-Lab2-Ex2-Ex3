package fr.rnabet.flickrapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import fr.rnabet.flickrapp.adapter.MyAdapter;
import fr.rnabet.flickrapp.R;
import fr.rnabet.flickrapp.async.AsyncFlickrJSONDataForList;

public class ListActivity extends AppCompatActivity {

    // Attributes
    private MyAdapter _adapter = new MyAdapter(this);
    private ListView _listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        // Retrieve ListView reference and set adapter to custom MyAdapter
        this._listView = (ListView) findViewById(R.id.list_list_view);
        this._listView.setAdapter(this._adapter);


        // Launch AsyncTask to query and display images list
        AsyncFlickrJSONDataForList listLoader = new AsyncFlickrJSONDataForList(this._adapter);
        listLoader.execute(MainActivity.getUrl());
    }
}