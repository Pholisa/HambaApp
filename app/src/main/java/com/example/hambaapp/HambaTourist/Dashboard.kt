package com.example.hambaapp.HambaTourist

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.AccomodationPage
import com.example.hambaapp.Entertainment
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityDashboardBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var recyclerViewTourism: RecyclerView
    private lateinit var recyclerViewTourism2: RecyclerView
    val businessArrayList = mutableListOf<BusinessDetailPublic1>()
    val businessArrayList1 = mutableListOf<BusinessDetailPublic1>()
    //private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var databaseReference: DatabaseReference
    private val database = FirebaseDatabase.getInstance()
    private val businessReference = database.getReference("Business")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //setting the recycler viewer
        recyclerViewTourism = findViewById(R.id.rvBusinessDisplaying)
        recyclerViewTourism.layoutManager = LinearLayoutManager(this)

        //setting second recycler
        //recyclerViewTourism2 = findViewById(R.id.rvBusinessDisplaying2)
      //  recyclerViewTourism2.layoutManager = LinearLayoutManager(this)

        //----------------------------------------------------------------------------------------------
        //getting business data from database
        getBusinessData()


    binding.layBed.setOnClickListener {
        //businessArrayList.clear()
        travelCategory("Accommodation")
        }

        binding.layCar.setOnClickListener {
            travelCategory("Travel")
        }

        binding.layGear.setOnClickListener {
            travelCategory("Other")
        }

        binding.layEntertain.setOnClickListener {
            travelCategory("Food & Entertainment")
        }

        //calling navigation bar function
        navigationBar()
    }

    //Displaying data based on categories
    private fun travelCategory(travel:String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Businesses")
        val query: Query = databaseReference.orderByChild("category").equalTo(travel)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              //  val businessArrayList1 = mutableListOf<BusinessDetailPublic1>()

                if (dataSnapshot.exists()) {
                    for (businessSnapshot in dataSnapshot.children) {
                        val businessItem = businessSnapshot.getValue(BusinessDetailPublic1::class.java)
                        businessItem?.let {
                            businessArrayList1.add(it)
                        }
                    }

                    recyclerViewTourism.adapter = TourismAdapter(this@Dashboard, businessArrayList1) { userProfile ->
                        // Your item click listener logic
                        Toast.makeText(applicationContext, "click works", Toast.LENGTH_SHORT).show()

                        // Bottom sheet data
                        val sheet1 = findViewById<FrameLayout>(R.id.sheet1)
                        BottomSheetBehavior.from(sheet1).apply {
                            peekHeight = 0
                            state = BottomSheetBehavior.STATE_EXPANDED
                            // calling function that populates sheet with data from database
                            sheetPopulation(userProfile)
                        }
                    }
                }
                else{
                    Toast.makeText(applicationContext, "cant find data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }



    private fun getBusinessData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Businesses")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    for (businessSnapshot in snapshot.children)
                    {
                        val busines = businessSnapshot.getValue(BusinessDetailPublic1::class.java)
                        businessArrayList.add(busines!!)
                    }

                    val adapter = TourismAdapter(this@Dashboard, businessArrayList,
                        onItemClickListener = { userProfile  ->
                            Toast.makeText(applicationContext, "click works", Toast.LENGTH_SHORT).show()
                            // Bottom sheet data
                            val sheet1 = findViewById<FrameLayout>(R.id.sheet1)
                            BottomSheetBehavior.from(sheet1).apply {
                                peekHeight = 0
                                state = BottomSheetBehavior.STATE_EXPANDED
                                //calling function that populates sheet with data from database
                                sheetPopulation(userProfile )
                            }
                        }
                    )
                    recyclerViewTourism.adapter = adapter
                }
                else //no businesses in database
                {
                    // Toast.makeText(applicationContext, "You currently have no businesses saved", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(this@Dashboard, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }

    //function that will populate bottom sheet with data
    private fun sheetPopulation(userProfile: BusinessDetailPublic1)
    {
        // Set up your views in the bottom sheet
        val coverImage: ImageView = binding.openImageDBV
        val businessName: TextView = binding.busTitleDBV
        val businessLocation : TextView = binding.contLocationDBV
        val businessPrice: TextView = binding.tvPriceDBV
        val businessSummary: TextView = binding.tvBusDescptionDBV
        val number: TextView = binding.contPhoneDBV
        val email: TextView = binding.ContEmailDBV

        // Setting image
        val imageBytes = Base64.decode(userProfile.stringImage, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        coverImage.setImageBitmap(bitmap)

        // Set text values
        businessName.text = userProfile.title
        businessLocation.text = userProfile.location
        businessPrice.text = userProfile.price
        businessSummary.text = userProfile.businessSummary
        number.text = userProfile.telephoneNo
        email.text = userProfile.emailAd


    }

    //displaying navigation function
    private fun navigationBar()
    {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // You are already in the Dashboard, so no need to navigate again.
                }

                R.id.location -> {
                    //mapActivity()
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

    /*private fun fetchDataFromFirebase() {
        databaseReference.child("nearby_places").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nearbyPlacesList = mutableListOf<NearbyPlaceData>()
                for (dataSnapshot in snapshot.children) {
                    val nearbyPlaceData = dataSnapshot.getValue(NearbyPlaceData::class.java)
                    nearbyPlaceData?.let { nearbyPlacesList.add(it) }
                }
                // Set up the RecyclerView adapter
                //nearbyAdapter = NearbyPlaceAdapter(this@Dashboard, nearbyPlacesList)
                //nearbyPlaceRecycler.adapter = nearbyAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }*/
}
