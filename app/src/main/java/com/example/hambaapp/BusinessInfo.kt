package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hambaapp.databinding.ActivityBusinessInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BusinessInfo : AppCompatActivity() {

     private lateinit var binding: ActivityBusinessInfoBinding
    private lateinit var database: DatabaseReference

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("users").child(userID!!).child("Business Information")
    private lateinit var firebaseAuthentication: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationBar()

        val buttonNext = findViewById<Button>(R.id.btnBusNext)

        buttonNext.setOnClickListener {
            ValidateData()
            val intentNext = Intent(this, BusinessDescription::class.java)
            startActivity(intentNext)


        }
        navigationBar()

    }
         private fun ValidateData() {

             val companyName = binding.ETBusCompName.text.toString()
             val registerNumber =binding.ETBusCompRegNo.text.toString()
             val emailAddress = binding.ETBusEmailAd.text.toString()
             val telephoneNumber=binding.ETBusTelNo.text.toString()
             val businessType = binding.ETBusType.text.toString()
             val businessAddress =binding.ETBusAddress.text.toString()
             val businessCategory =binding.ETBusCat.text.toString()

        if (binding.ETBusCompName.text.toString().isEmpty() ||
            binding.ETBusCompRegNo.text.toString().isEmpty() ||
            binding.ETBusEmailAd.text.toString().isEmpty() ||
            binding.ETBusTelNo.text.toString().isEmpty() ||
            binding.ETBusType.text.toString().isEmpty() ||
            binding.ETBusAddress.text.toString().isEmpty() ||
            binding.ETBusCat.text.toString().isEmpty()
        ) {
            Toast.makeText(this,"Please enter all fields", Toast.LENGTH_SHORT).show()
        }
        else
        {
            /*val companyName = findViewById<EditText>(R.id.ETBusCompName)
            var name = companyName.text.toString().trim()

            val registerNumber = findViewById<EditText>(R.id.ETBusCompRegNo)
            var regNo = registerNumber.text.toString().trim()

            val emailAddress = findViewById<EditText>(R.id.ETBusEmailAd)
            var email = emailAddress.text.toString().trim()

            val telephoneNumber = findViewById<EditText>(R.id.ETBusTelNo)
            var telephone = telephoneNumber.text.toString().trim()

            val businessType = findViewById<EditText>(R.id.ETBusType)
            var type = businessType.text.toString().trim()

            val businessAddress = findViewById<EditText>(R.id.ETBusAddress)
            var address = businessAddress.text.toString().trim()

            val businessCategory = findViewById<EditText>(R.id.ETBusCat)
            var category = businessCategory.text.toString().trim()


            myReference.push().setValue(name)
            myReference.push().setValue(regNo)
            myReference.push().setValue(email)
            myReference.push().setValue(telephone)
            myReference.push().setValue(type)
            myReference.push().setValue(address)
            myReference.push().setValue(category)*/
            database = FirebaseDatabase.getInstance().getReference("Business Information")
            val Info = Information(companyName, registerNumber, emailAddress, telephoneNumber, businessType,businessAddress,businessCategory)
            database.child(userID!!).setValue(Info).addOnSuccessListener {
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()
            }
        }

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
