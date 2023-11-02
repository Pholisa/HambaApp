package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.databinding.ActivityBusinessDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BusinessDashboard : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Call navigation bar
        navigationBar()

    }

    //navigation function
    private fun navigationBar() {
        // This will account for event clicking of the navigation bar (similar to if statement format)
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

                else -> {
                }
            }
            true
        }
    }
}
