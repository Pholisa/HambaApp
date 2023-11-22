package com.example.hambaapp.HambaBusiness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.Dashboard
import com.example.hambaapp.Favourites
import com.example.hambaapp.MapsActivity
import com.example.hambaapp.R
import com.example.hambaapp.Welcome
import com.example.hambaapp.databinding.ActivityBusinessDashboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


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

    //----------------------------------------------------------------------------------------------
    private fun adapterData()
    {
        val adapter = MyBusinessAdapter(this@BusinessDashboard, businessArrayList,
            onDeleteClickListener = { position ->
                // calling delete business function
                deleteBusiness(position)
            },
            onItemClickListener = { position ->
                // Bottom sheet data
                val sheet1 = findViewById<FrameLayout>(R.id.sheet)
                BottomSheetBehavior.from(sheet1).apply {
                    peekHeight = 0
                    state = BottomSheetBehavior.STATE_EXPANDED
                    // calling function that populates sheet with data from the database
                   // Toast.makeText(applicationContext, "image found", Toast.LENGTH_SHORT).show()
                    // businessName
                    var businessName = findViewById<TextView>(R.id.tvBusNme)
                    var businessName1 = businessArrayList[position]
                    businessName.text = businessName1.title.toString()

                    // Business Address
                    var businessLocation = findViewById<TextView>(R.id.tv_Bus_Address)
                    var businessLocation1 = businessArrayList[position]
                    businessLocation.text = businessLocation1.location.toString()

                    // Business price
                    var businessPrice = findViewById<TextView>(R.id.tv_Bus_Price)
                    var businessPrice1 = businessArrayList[position]
                    businessPrice.text = "R" + businessPrice1.price.toString()

                    // Business Summary
                    var businessSummary = findViewById<TextView>(R.id.tv_Bus_Summary)
                    var businessSummary1 = businessArrayList[position]
                    businessSummary.text = businessSummary1.businessSummary.toString()

                    // Business Image
                  //  val businessImage = findViewById<ImageView>(R.id.ivCoverImage)
                    val businessImage1 = businessArrayList[position].stringImage
                  //  val businessImage2 = businessImage1.stringImage.toString()

// Load image using Picasso
                    if (!businessImage1.isNullOrBlank())
                    {
                        Picasso.get().load(businessImage1).into(binding.ivCoverImage)
                    } else {
                        Toast.makeText(applicationContext, "image not found", Toast.LENGTH_SHORT).show()
                    }

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
    private fun sheetPopulation(position: Int)
    {
       // Toast.makeText(applicationContext, "image found", Toast.LENGTH_SHORT).show()
        // businessName
        var businessName = findViewById<TextView>(R.id.tvBusNme)
        var businessName1 = businessArrayList[position]
        businessName.text = businessName1.title.toString()

        // Business Address
        var businessLocation = findViewById<TextView>(R.id.tv_Bus_Address)
        var businessLocation1 = businessArrayList[position]
        businessLocation.text = businessLocation1.location.toString()

        // Business price
        var businessPrice = findViewById<TextView>(R.id.tv_Bus_Price)
        var businessPrice1 = businessArrayList[position]
        businessPrice.text = "R" + businessPrice1.price.toString()

        // Business Summary
        var businessSummary = findViewById<TextView>(R.id.tv_Bus_Summary)
        var businessSummary1 = businessArrayList[position]
        businessSummary.text = businessSummary1.businessSummary.toString()

        // Business Image
        var businessImage = findViewById<ImageView>(R.id.ivCoverImage)
        var businessImage1 = businessArrayList[position]
        var businessImage2 = businessImage1.stringImage

    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //decoding image from string to image
    private fun decodeImageFromString(imageString: String?): Bitmap?
    {
        try {
            val decodedBytes: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            // Handle the decoding error (e.g., log the error)
            e.printStackTrace()
        }
        return null
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
                    val intent = Intent(this, Welcome::class.java)
                    startActivity(intent)
                }
                .show()
    }
    //----------------------------------------------------------------------------------------------
}
