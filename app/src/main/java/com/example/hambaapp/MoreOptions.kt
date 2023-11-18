package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hambaapp.databinding.ActivityMoreOptionsBinding


import android.widget.ImageView
import com.example.hambaapp.HambaBusiness.BusinessListIntro
import com.example.hambaapp.HambaBusiness.BusinessSettings

class MoreOptions : AppCompatActivity() {

    private lateinit var binding: ActivityMoreOptionsBinding

    lateinit var infoImageView: ImageView
    lateinit var listImageView: ImageView
    lateinit var portalImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationBar()
        infoImageView = findViewById(R.id.iv_info)
        listImageView = findViewById(R.id.iv_listBusiness)
        portalImageView = findViewById(R.id.iv_businessPortal)



        //Allows user to click image view
        //Will take user to the More Info screen after clicking
        infoImageView.setOnClickListener {
            val intent = Intent(this, MoreInfo::class.java)
            startActivity(intent)

        }

        //Allows user to click image view
        //Will take user to the Business Description screen after clicking
        listImageView.setOnClickListener {
            val intent = Intent(this, BusinessListIntro::class.java)
            startActivity(intent)

        }

        //Allows user to click image view
        //Will take user to the Business Portal screen after clicking
        portalImageView.setOnClickListener {
            val intent = Intent(this, BusinessListIntro::class.java)
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
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                }

                R.id.favourites -> {
                    val intent = Intent(this, Favourites::class.java)
                    startActivity(intent)
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
}