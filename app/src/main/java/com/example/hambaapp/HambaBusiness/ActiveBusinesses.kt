package com.example.hambaapp.HambaBusiness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hambaapp.AdminDashboard
import com.example.hambaapp.AdminPortal
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityActiveBusinessesBinding

class ActiveBusinesses : AppCompatActivity() {

    private lateinit var binding: ActivityActiveBusinessesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveBusinessesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
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