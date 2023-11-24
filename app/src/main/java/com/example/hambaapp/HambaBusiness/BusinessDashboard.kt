package com.example.hambaapp.HambaBusiness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.HambaTourist.Dashboard
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityBusinessDashboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior


class BusinessDashboard : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessDashboardBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    val businessArrayList = mutableListOf<BusinessDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Call navigation bar
        navigationBar()
        //button to add a new business
        binding.btnAddBusiness.setOnClickListener {
            val addBusiness = Intent(this, BusinessListingData::class.java)
            startActivity(addBusiness)
        }

        //setting the recycler viewer
        recyclerView = findViewById(R.id.recycler1)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //----------------------------------------------------------------------------------------------
        //getting business data from database
        retrievBusinessDataFromFirebase()

    }

    //----------------------------------------------------------------------------------------------
    private fun retrievBusinessDataFromFirebase()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID!!).child("Listing Data")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    for (businessSnapshot in snapshot.children)
                    {
                        val birdie = businessSnapshot.getValue(BusinessDetail::class.java)
                        businessArrayList.add(birdie!!)
                    }

                    adapterData()

                }
                else //no birds in database
                {
                    Toast.makeText(applicationContext, "You currently have no businesses saved", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(this@BusinessDashboard, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
    //----------------------------------------------------------------------------------------------

    private fun adapterData()
    {
        val adapter = MyBusinessAdapter(this@BusinessDashboard, businessArrayList,
            onDeleteClickListener = { position ->
                // calling delete business function
                deleteBusiness(position)
            },
            onItemClickListener = { userProfile ->
                // Bottom sheet data
                val sheet1 = findViewById<FrameLayout>(R.id.sheet)
                BottomSheetBehavior.from(sheet1).apply {
                    peekHeight = 0
                    state = BottomSheetBehavior.STATE_EXPANDED

                    //sheet population fucntion that will display business data on sheet
                     sheetPopulation(userProfile)

                    // edit button
                    var editBusines = findViewById<TextView>(R.id.tv_Edit_Business)
                    editBusines.setOnClickListener {
                        Toast.makeText(applicationContext, "currently disabled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
        recyclerView.adapter = adapter
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //function to delete a business
    private fun deleteBusiness(position: Int)
    {
        // Handling item deletion here
        val deletedBusiness = businessArrayList[position]
        // Removing the item from the list
        businessArrayList.removeAt(position)
        // Notify the adapter about the removal
        recyclerView.adapter?.notifyItemRemoved(position)
        // Deleting the value from the database
        databaseReference.orderByChild("title").equalTo(deletedBusiness.title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    for (birdSnapshot in snapshot.children)
                    {
                        birdSnapshot.ref.removeValue()
                    }
                }
                override fun onCancelled(error: DatabaseError)
                {
                    // Nothing to handle
                }
            })
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //function that will populate bottom sheet with data
    private fun sheetPopulation(userProfile: BusinessDetail) {

        // Set up your views in the bottom sheet
        val coverImage: ImageView = binding.ivCoverImage
        val title: TextView = binding.tvBusNme
        val address: TextView = binding.tvBusAddress
        val price: TextView = binding.tvBusPrice
        val summary: TextView = binding.tvBusSummary

        // Setting image
        val imageBytes = Base64.decode(userProfile.stringImage, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        coverImage.setImageBitmap(bitmap)
        // Set text values
        title.text = userProfile.title
        summary.text = userProfile.businessSummary
        address.text = userProfile.location
        price.text = userProfile.price
    }

    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //navigation function
    private fun navigationBar()
    {
        //This will account for event clicking of the navigation bar (similar to if statement format)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.home -> {
                    val intent = Intent(this, BusinessDashboard::class.java)
                    startActivity(intent)
                }

                //we need a recyler viewer of all active businesses
                R.id.activeBusinesses -> {
                    logoutUI()
                }

                R.id.profile -> {
                    val intent = Intent(this, BusinessSettings::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //logout function
    private fun logoutUI()
    {
            MaterialAlertDialogBuilder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log-out?")
                .setNeutralButton("Dismiss") { dialog, which ->
                    dialog.dismiss()
                }

                .setPositiveButton("Sign out") { dialog, which ->
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }
                .show()
    }
    //----------------------------------------------------------------------------------------------
}

