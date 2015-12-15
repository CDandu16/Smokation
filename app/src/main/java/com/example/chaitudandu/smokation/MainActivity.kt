package com.example.chaitudandu.smokation

import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.parse.Parse
import com.parse.ParseObject


import Utils.APIKeys

import android.content.Intent
import android.util.Log

import java.io.IOException
import java.io.ObjectInputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList
import java.util.concurrent.ExecutionException


import Utils.Smokation
import im.delight.android.location.SimpleLocation

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {




    /**
     * Provides the entry point to Google Play services.
     */
    lateinit protected  var mGoogleApiClient: GoogleApiClient
    /**
     * The list of geofences used in this sample.
     */
    lateinit protected var mGeofenceList: ArrayList<Geofence>

    /**
     * Used to keep track of whether geofences were added.
     */
    private var mGeofencesAdded: Boolean = false

    /**
     * Used when requesting to add or remove geofences.
     */
    private var mGeofencePendingIntent: PendingIntent? = null

    /**
     * Used to persist application state about whether geofences were added.
     */
    private var mSharedPreferences: SharedPreferences? = null

    private var mAddGeofencesButton: Button? = null
    private var mRemoveGeofencesButton: Button? = null

    //open map

    /**
     * Called when the user clicks the map button
     */
    fun openMap(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Sets views for Android
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        //Some url endpoint that you may have
        val myUrl = "http://45.55.156.205:8000"
        //String to place our result in
        var result = ArrayList<Smokation>()
        //Instantiate new instance of our class
        val getRequest = GetSmokationsRequest()
        //Perform the doInBackground method, passing in our url

        var location: SimpleLocation = SimpleLocation(this)
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this)
        }

        try {
            result = getRequest.execute(myUrl).get()
            Log.d("hello", result.toString())
        } catch (e: InterruptedException) {

        } catch (e: ExecutionException) {

        }

        mAddGeofencesButton = findViewById(R.id.add_geofences_button) as Button
        mRemoveGeofencesButton = findViewById(R.id.remove_geofences_button) as Button
        mGeofenceList = ArrayList<Geofence>()
        mGeofencePendingIntent = null
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE)

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences!!.getBoolean(Constants.GEOFENCES_ADDED_KEY, false)
        populateGeofenceList(result)

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient()

    }


    fun create(v: View) {
        when (v.id) {
            R.id.addSmokation -> try {
                val slocation = SimpleLocation(this, true)
                if (!slocation.hasLocationEnabled()) {
                    // ask the user to enable location access
                    SimpleLocation.openSettings(this)
                }
                val result = AddSmokationRequest(this).execute(java.lang.Double.toString(slocation.latitude), java.lang.Double.toString(slocation.longitude)).get()
                Toast.makeText(this@MainActivity, result, Toast.LENGTH_SHORT).show()
            } catch (e: InterruptedException) {

            } catch (e: ExecutionException) {

            }

        }
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

    @Synchronized protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        mGoogleApiClient.disconnect()
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    override fun onConnected(connectionHint: Bundle) {
        Log.i(TAG, "Connected to GoogleApiClient")
    }

    override fun onConnectionFailed(result: ConnectionResult?) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
    }

    override fun onConnectionSuspended(cause: Int) {
        // The connection to Google Play services was lost for some reason.
        Log.i(TAG, "Connection suspended")

        // onConnected() will be called again automatically when the service reconnects
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
            // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
            // is already inside that geofence.
            // Add the geofences to be monitored by geofencing service.
            // Return a GeofencingRequest.
    val geofencingRequest: GeofencingRequest
        get() {
            val builder = GeofencingRequest.Builder()
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            builder.addGeofences(mGeofenceList)
            return builder.build()
        }

    fun addGeofencesButtonHandler(view: View) {
        if (!mGoogleApiClient.isConnected) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show()
            return
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    geofencingRequest,
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    geofencePendingIntent).setResultCallback(this) // Result processed in onResult().
        } catch (securityException: SecurityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException)
        }

    }

    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    fun removeGeofencesButtonHandler(view: View) {
        if (!mGoogleApiClient.isConnected) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show()
            return
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    geofencePendingIntent).setResultCallback(this) // Result processed in onResult().
        } catch (securityException: SecurityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException)
        }

    }

    private fun logSecurityException(securityException: SecurityException) {
        Log.e(TAG, "Invalid location permission. " + "You need to use ACCESS_FINE_LOCATION with geofences", securityException)
    }

    /**
     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
     * Either method can complete successfully or with an error.
     *
     *
     * Since this activity implements the [ResultCallback] interface, we are required to
     * define this method.

     * @param status The Status returned through a PendingIntent when addGeofences() or
     * *               removeGeofences() get called.
     */
    override fun onResult(status: Status) {
        if (status.isSuccess) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded
            val editor = mSharedPreferences!!.edit()
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded)
            editor.apply()

        } else {
            // Get the status code for the error and log it using a user-friendly message.
            val geoError = GeofenceErrorMessages()
            val errorMessage = geoError.getErrorString(this,
                    status.statusCode)
            Log.e(TAG, errorMessage)
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.

     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private // Reuse the PendingIntent if we already have it.
            // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
            // addGeofences() and removeGeofences().
    val geofencePendingIntent: PendingIntent
        get() {
            if (mGeofencePendingIntent != null) {
                return mGeofencePendingIntent as PendingIntent
            }
            val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
            return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    fun populateGeofenceList(smokers: ArrayList<Smokation>) {
        for (i in smokers.indices) {
            Constants.BAY_AREA_LANDMARKS.put("Smoker Location", LatLng(smokers[i].latitude, smokers[i].longitude))
        }
        for (entry in Constants.BAY_AREA_LANDMARKS.entries) {
            //for (int i = 0;i<smokers.size();i++) {
            Log.d("test", "Smoker Location")
            mGeofenceList.add(Geofence.Builder().setRequestId// Set the request ID of the geofence. This is a string to identify this
            // geofence.
            ("Smoker Location").setCircularRegion// Set the circular region of this geofence.
            (
                    entry.value.latitude,
                    entry.value.longitude,
                    Constants.GEOFENCE_RADIUS_IN_METERS).setExpirationDuration// Set the expiration duration of the geofence. This geofence gets automatically
            // removed after this period of time.
            (Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS).setTransitionTypes// Set the transition types of interest. Alerts are only generated for these
            // transition. We track entry and exit transitions in this sample.
            (Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT).build// Create the geofence.
            ())
        }
    }

    /**
     * Ensures that only one button is enabled at any time. The Add Geofences button is enabled
     * if the user hasn't yet added geofences. The Remove Geofences button is enabled if the
     * user has added geofences.
     */
    private fun setButtonsEnabledState() {
        if (mGeofencesAdded) {
            mAddGeofencesButton!!.isEnabled = false
            mRemoveGeofencesButton!!.isEnabled = true
        } else {
            mAddGeofencesButton!!.isEnabled = true
            mRemoveGeofencesButton!!.isEnabled = false
        }
    }

    private inner class GetSmokationsRequest : AsyncTask<String, Void, ArrayList<Smokation>>() {

        override fun doInBackground(vararg params: String): ArrayList<Smokation> {
            val stringUrl = params[0]
            val result: String
            val smokations = ArrayList<Smokation>()
            try {
                val url = URL(stringUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.setRequestProperty("RequestType", "getSmokations")
                connection.connect()
                val input = ObjectInputStream(connection.inputStream)
                var smoke: Smokation
                val o: Any
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

    private inner class AddSmokationRequest(internal var context:

                                            Context) : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            val lat = params[0]
            val longi = params[1]
            var response = "location not submitted"
            try {
                val url = URL("http://45.55.156.205:8000")
                val connection = url.openConnection() as HttpURLConnection
                connection.setRequestProperty("RequestType", "addSmokation")
                connection.addRequestProperty("Latitude", lat)
                connection.addRequestProperty("Longitude", longi)
                connection.connect()
                val input = ObjectInputStream(connection.inputStream)
                response = input.readUTF()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return response
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
        }

    }

    companion object {
        protected val TAG = "MainActivity"
    }
}
