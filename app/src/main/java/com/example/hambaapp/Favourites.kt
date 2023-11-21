package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.HambaBusiness.BusinessDetailPublic
import com.example.hambaapp.databinding.ActivityFavouritesBinding

class Favourites : AppCompatActivity() {

    private lateinit var binding: ActivityFavouritesBinding
    private lateinit var recyclerViewTourism: FavouriteBusinessAdapter
    private lateinit var recyclerViewFavourites: RecyclerView
    private lateinit var favoriteAdapter: ItemAdapter
    private lateinit var favorites: MutableList<BusinessDetailPublic>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()

        //recycler viewer setting
        recyclerViewFavourites = findViewById(R.id.rv_favourites)
        recyclerViewFavourites.layoutManager = LinearLayoutManager(this)


      /*  // Load the favorite items from a persistent storage if needed
        recyclerViewTourism = FavouriteBusinessAdapter(favorites)

        favoriteAdapter = ItemAdapter(favorites)

        recyclerViewFavourites.apply {
            layoutManager = LinearLayoutManager(this@Favourites)
            adapter = favoriteAdapter
        }*/

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
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
}