package com.example.hambaapp.HambaTourist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.CommentsAdapter
import com.example.hambaapp.R
import com.example.hambaapp.User3
import com.example.hambaapp.databinding.ActivityFavouritesBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Favourites : AppCompatActivity() {

    private lateinit var binding: ActivityFavouritesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    val businessArrayList = mutableListOf<User3>() //dictionary to store business data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()

        //setting the recycler viewer
        recyclerView = findViewById(R.id.rvBusinessDisplayingComments)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //getting business data from database
        retrieveBusinessDataFromFirebase()
    }

    private fun retrieveBusinessDataFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Comments")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    businessArrayList.clear() // Clear the list before adding new data

                    for (businessSnapshot in snapshot.children) {
                        val business = businessSnapshot.getValue(User3::class.java)
                        business?.let {
                            businessArrayList.add(it)
                        }
                    }

                    // Setting up the RecyclerView adapter after getting data
                    val adapter = CommentsAdapter(this@Favourites, businessArrayList)
                    recyclerView.adapter = adapter
                } else {
                    // Handle case where no businesses are in the database
                    // For example, show a message to the user
                    Toast.makeText(this@Favourites, "No businesses found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Favourites, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
    //----------------------------------------------------------------------------------------------
    private fun navigationBar()
    {
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
        //----------------------------------------------------------------------------------------------
    }
}
//------------------------------------------ooo000EndOfFile000ooo-----------------------------------