package com.example.chaitudandu.smokation

/**
 * Takes users location in the background
 */

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class MyService : Service() {
    private var mLocationManager: LocationManager? = null

    private inner class LocationListener(provider: String) : android.location.LocationListener {
        internal var mLastLocation: Location

        init {
            Log.e(TAG, "LocationListener " + provider)
            mLastLocation = Location(provider)

            //send location to mainActivity
            val intent = Intent()
            intent.setAction(MY_ACTION)
            intent.putExtra("Location", mLastLocation)
            sendBroadcast(intent)
        }

        @Override
        fun onLocationChanged(location: Location) {
            Log.e(TAG, "onLocationChanged: " + location.toString())
            mLastLocation.set(location)

        }

        @Override
        fun onProviderDisabled(provider: String) {
            Log.e(TAG, "onProviderDisabled: " + provider)
        }

        @Override
        fun onProviderEnabled(provider: String) {
            Log.e(TAG, "onProviderEnabled: " + provider)
        }

        @Override
        fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e(TAG, "onStatusChanged: " + provider)
        }
    }

    internal var mLocationListeners = arrayOf<LocationListener>(LocationListener(LocationManager.GPS_PROVIDER), LocationListener(LocationManager.NETWORK_PROVIDER))

    @Override
    fun onBind(arg0: Intent): IBinder? {
        return null
    }

    @Override
    fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    @Override
    fun onCreate() {
        Log.e(TAG, "onCreate")
        initializeLocationManager()
        try {
            mLocationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1])
        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage())
        }

        try {
            Toast.makeText(this@MyService, "It Works", Toast.LENGTH_SHORT).show()
            mLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0])
        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage())
        }

    }

    @Override
    fun onDestroy() {
        Log.e(TAG, "onDestroy")
        super.onDestroy()
        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: SecurityException) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex)
                }

            }
        }
    }

    private fun initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager")
        if (mLocationManager == null) {
            mLocationManager = getApplicationContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    companion object {
        private val TAG = "Hello"
        private val LOCATION_INTERVAL = 10//change this for time
        private val LOCATION_DISTANCE = 0f//change this to get it every distance
        private val MY_ACTION = "MY_ACTION"
    }
}
