package com.example.hambaapp.HambaBusiness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hambaapp.HambaTourist.Dashboard
import com.example.hambaapp.Favourites
import com.example.hambaapp.MapsActivity
import com.example.hambaapp.R
import com.example.hambaapp.databinding.BusinessEntryItemsBinding


class BusinessEntryItems : AppCompatActivity() {

    private lateinit var binding: BusinessEntryItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BusinessEntryItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationBar()

    }

    //----------------------------------------------------------------------------------------------
    //navigation bar
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
                    val intent = Intent(this, BusinessSettings::class.java)
                    startActivity(intent)
                }


                else -> {}
            }
            true
        }
    }
    //----------------------------------------------------------------------------------------------
}