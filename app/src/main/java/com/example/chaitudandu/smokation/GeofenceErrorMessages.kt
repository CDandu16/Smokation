package com.example.chaitudandu.smokation

import android.content.Context
import android.content.res.Resources

import com.google.android.gms.location.GeofenceStatusCodes

/**
 * Created by Chaitu Dandu on 11/29/2015.
 */
class GeofenceErrorMessages {



    fun getErrorString(context: Context, errorCode: Int): String {
        val mResources = context.getResources()
        when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> return mResources.getString(R.string.geofence_not_available)
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> return mResources.getString(R.string.geofence_too_many_geofences)
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> return mResources.getString(R.string.geofence_too_many_pending_intents)
            else -> return mResources.getString(R.string.unknown_geofence_error)
        }
    }
}
/**
 * Prevents instantiation.
 */