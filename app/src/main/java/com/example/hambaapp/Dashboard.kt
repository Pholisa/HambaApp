package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.Adapter.NearbyPlaceAdaptar
import com.example.hambaapp.HambaBusiness.BusinessDetail
import com.example.hambaapp.HambaBusiness.BusinessDetailPublic
import com.example.hambaapp.HambaBusiness.MyBusinessAdapter
import com.example.hambaapp.Model.NearbyPlaceData
import com.example.hambaapp.databinding.ActivityDashboardBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var recyclerViewTourism: RecyclerView
    private lateinit var adapter: FavouriteBusinessAdapter
    private lateinit var like: ImageView

   // private lateinit var businessArrayList: ArrayList<BusinessDetail>
    private lateinit var databaseReference: DatabaseReference
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    val businessArrayList = mutableListOf<BusinessDetailPublic>()


    //private lateinit var nearbyPlaceRecycler: RecyclerView
    //private lateinit var nearbyAdapter: NearbyPlaceAdapter
    //private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationBar()
        //databaseReference = FirebaseDatabase.getInstance().reference

        // Initialize RecyclerView
        //nearbyPlaceRecycler = findViewById(R.id.nearby_place_recycler)
        //nearbyPlaceRecycler.layoutManager = LinearLayoutManager(this)

        // Fetch data from Firebase
        //fetchDataFromFirebase()

        //business list
        //businessArrayList = arrayListOf()

       // databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID!!).child()

        //-----------------------------------New Changes ---------------------------------------------------
        //recycler viewer setting
         recyclerViewTourism = findViewById(R.id.tvBusinessDisplay)
         recyclerViewTourism.layoutManager = LinearLayoutManager(this)

        like = findViewById(R.id.ivFavRV)

        //----------------------------------------------------------------------------------------------
        //getting business data from database
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID!!).child("Listing Data")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    for (businessSnapshot in snapshot.children)
                    {
                        val business = businessSnapshot.getValue(BusinessDetailPublic::class.java)
                        businessArrayList.add(business!!)
                    }


                    val favAdapter = FavouriteBusinessAdapter(this@Dashboard, businessArrayList,
                        onItemClickListener = { position ->
                            like.setImageResource(R.drawable.ic_heart)

                            Toast.makeText(applicationContext, "click works", Toast.LENGTH_SHORT).show()
                            // Bottom sheet data
                            val sheet1 = findViewById<FrameLayout>(R.id.sheet)
                            BottomSheetBehavior.from(sheet1).apply {
                                peekHeight = 0
                                state = BottomSheetBehavior.STATE_EXPANDED
                                //calling function that populates sheet with data from database
                                sheetPopulation(position)

                                //edit button
                                var editBusines = findViewById<TextView>(R.id.tv_Edit_Business)
                                editBusines.setOnClickListener {
                                    Toast.makeText(applicationContext, "currently disabled", Toast.LENGTH_SHORT).show()
                                }


                            }
                        },
                    )
                    recyclerViewTourism.adapter = favAdapter
                }
                else //No businesses found in database
                {
                    Toast.makeText(applicationContext, "You currently have no businesses saved", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(this@Dashboard, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })


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


    }

    //function that will populate bottom sheet with data
    private fun sheetPopulation(position: Int)
    {
        // businessName
        var businessName = findViewById<TextView>(R.id.tvBusNme)
        var businessName1 = businessArrayList[position]
        businessName.text = businessName1.title.toString()

        //Business Address
        var businessType = findViewById<TextView>(R.id.tvBusinessType)
        var businessTypes = businessArrayList[position]
        businessType.text = businessTypes.location.toString()

        //Business Summary
        var businessSummary = findViewById<TextView>(R.id.tv_Bus_Summary)
        var businessSummary1 = businessArrayList[position]
        businessSummary.text = businessSummary1.businessSummary.toString()
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
