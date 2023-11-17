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

                    val adapter = MyBusinessAdapter(this@BusinessDashboard,businessArrayList) { position ->
                        // Handling item deletion here
                        val deletedBird = businessArrayList[position]

                        // Removing the item from the list
                        businessArrayList.removeAt(position)

                        // Notify the adapter about the removal
                        recyclerView.adapter?.notifyItemRemoved(position)

                        // Deleting the value from the database
                        databaseReference.orderByChild("title").equalTo(deletedBird.title)
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

                    recyclerView.adapter = adapter

                }
                else
                {
                    Toast.makeText(applicationContext, "You currently have no birds saved", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(this@BusinessDashboard, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun deleteItem()
    {

    }

    //navigation function
    private fun navigationBar() {
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
}
