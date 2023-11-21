package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.Adapter.NearbyPlaceAdaptar
import com.example.hambaapp.HambaBusiness.BusinessDetail
import com.example.hambaapp.Model.NearbyPlaceData
import com.example.hambaapp.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var recyclerViewTourism: RecyclerView
    private lateinit var businessArrayList: ArrayList<BusinessDetail>
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var databaseReference: DatabaseReference


    //private lateinit var nearbyPlaceRecycler: RecyclerView
    //private lateinit var nearbyAdapter: NearbyPlaceAdapter
    //private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //databaseReference = FirebaseDatabase.getInstance().reference

        // Initialize RecyclerView
        //nearbyPlaceRecycler = findViewById(R.id.nearby_place_recycler)
        //nearbyPlaceRecycler.layoutManager = LinearLayoutManager(this)

        // Fetch data from Firebase
        //fetchDataFromFirebase()

        //recycler viewer setting
        recyclerViewTourism = findViewById(R.id.tvBusinessDisplay)
        recyclerViewTourism.layoutManager = LinearLayoutManager(this)

        //business list
        businessArrayList = arrayListOf()

       // databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID!!).child()

        binding.layBed.setOnClickListener {
            val signupIntent = Intent(this, AccomodationPage::class.java)
            startActivity(signupIntent)
        }

        binding.layCar.setOnClickListener {
            val signupIntent = Intent(this, Register::class.java)
            startActivity(signupIntent)
        }

        binding.layGear.setOnClickListener {
            val signupIntent = Intent(this, MoreOptions::class.java)
            startActivity(signupIntent)
        }

        binding.layEntertain.setOnClickListener {
            val signupIntent = Intent(this, Entertainment::class.java)
            startActivity(signupIntent)
        }

        navigationBar()
    }

    private fun navigationBar() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // You are already in the Dashboard, so no need to navigate again.
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
