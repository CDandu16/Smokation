package com.example.chaitudandu.smokation

import com.google.android.gms.maps.model.LatLng

import java.util.HashMap

/**
 * Created by Chaitu Dandu on 11/29/2015.
 */
object Constants {

    val PACKAGE_NAME = "com.google.android.gms.location.Geofence"

    val SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME"

    val GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY"

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    val GEOFENCE_EXPIRATION_IN_HOURS: Long = 12

    /**
     * For this sample, geofences expire after twelve hours.
     */
    val GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000
    val GEOFENCE_RADIUS_IN_METERS = 1609f // 1 mile, 1.6 km

    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    val BAY_AREA_LANDMARKS = HashMap<String, LatLng>()

    init {


        // house
        BAY_AREA_LANDMARKS.put("SFO", LatLng(42.575452, -83.1394288))

        // Googleplex.
        BAY_AREA_LANDMARKS.put("GOOGLE", LatLng(37.422611, -122.0840577))
    }


}
