package yukams.app.background_locator_2.provider

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.huawei.hms.location.*

class HuaweiLocationProviderClient(context: Context, override var listener: LocationUpdateListener?) : BLLocationProvider {
    private val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationCallback = LocationListener(listener)

    override fun removeLocationUpdates() {
        client.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates(request: LocationRequestOptions) {
        client.requestLocationUpdates(getLocationRequest(request), locationCallback, Looper.getMainLooper())
    }

    private fun getLocationRequest(request: LocationRequestOptions): LocationRequest {
        val locationRequest = LocationRequest.create()

        locationRequest.interval = request.interval
        locationRequest.fastestInterval = request.interval
        locationRequest.maxWaitTime = request.interval
        locationRequest.priority = request.accuracy
        locationRequest.smallestDisplacement = request.distanceFilter

        return locationRequest
    }
}

private class LocationListener(val listener: LocationUpdateListener?) : LocationCallback() {
    override fun onLocationResult(location: LocationResult) {
        listener?.onLocationUpdated(LocationParserUtil.getLocationMapFromLocation(location))
    }
}