package com.example.hambaapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hambaapp.HambaBusiness.BusinessRegForm
import com.example.hambaapp.HambaBusiness.BusinessSettings
import com.example.hambaapp.HambaTourist.Dashboard
import com.example.hambaapp.HambaTourist.Favourites
import com.example.hambaapp.HambaTourist.MapsActivity
import com.example.hambaapp.databinding.ActivityMoreInfoBinding

class MoreInfo : AppCompatActivity() {

    private lateinit var binding: ActivityMoreInfoBinding

    lateinit var btnNextOpt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnNextOpt = findViewById(R.id.btnBusNext)

        //Allows user to click image view
        //Will take user to the next screen after clicking
        btnNextOpt.setOnClickListener {
            val intent = Intent(this, BusinessRegForm::class.java)
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