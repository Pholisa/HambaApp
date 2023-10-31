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
import com.example.hambaapp.Model.MyPlaces
import com.example.hambaapp.Remote.IGoogleAPIService

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.hambaapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
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

    private  var fusedLocationClient: FusedLocationProviderClient? = null

    private lateinit var accommodation: ImageButton

    private lateinit var transport: ImageButton

    private lateinit var restaurant: ImageButton

    private lateinit var localbar: ImageButton

    private lateinit var atm: ImageButton

    lateinit var mService: IGoogleAPIService

    internal lateinit var currentPlace: MyPlaces

    var latitude = 0.0

    var longitude = 0.0


    //----------------------------------------------------------------------------------
    //Ask user for permission before showing map
    override fun onMapReady(googleMap: GoogleMap) {
        mapView.onResume()
        mMap = googleMap

        askPermissionLocation()

        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mMap!!.setMyLocationEnabled(true)

    }

    //--------------------------------------------------------------------------------------
    //Uses Google Services to search nearby places based on the string of the button clicked
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the nav bar function
        navigationBar()

        //Init Service
        mService = Common.googleApiService
        mapView = findViewById<MapView>(R.id.map)

        accommodation = findViewById(R.id.ib_accomodation)
        transport = findViewById(R.id.ib_transport)
        restaurant = findViewById(R.id.ib_restaurant)
        localbar = findViewById(R.id.ib_bar)
        atm = findViewById(R.id.ib_atm)

        askPermissionLocation()

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        //On click events to filter search of nearby places
        accommodation.setOnClickListener {
            nearByPlace("accommodation")

        }
        transport.setOnClickListener {
            nearByPlace("transport")
        }
        localbar.setOnClickListener {
            nearByPlace("local bar")
        }
        restaurant.setOnClickListener {
            nearByPlace("restaurant")
        }
        atm.setOnClickListener {
            nearByPlace("atm")
        }


    }
    //----------------------------------------------------------------------------------
    //Method for search for nearby places based on string for the type of placed being located
    private fun nearByPlace(typePlace: String) {
        //Clears all markers on the map
        mMap!!.clear()

        //build URL request based on lacation
        val url = getUrl(latitude,longitude, typePlace)

        mService.getNearbyPlaces(url).enqueue(object : Callback<MyPlaces> {
            override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                currentPlace = response.body()!!

                if(response.isSuccessful){

                    for (i in 0 until response.body()!!.results!!.size) {
                        val markerOptions=MarkerOptions()
                        val googlePlace = response.body()!!.results!![i]
                        val lat = googlePlace.geometry!!.location!!.lat
                        val lng = googlePlace.geometry!!.location!!.lng
                        val placeName = googlePlace.name
                        val description = googlePlace.rating.toString()
                        val latLng = LatLng(lat,lng)

                         //Customised markers based on string searched
                        if(typePlace=="accommodation") {
                            markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_VIOLET))

                        }else if(typePlace=="transport")
                            markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_AZURE))
                        else if(typePlace=="restaurant")
                            markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_RED))
                        else if(typePlace=="local bar")
                            markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_ORANGE))
                        else if(typePlace=="atm")
                            markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_GREEN))
                        else
                            markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_BLUE))


                        //Shows details of marker selected
                        val show = mMap!!.addMarker(
                            markerOptions
                                .position(latLng)
                                .title(placeName).snippet("Ratings: $description")
                        )
                        show?.showInfoWindow()

                        //Move camera
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                    }


                }
            }

            override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                Toast.makeText(baseContext, ""+t.message, Toast.LENGTH_SHORT).show()
            }

        })


    }

    //----------------------------------------------------------------------------------
    //The method gets the URL required for searching for nearby places using the Places API services
    //and the project's API key
    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=10000") //10 km
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyA5QUhTzcsbgSQCvNlARtgPKWDfp0og_ac")

        Log.d("URL_DEBUG",googlePlaceUrl.toString())
        return googlePlaceUrl.toString()

    }
    //----------------------------------------------------------------------------------
    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        askPermissionLocation()

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }

    //--------------------------------------------------------------------------------------------------------------------
    // Asks the user for permission to access their location
    private fun askPermissionLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

        getCurrentLocation()

    }

    //--------------------------------------------------------------------------------------------------------------------
    //Gets the user's current location and shows it on the map
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)

        try{
            val location =  fusedLocationClient!!.getLastLocation()

            location.addOnCompleteListener(object : OnCompleteListener<Location> {
                override fun onComplete(loc: Task<Location>) {
                    if (loc.isSuccessful) {

                        val currentLocation = loc.result as Location?
                        if (currentLocation != null) {
                            moveCamera(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                DEFAULT_ZOOM
                            )

                            latitude = currentLocation.latitude
                            longitude = currentLocation.longitude

                        }
                    } else {
                        askPermissionLocation()

                    }
                }
            })
        } catch (se: Exception) {
            Log.e("TAG", "Security Exception")
        }

    }

    //--------------------------------------------------------------------------------------------------------------------
    //Zoom/Moves the camera to the user's location
    private fun moveCamera(latLng: LatLng, zoom: Float) {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }


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
                    val intent = Intent(this, BusinessPortal::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
}
//------------------------------------------<<<<<<<<<<<-End Of File->>>>>>>>>>>-------------------------