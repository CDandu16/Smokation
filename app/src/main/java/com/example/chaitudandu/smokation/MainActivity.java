package com.example.chaitudandu.smokation

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast

import com.parse.Parse
import com.parse.ParseObject

import Utils.APIKeys
import android.content.Intent
import android.util.Log
import im.delight.android.location.SimpleLocation

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        //Sets views for Android
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        //gps from Simple_Location Jar
        var location: SimpleLocation = SimpleLocation(this)
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        //Parse initialization
        Parse.enableLocalDatastore(this)
        Parse.initialize(this, APIKeys.Application_ID, APIKeys.Client_Key)
        var smokerLocation: ParseObject = ParseObject("smokerLocation")

        val test = findViewById(R.id.button1) as Button
        test.setOnClickListener {
            smokerLocation.put("latitude", location.latitude)
            smokerLocation.put("longitude", location.longitude);
            smokerLocation.saveInBackground()
            //says if user submits position of course we should change it to when it actually uploads
            Toast.makeText(applicationContext, "Spot Submitted", Toast.LENGTH_LONG).show()
            //output position to console
            Log.i("hello","lat" + location.getLatitude() + "long" + location.longitude);
            //Starts listening service
        }
        //uncomemnt this line if you want to drain battery
        startService(Intent(this, javaClass<MyService>()))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
