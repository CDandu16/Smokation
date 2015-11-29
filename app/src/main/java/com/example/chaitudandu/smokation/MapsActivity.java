package com.example.chaitudandu.smokation;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<ParseObject> smokerLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        //Retrieve lat long from Parse

        ParseQuery<ParseObject> latLongquery = new ParseQuery<ParseObject>("smokerLocation");
        latLongquery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.i("score", "Retrieved " + objects.size() + " scores");
                    smokerLocations = objects;
                    //goes through the list of parse objects containing lat and long
                    for(int i=0;i<smokerLocations.size();i++){
                        //draws spots to map
                        LatLng marker = new LatLng(smokerLocations.get(i).getDouble("latitude"),smokerLocations.get(i).getDouble("longitude"));
                        mMap.addMarker(new MarkerOptions().position(marker).title("Marker in Sydney"));
                    }
                } else {
                    Log.i("score", "Error: " + e.getMessage());
                    Toast.makeText(MapsActivity.this, "Error retrieving", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i("score","works here");



        // Add a marker in Sydney and move the camera

    }

/////////Don't write anyting in there write everything in OnCreate
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
