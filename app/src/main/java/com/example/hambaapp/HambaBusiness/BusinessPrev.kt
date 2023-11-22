package com.example.hambaapp.HambaBusiness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hambaapp.Dashboard
import com.example.hambaapp.Favourites
import com.example.hambaapp.MapsActivity
import com.example.hambaapp.R
import com.example.hambaapp.Welcome
import com.example.hambaapp.databinding.ActivityBusinessPrevBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class BusinessPrev : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessPrevBinding

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("users").child(userID!!).child("Business Information")
    private lateinit var firebaseAuthentication: FirebaseAuth

    private lateinit var database: DatabaseReference
    private lateinit var companyName : TextView
    private lateinit var registerNumber : TextView
    private lateinit var emailAddress : TextView
    private lateinit var telephoneNumber : TextView
    private lateinit var businessType : TextView
    private lateinit var businessAddress : TextView
    private lateinit var businessCategory : TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessPrevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //is user clicks save
        var save = findViewById<Button>(R.id.btnDone1)

        save.setOnClickListener {
            val intent = Intent(this, Welcome::class.java)
            startActivity(intent)
        }

        companyName = findViewById(R.id.tvCompanyNameShow)
        registerNumber = findViewById(R.id.tvRegisterNumberShow)
        emailAddress = findViewById(R.id.tvEmailAddressShow)
        telephoneNumber = findViewById(R.id.tvTelephoneNumberShow)
        businessType = findViewById(R.id.tvBusinessTypeShow)
        businessAddress = findViewById(R.id.tvBusinessAddressShow)
        businessCategory = findViewById(R.id.tvBusinessCategoryShow)


        database = Firebase.database.reference
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        database.child("users").child(userID!!).get().addOnSuccessListener {
            val company = it.child("Business Information").child("companyName").value.toString()
            val regNumber =
                it.child("Business Information").child("registerNumber").child("").value.toString()
            val emailAd =
                it.child("Business Information").child("emailAddress").child("").value.toString()
            val telephoneNum =
                it.child("Business Information").child("telephoneNumber").child("").value.toString()
            val type =
                it.child("Business Information").child("businessType").child("").value.toString()
            val businessAd =
                it.child("Business Information").child("businessAddress").child("").value.toString()
            val businessCat =
                it.child("Business Information").child("businessCategory").child("").value.toString()
            val theTitle =
                it.child("Business Description").child("title").child("").value.toString()
            val businessSum =
                it.child("Business Description").child("businessSummary").child("").value.toString()

            companyName.text = company
            registerNumber.text = regNumber
            emailAddress.text = emailAd
            telephoneNumber.text = telephoneNum
            businessType.text = type
            businessAddress.text = businessAd
            businessCategory.text = businessCat


        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

        }


    }


    private fun navigationBar() {
        //This will account for event clicking of the navigation bar (similar to if statement format)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.home -> {
                    val intent = Intent(this, BusinessDashboard::class.java)
                    startActivity(intent)
                }
                //we need a recyler viewer of all active businesses
                R.id.activeBusinesses -> {
                    logoutUI()
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
    //logout function
    private fun logoutUI()
    {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log-out?")
            .setNeutralButton("Dismiss") { dialog, which ->
                dialog.dismiss()
            }

            .setPositiveButton("Sign out") { dialog, which ->
                val intent = Intent(this, Dashboard::class.java)
                startActivity(intent)
            }
            .show()
    }
    //----------------------------------------------------------------------------------------------
}