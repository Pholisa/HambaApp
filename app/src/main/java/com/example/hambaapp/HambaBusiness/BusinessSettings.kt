package com.example.hambaapp.HambaBusiness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.hambaapp.CurrentListing
import com.example.hambaapp.DeleteListing
import com.example.hambaapp.R
import com.example.hambaapp.Welcome
import com.example.hambaapp.databinding.ActivityBusinessSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BusinessSettings : AppCompatActivity() {


    private lateinit var binding: ActivityBusinessSettingsBinding
    private lateinit var viewListImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layAddList.setOnClickListener {
            val signupIntent = Intent(this, BusinessListingData::class.java)
            startActivity(signupIntent)
        }

        binding.layEditList.setOnClickListener {
            val signupIntent = Intent(this, BusinessListingData::class.java)
            startActivity(signupIntent)
        }

        binding.layViewList.setOnClickListener {
            val signupIntent = Intent(this, CurrentListing::class.java)
            startActivity(signupIntent)
        }

        binding.layDeleteList.setOnClickListener {
            val signupIntent = Intent(this, DeleteListing::class.java)
            startActivity(signupIntent)
        }


        //calling the navigation bar
        navigationBar()

        viewListImageView = findViewById(R.id.iv_viewList)

        //Allows user to click image view
        //Will take user to the More Info screen after clicking
        viewListImageView.setOnClickListener {
            val intent = Intent(this, CurrentListing::class.java)
            startActivity(intent)
        }
    }

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