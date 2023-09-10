package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hambaapp.databinding.BusinessEntryItemsBinding


class BusinessEntryItems : AppCompatActivity() {

    private lateinit var binding: BusinessEntryItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BusinessEntryItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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