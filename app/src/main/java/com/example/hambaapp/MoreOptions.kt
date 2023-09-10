package com.example.hambaapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityMoreOptionsBinding


import android.widget.ImageView

class MoreOptions : AppCompatActivity() {

    private lateinit var binding: ActivityMoreOptionsBinding

    lateinit var infoImageView: ImageView
    lateinit var listImageView: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationBar()
        infoImageView = findViewById(R.id.iv_info)
        listImageView = findViewById(R.id.iv_listBusiness)



        //Allows user to click image view
        //Will take user to the More Info screen after clicking
        infoImageView.setOnClickListener {
            val intent = Intent(this, MoreInfo::class.java)
            startActivity(intent)

        }

        //Allows user to click image view
        //Will take user to the Business Description screen after clicking
        listImageView.setOnClickListener {
            val intent = Intent(this, BusinessList::class.java)
            startActivity(intent)

        }

        navigationBar()

    }
    private fun navigationBar() {
        //This will account for event clicking of the navigation bar (similar to if statement format)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.home -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }

                R.id.location -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }

                R.id.favourites -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }
                R.id.profile -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
}