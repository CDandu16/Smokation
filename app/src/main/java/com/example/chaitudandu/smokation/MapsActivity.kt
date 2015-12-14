package com.example.chaitudandu.smokation

import android.os.AsyncTask
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery

import java.io.IOException
import java.io.ObjectInputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList
import java.util.concurrent.ExecutionException

import Utils.Smokation

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val smokerLocations: List<ParseObject>? = null

    @Override
    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mMap = (getFragmentManager().findFragmentById(R.id.map) as MapFragment).getMap()
        val myUrl = "http://45.55.156.205:8000"
        val getRequest = GetSmokationsRequest()
        var smokerLocations = ArrayList<Smokation>()
        try {
            smokerLocations = getRequest.execute(myUrl).get()
        } catch (e: InterruptedException) {

        } catch (e: ExecutionException) {

        }

        //goes through the list of parse objects containing lat and long
        for (i in 0..smokerLocations.size() - 1) {
            //draws spots to map
            val marker = LatLng(smokerLocations.get(i).getLatitude(), smokerLocations.get(i).getLongitude())
            mMap!!.addMarker(MarkerOptions().position(marker).title("Smoking Marker"))
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
    fun onMapReady(googleMap: GoogleMap) {

    }

    inner class GetSmokationsRequest : AsyncTask<String, Void, ArrayList<Smokation>>() {

        @Override
        protected fun doInBackground(vararg params: String): ArrayList<Smokation> {
            val stringUrl = params[0]
            val result: String
            val smokations = ArrayList<Smokation>()
            try {
                val url = URL(stringUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.setRequestProperty("RequestType", "getSmokations")
                connection.connect()
                val input = ObjectInputStream(connection.getInputStream())
                var smoke: Smokation
                val o: Object
                val counter = input.readInt()
                Log.d("hello", "dddd" + counter)
                for (i in 0..counter - 1) {
                    Log.d("hello", "" + i)
                    smoke = Smokation(input.readDouble(), input.readDouble())
                    smokations.add(smoke)
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //prints out smoker location
            return smokations
        }

    }
}
