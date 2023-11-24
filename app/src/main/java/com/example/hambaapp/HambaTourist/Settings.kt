package com.example.hambaapp.HambaTourist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hambaapp.HambaBusiness.BusinessOption
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivitySettingsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()
        var registerAsOwner = findViewById<ImageView>(R.id.iv_businessEdit)

        registerAsOwner.setOnClickListener {
            val intent = Intent(this, BusinessOption::class.java)
            startActivity(intent)
        }

        //about application
        var about = findViewById<ImageView>(R.id.iv_selectAbout)
        about.setOnClickListener {

        }

        val sheet1 = findViewById<FrameLayout>(R.id.sheet1)
        var constraintAbout = findViewById<ConstraintLayout>(R.id.constraint_about)
        var textValue = findViewById<TextView>(R.id.tv_aboutText)
        //about
        constraintAbout.setOnClickListener {
            Toast.makeText(this, "click works", Toast.LENGTH_SHORT).show()
            BottomSheetBehavior.from(sheet1).apply {
                peekHeight = 0
                state = BottomSheetBehavior.STATE_EXPANDED
                textValue.text = "Hamba by SALTHA is an authentic South African tourism and leisure application designed to provide the best experience of South Africa.Join us as either a small South African business or just looking for a vibe!"
            }
        }

        //terms and conditions
        var constraintConditions = findViewById<ConstraintLayout>(R.id.constraint_conditions)
        constraintConditions.setOnClickListener {
            BottomSheetBehavior.from(sheet1).apply {
                peekHeight = 0
                state = BottomSheetBehavior.STATE_EXPANDED
                textValue.text = "Terms and conditions go here"
            }
        }

        //privacy
        var constraintPrivacy = findViewById<ConstraintLayout>(R.id.constraint_privacy)
        constraintPrivacy.setOnClickListener {
            BottomSheetBehavior.from(sheet1).apply {
                peekHeight = 0
                state = BottomSheetBehavior.STATE_EXPANDED
                textValue.text = "privacy policy goes here"
            }
        }

        //rate
        var constraintRate = findViewById<ConstraintLayout>(R.id.constraint_rate)
        constraintRate.setOnClickListener {
            Toast.makeText(this, "Enabled at release", Toast.LENGTH_SHORT).show()
        }

        //share
        var constraintShare = findViewById<ConstraintLayout>(R.id.constraint_share)
        constraintShare.setOnClickListener {
            Toast.makeText(this, "To do ", Toast.LENGTH_SHORT).show()
        }
    }

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
    }
}