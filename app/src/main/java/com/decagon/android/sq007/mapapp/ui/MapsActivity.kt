package com.decagon.android.sq007.mapapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.decagon.android.sq007.R
import com.decagon.android.sq007.mapapp.model.FemiLocationLogging
import com.decagon.android.sq007.mapapp.model.MyLocationLogging
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var myMap: GoogleMap
    private lateinit var femiMap: GoogleMap

    private lateinit var databaseRef: DatabaseReference
    private val LOCATION_PERMISSION_REQUEST = 1
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //fuse location api helps to get the precise location. it is coming from "google play-services-location" dependency
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        databaseRef = Firebase.database.reference
        databaseRef.addValueEventListener(getFemmiLocation())
    }


    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        femiMap = googleMap
        getLocationAccess()
        getFemmiLocation()
        femiMap.isMyLocationEnabled = true

        //delete asshole
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        }


    }

    // Use LocationRequest to get my location and LocationCallback to keep tracking my location
    private fun getMyLocationUpdates() {
        locationRequest = LocationRequest.create().apply {
//            interval = 20000
//            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//            maxWaitTime = 100
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation


                    databaseRef = Firebase.database.reference
                    val locationlogging = MyLocationLogging(location.latitude, location.longitude)
                    databaseRef.child("DennisLocation").setValue(locationlogging)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext,
                                "Locations written into the database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                "Error occured while writing the locations",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    // display my current location from call back location getter
                    val latLng = LatLng(location.latitude, location.longitude)
                    myMap.addMarker(MarkerOptions().position(latLng).title("Dennis"))
//                      .icon(bitmapDescriptorFromVector(applicationContext, R.mipmap.ic_dennis_round)))
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
                }
            }
        }
    }

        //FEMI LOCATION TRACKER
    private fun getFemmiLocation(): ValueEventListener {
        val logListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Could not read Femi Location",
                    Toast.LENGTH_LONG
                ).show()
            }

            //     @SuppressLint("LongLogTag")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    val femLiocationLogging =
                        dataSnapshot.child("FemiLocation").getValue(FemiLocationLogging::class.java)
                    var femiLat = femLiocationLogging?.Latitude
                    var femiLong = femLiocationLogging?.Longitude
                    //Log.d("Latitude of driver", driverLat.toString())
                    //    Log.d("Longitude read from database", driverLong.toString())

                    if (femiLat != null && femiLong != null) {
                        val femiLoc = LatLng(femiLat, femiLong)

                        femiMap.addMarker(MarkerOptions().position(femiLoc).title("Femi"))
                        femiMap.animateCamera(CameraUpdateFactory.newLatLngZoom(femiLoc, 20f))

                        Toast.makeText(
                            applicationContext,
                            "Femi Location accessed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
        return logListener
    }

    //get current location updates using FusedLocationProviderClient
    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    //Location access and permissions controls
    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                this@MapsActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            myMap.isMyLocationEnabled = true
            getMyLocationUpdates()
            startLocationUpdates()
        } else
            ActivityCompat.requestPermissions(
                this@MapsActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                getLocationAccess()
            } else {
                Toast.makeText(
                    this,
                    "Oga mi, You have not granted location access permission",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }


    // function that convert png and vector to mipmap for map icon usage
    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}