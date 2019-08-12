package com.pear2pear.car2car.gmaps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.pear2pear.car2car.activities.HomeActivity
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.*
import com.pear2pear.car2car.R
import kotlinx.android.synthetic.main.dialog_delete_account.view.*
import java.text.SimpleDateFormat
import java.util.*

class PositionPresenter(var context: Context) {

    var activity = context as HomeActivity

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    fun start() {
        mLocationRequest = LocationRequest()
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
        if (checkPermissionForLocation(context)) {
            startLocationUpdates()
        }
    }

    protected fun startLocationUpdates() { // Create the location request to start receiving updates

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.setInterval(INTERVAL)
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)  // new Google API SDK v11 uses getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {   // New location has now been determined
        mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")
        println("Updated at : " + sdf.format(date))
        HomeActivity.CurrentPosition.position = (LatLng(
            mLastLocation.latitude,
            mLastLocation.longitude
        ))   // You can now create a LatLng Object for use with maps
    }

    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    //da sistemare (rifiuto richiesta posizione)
    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    */

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {

                ActivityCompat.requestPermissions(
                    activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),  // Show the permission request
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(context)
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_account, null)
        builder.setView(mDialogView)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
        val alert: AlertDialog = builder.create()
        mDialogView.bt_confirm_delete_account.setOnClickListener {
            startActivityForResult(
                activity,
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 11, null
            )
        }
        mDialogView.bt_cancel_delete_account.setOnClickListener {
            alert.closeOptionsMenu()
        }
        alert.show()
    }
}