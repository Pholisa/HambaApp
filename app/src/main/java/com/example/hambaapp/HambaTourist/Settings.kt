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
import com.example.hambaapp.MoreInfo
import com.example.hambaapp.R
import com.example.hambaapp.RateUs
import com.example.hambaapp.TermsAndContiditions
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


        val sheet1 = findViewById<FrameLayout>(R.id.sheet1)
        var constraintAbout = findViewById<ConstraintLayout>(R.id.constraint_about)
        var textValue = findViewById<TextView>(R.id.tv_aboutText)
        var constraintList= findViewById<ConstraintLayout>(R.id.list_businessconstraint)

        //List business on click listener/register as business owner
        constraintList.setOnClickListener{
            val intent = Intent(this, BusinessOption::class.java)
            startActivity(intent)
        }
        //about
        constraintAbout.setOnClickListener {
            val intent = Intent(this, MoreInfo::class.java)
            startActivity(intent)
        }

        //terms and conditions
        var constraintConditions = findViewById<ConstraintLayout>(R.id.constraint_conditions)
        constraintConditions.setOnClickListener {
            val intent = Intent(this, TermsAndContiditions::class.java)
            startActivity(intent)
        }

        //privacy
        var constraintPrivacy = findViewById<ConstraintLayout>(R.id.constraint_privacy)
        constraintPrivacy.setOnClickListener {
            BottomSheetBehavior.from(sheet1).apply {
                peekHeight = 0
                state = BottomSheetBehavior.STATE_EXPANDED
                textValue.text = "To be released"
            }
        }

        //rate
        var constraintRate = findViewById<ConstraintLayout>(R.id.constraint_rate)
        constraintRate.setOnClickListener {
            val intent = Intent(this, RateUs::class.java)
            startActivity(intent)
        }

        //share
        var constraintShare = findViewById<ConstraintLayout>(R.id.constraint_share)
        constraintShare.setOnClickListener {
            Toast.makeText(this, "Enabled at release", Toast.LENGTH_SHORT).show()
        }
    }
    //----------------------------------------------------------------------------------------------

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
    }
    //----------------------------------------------------------------------------------------------
}
//------------------------------------------ooo000EndOfFile000ooo-----------------------------------