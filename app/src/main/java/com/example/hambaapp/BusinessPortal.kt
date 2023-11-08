package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.hambaapp.databinding.ActivityBusinessInfoBinding
import com.example.hambaapp.databinding.ActivityBusinessPortalBinding
import com.example.hambaapp.databinding.ActivityMoreOptionsBinding

class BusinessPortal : AppCompatActivity() {


    private lateinit var binding: ActivityBusinessPortalBinding
    private lateinit var viewListImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessPortalBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.layAddList.setOnClickListener {
            val signupIntent = Intent(this, BusinessDescription::class.java)
            startActivity(signupIntent)
        }

        binding.layEditList.setOnClickListener {
            val signupIntent = Intent(this, BusinessDescription::class.java)
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
                    val intent = Intent(this, BusinessInfo::class.java)
                    startActivity(intent)
                }

                //we need a recyler viewer of all active businesses
                R.id.activeBusinesses -> {
                    val intent = Intent(this, ActiveBusinesses::class.java)
                    startActivity(intent)
                }

                R.id.profile -> {
                    val intent = Intent(this, BusinessPortal::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
}