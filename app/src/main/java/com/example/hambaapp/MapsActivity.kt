package com.example.hambaapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.hambaapp.Common.Common
import com.example.hambaapp.HambaBusiness.BusinessSettings
import com.example.hambaapp.Model.MyPlaces
import com.example.hambaapp.Remote.IGoogleAPIService

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.hambaapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    //----------------------------------------------------------------------------------
    //Reference: EDMT Dev
    //Url: https://www.youtube.com/watch?v=tI4dV9n6-yE
    //Reference: The Coders Integrity
    //Url: https://www.youtube.com/watch?v=SzIuH0HV8tg&t=9s
    //Use: Show user's current location and Nearby Places
    lateinit var mapView: MapView

    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    private val DEFAULT_ZOOM = 15f

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 1

    private lateinit var accommodation: ImageButton

    private lateinit var transport: ImageButton

    private lateinit var restaurant: ImageButton

    private lateinit var localbar: ImageButton

    private lateinit var atm: ImageButton

    lateinit var mService: IGoogleAPIService

    internal lateinit var currentPlace: MyPlaces

    var latitude = 0.0
    var longitude = 0.0
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val database = FirebaseDatabase.getInstance()
   // private val myReference3 = database.getReference("users").child(userID!!).child("Listing Data")
    private val myMutableMap: MutableMap<LatLng, String> = mutableMapOf()

    private val allListingDataReference = database.getReference("Businesses")
    private var  businessName:String = ""


    //--------------------------------------------------------------------------------------
    //Uses Google Services to search nearby places based on the string of the button clicked
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the nav bar function
        navigationBar()


        //Initialising map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //calling retrieve business data function
        allListingDataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    // If sighting locations are found
                    for (userSnapshot in snapshot.children)
                    {
                        // Using the correct type parameter for the getValue method
                        val businessLocat = userSnapshot.child("location").getValue(String::class.java).toString()
                        businessName = userSnapshot.child("title").getValue(String::class.java).toString()
                       // val selectedCategory = userSnapshot.child("selectedCategory").getValue(String::class.java).toString()

                        // Your existing code to process businessLocat and businessName...
                        if (businessLocat != null)
                        {
                            // Split the coordinate string into pairs and convert to LatLng
                            val coordinatePairs =
                                businessLocat.split("|") // Split by pipe character

                            for (coordinatePair in coordinatePairs) {
                                val parts = coordinatePair.split(",") // Split by comma
                                if (parts.size == 2) {
                                    val latitude = parts[0].toDoubleOrNull()
                                    val longitude = parts[1].toDoubleOrNull()
                                    if (latitude != null && longitude != null) {
                                        val theCoordinates = LatLng(latitude, longitude)
                                        //birdObservations.add(theCoordinates)
                                        myMutableMap.put(theCoordinates, businessName)
                                    }
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(this@MapsActivity, "Can't find the Location", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else
                {
                    // Handle case when no data is found
                   // Log.e("FirebaseData", "No data found in Listing Data")
                    Toast.makeText(this@MapsActivity, "No data found in Businesses", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Data retrieval failed: $error")
                // Handle error
                Toast.makeText(this@MapsActivity, "retrieval failed", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, show the device's current location on the map
            onMapReady(mMap)
        }
    }

    //----------------------------------------------------------------------------------------------
    //Executables once map is on screen
    override fun onMapReady(googleMap: GoogleMap)
    {

        mMap = googleMap


        //Zoom in Controls
        zoomFunction(googleMap)
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
            return
        }
        // Enable My Location button and show the user's location on the map
        mMap.isMyLocationEnabled = true
        // Get the device's current location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // Add a marker at the device's current location
                   // var userLocation = LatLng(it.latitude, it.longitude) //actual device location uncomment this
                    var userLocation = LatLng(-33.8970590380015, 18.48906600246067) //hard coded location to finish app from
                    val markerOptions = MarkerOptions()
                    markerOptions.position(userLocation)
                    markerOptions.title("Your Locationnn")
                    mMap.addMarker(markerOptions)

                    // How zoomed in the map will be.
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))


                    //retrive sightings to display
                    runOnUiThread{
                        for ((coordinates, businessName) in myMutableMap)
                        {
                            val latLng = LatLng(coordinates.latitude, coordinates.longitude)
                            val markerOption = MarkerOptions()
                                .position(latLng)
                                .title(businessName)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markersmall))
                            googleMap.addMarker(markerOption)
                            Toast.makeText(this, "we found $businessName", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }

    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    private fun zoomFunction(googleMap: GoogleMap)
    {
        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isRotateGesturesEnabled = true
            isScrollGesturesEnabled = true
            isTiltGesturesEnabled = true
            isZoomGesturesEnabled = true
        }
    }
    //----------------------------------------------------------------------------------------------



    private fun navigationBar() {
        //This will account for event clicking of the navigation bar (similar to if statement format)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.home -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }

                R.id.location -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                }

                R.id.favourites -> {
                    val intent = Intent(this, Favourites::class.java)
                    startActivity(intent)
                }
                R.id.profile -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
}
//------------------------------------------<<<<<<<<<<<-End Of File->>>>>>>>>>>-------------------------