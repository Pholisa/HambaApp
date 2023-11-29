package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hambaapp.HambaTourist.Dashboard
import com.example.hambaapp.HambaTourist.Favourites
import com.example.hambaapp.HambaTourist.MapsActivity
import com.example.hambaapp.HambaTourist.Settings
import com.example.hambaapp.databinding.ActivitySettingsBinding
import com.example.hambaapp.databinding.ActivityTermsAndContiditionsBinding

class TermsAndContiditions : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndContiditionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndContiditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()
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
    }
    //----------------------------------------------------------------------------------------------
}
//------------------------------------------ooo000EndOfFile000ooo-----------------------------------