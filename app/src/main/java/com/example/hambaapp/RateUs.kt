package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hambaapp.HambaBusiness.BusinessDashboard
import com.example.hambaapp.HambaBusiness.BusinessDetail
import com.example.hambaapp.HambaBusiness.BusinessSettings
import com.example.hambaapp.HambaTourist.Dashboard
import com.example.hambaapp.HambaTourist.Favourites
import com.example.hambaapp.HambaTourist.MapsActivity
import com.example.hambaapp.HambaTourist.Settings
import com.example.hambaapp.databinding.ActivityRateUsBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RateUs : AppCompatActivity() {

    private lateinit var binding: ActivityRateUsBinding
    private val theDatabase = Firebase.database
    private val myReference = theDatabase.getReference("Comments")

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityRateUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling navigation bar
        navigationBar()

        var submit = findViewById<Button>(R.id.btn_submit_comment)
        submit.setOnClickListener {
            sendRatings()
        }
    }

    private fun sendRatings()
    {
        var userName = findViewById<EditText>(R.id.commenter_name)
        var comments = findViewById<EditText>(R.id.commentEditText)

        var name = userName.text.toString()
        var comment = comments.text.toString()


        if(name!= null && comment !=null)
        {
            val comment = User3(name,comment)
            myReference.push().setValue(comment).addOnSuccessListener {
                // Toast.makeText(this, "selected category is $selectedCategory", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Comment successfully saved! We appreciate the feedback!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Favourites::class.java)
                startActivity(intent)
            }
        }
        else
        {
            Toast.makeText(this@RateUs, "Please your enter name AND comment", Toast.LENGTH_SHORT).show()
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