package com.example.hambaapp.HambaAdmin

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.AdminPortal
import com.example.hambaapp.ApprovedRequestAdapter
import com.example.hambaapp.HambaBusiness.Information
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityActiveBusinessesBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActiveBusinesses : AppCompatActivity() {

    private lateinit var binding: ActivityActiveBusinessesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    val businessList = mutableListOf<Information>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveBusinessesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()

        //setting the recycler viewer
        recyclerView = findViewById(R.id.rv_displayApproved)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //calling function to retrieve data
        retrievBusinessDataFromFirebase()
    }
    //----------------------------------------------------------------------------------------------
    private fun retrievBusinessDataFromFirebase()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Approved Requests")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    for (businessSnapshot in snapshot.children)
                    {
                        val myBiz = businessSnapshot.getValue(Information::class.java)
                        businessList.add(myBiz!!)
                    }

                    adapterData()

                }
                else //no requests in database
                {
                    Toast.makeText(applicationContext, "No approved businesses", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(this@ActiveBusinesses, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    //----------------------------------------------------------------------------------------------
    //method to handle data click listeners
    private fun adapterData() {
        val adapter = ApprovedRequestAdapter(
            this@ActiveBusinesses, businessList,
            onDeleteClickListener = { position ->
                // calling delete business function
                deleteBusiness(position)
            },
            onItemClickListener = { userProfile ->
                // Bottom sheet data
                val sheet1 = findViewById<FrameLayout>(R.id.sheet4)
                BottomSheetBehavior.from(sheet1).apply {
                    peekHeight = 0
                    state = BottomSheetBehavior.STATE_EXPANDED

                    // sheet population function that will display business data on sheet
                    sheetPopulation(userProfile)
                }
            }
        )
        recyclerView.adapter = adapter
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //function that will populate bottom sheet with data
    private fun sheetPopulation(userProfile: Information) {

        // Set up your views in the bottom sheet
        val coverImage: ImageView = binding.ivCoverImage4
        var registerNumber= findViewById<TextView>(R.id.tv_RegNumber)
        var businessName = findViewById<TextView>(R.id.tv_BusNme)
        var address = findViewById<TextView>(R.id.tv_Bus_Addy)
        var phoneNumber = findViewById<TextView>(R.id.tv_Tel_Numb)
        var businessType = findViewById<TextView>(R.id.tv_Bus_Type)
        var category = findViewById<TextView>(R.id.tv_Bus_Category)
        var emailAddress = findViewById<TextView>(R.id.tv_email_Addy)


        // Setting image
        val imageBytes = Base64.decode(userProfile.stringImage, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        coverImage.setImageBitmap(bitmap)

        // Set text values
        businessName.text = userProfile.companyName
        registerNumber.text = userProfile.registerNumber
        address.text = userProfile.businessAddress
        phoneNumber.text = userProfile.telephoneNumber
        businessType.text = userProfile.businessType
        category.text = userProfile.businessCategory
        emailAddress.text = userProfile.emailAddress

    }
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //function to delete a business based on posiytion
    private fun deleteBusiness(position: Int)
    {
        // Handling item deletion here
        val deletedBusiness = businessList[position]
        // Removing the item from the list
        businessList.removeAt(position)
        // Notify the adapter about the removal
        recyclerView.adapter?.notifyItemRemoved(position)
        // Deleting the value from the database
        databaseReference.orderByChild("companyName").equalTo(deletedBusiness.companyName)
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
    //navigation bar function
    private fun navigationBar() {
        //This will account for event clicking of the navigation bar (similar to if statement format)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.home -> {
                    val intent = Intent(this, AdminDashboard::class.java)
                    startActivity(intent)
                }

                //we need a recyler viewer of all active businesses
                R.id.activeBusinesses -> {
                    val intent = Intent(this, ActiveBusinesses::class.java)
                    startActivity(intent)
                }

                R.id.profile -> { //Admin settings screen
                    val intent = Intent(this, AdminPortal::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
    //----------------------------------------------------------------------------------------------
}
//------------------------------------------ooo000EndOfFile000ooo-----------------------------------