package com.example.chaitudandu.smokation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import Utils.APIKeys;
import android.content.Intent;
import android.util.Log;
import im.delight.android.location.SimpleLocation;

class MainActivity extends AppCompatActivity
{
    public ParseObject smokerLocation;
    public SimpleLocation location;

    //open map
    /** Called when the user clicks the map button */
    public void openMap(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Sets views for Android
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //gps from Simple_Location Jar
        location = new SimpleLocation(this);
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        //Parse initialization
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APIKeys.Application_ID, APIKeys.Client_Key);
        smokerLocation = new ParseObject("smokerLocation");

        Button test = (Button)findViewById(R.id.button1);
        test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                smokerLocation.put("latitude", location.getLatitude());
                smokerLocation.put("longitude", location.getLongitude());
                smokerLocation.saveInBackground();
                //says if user submits position of course we should change it to when it actually uploads
                Toast.makeText(getApplicationContext(), "Spot Submitted", Toast.LENGTH_LONG).show();
                //output position to console
                Log.i("hello","lat" + location.getLatitude() + "long" + location.getLongitude());
                //Starts listening service
            }
        });
        //uncomemnt this line if you want to drain battery
        //startService(Intent(this, javaClass<MyService>()))

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
