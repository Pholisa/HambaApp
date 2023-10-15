package com.example.hambaapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.hambaapp.databinding.ActivityBusinessPrevBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class BusinessPrev : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessPrevBinding

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("users").child(userID!!).child("Business Preview")
    private lateinit var firebaseAuthentication: FirebaseAuth

    private lateinit var database: DatabaseReference
    private lateinit var companyName : TextView
    private lateinit var registerNumber : TextView
    private lateinit var emailAddress : TextView
    private lateinit var telephoneNumber : TextView
    private lateinit var businessType : TextView
    private lateinit var businessAddress : TextView
    private lateinit var businessCategory : TextView
    private lateinit var title : TextView
    private lateinit var businessSummary : TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessPrevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        companyName = findViewById(R.id.tvCompanyNameShow)
        registerNumber = findViewById(R.id.tvRegisterNumberShow)
        emailAddress = findViewById(R.id.tvEmailAddressShow)
        telephoneNumber = findViewById(R.id.tvTelephoneNumberShow)
        businessType = findViewById(R.id.tvBusinessTypeShow)
        businessAddress = findViewById(R.id.tvBusinessAddressShow)
        businessCategory = findViewById(R.id.tvBusinessCategoryShow)
        title = findViewById(R.id.tvTitleShow)
        businessSummary = findViewById(R.id.tvBusinessSummaryShow)

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
            title.text = theTitle
            businessSummary.text = businessSum

        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

        }
    }


        /*// Read from the database
        myReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<String>()
                Log.d(TAG, "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })


        myReference.child("users").child(userID!!).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        navigationBar()

    }*/



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