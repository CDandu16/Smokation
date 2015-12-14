package com.example.chaitudandu.smokation;

import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Utils.Smokation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<ParseObject> smokerLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        String myUrl = "http://45.55.156.205:8000";
        GetSmokationsRequest getRequest = new GetSmokationsRequest();
        ArrayList<Smokation> smokerLocations = new ArrayList<Smokation>();
        try {
            smokerLocations = getRequest.execute(myUrl).get();
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }

        //goes through the list of parse objects containing lat and long
        for (int i = 0; i < smokerLocations.size(); i++) {
            //draws spots to map
            LatLng marker = new LatLng(smokerLocations.get(i).getLatitude(), smokerLocations.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(marker).title("Smoking Marker"));
        }

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

    public class GetSmokationsRequest extends AsyncTask<String, Void, ArrayList<Smokation>> {

        @Override
        protected ArrayList<Smokation> doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            ArrayList<Smokation> smokations = new ArrayList<Smokation>();
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("RequestType", "getSmokations");
                connection.connect();
                ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
                Smokation smoke;
                Object o;
                int counter = input.readInt();
                Log.d("hello", "dddd" + counter);
                for (int i = 0; i < counter; i++) {
                    Log.d("hello", "" + i);
                    smoke = new Smokation(input.readDouble(), input.readDouble());
                    smokations.add(smoke);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //prints out smoker location
            return smokations;
        }

    }
}
