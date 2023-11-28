package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hambaapp.HambaAdmin.AdminDashboard
import com.example.hambaapp.HambaAdmin.ActiveBusinesses
import com.example.hambaapp.databinding.ActivityAdminPortalBinding

class AdminPortal : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPortalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPortalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

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
}