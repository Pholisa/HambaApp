package com.example.hambaapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.hambaapp.databinding.ActivityBusinessInfoBinding
import com.example.hambaapp.databinding.ActivityBusinessPrevBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.DatabaseReference

class BusinessPrev : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessPrevBinding
    private lateinit var imageContainer: LinearLayout // A container for displaying images
    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var companyName: TextView
    private lateinit var registerNumber: TextView
    private lateinit var emailAddress: TextView
    private lateinit var telephoneNumber: TextView
    private lateinit var businessType: TextView
    private lateinit var businessAddress: TextView
    private lateinit var businessCategory: TextView
    private lateinit var title: TextView
    private lateinit var businessSummary: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessPrevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // If the user clicks "save"
        val save = findViewById<Button>(R.id.btnDone1)
        save.setOnClickListener {
            val intent = Intent(this, BusinessDashboard::class.java)
            startActivity(intent)
        }

        companyName = findViewById(R.id.tvCompanyNameShow)
        registerNumber = findViewById(R.id.tvRegisterNumberShow)
        emailAddress = findViewById(R.id.tvEmailAddressShow)
        telephoneNumber = findViewById(R.id.tvTelephoneNumberShow)
        businessType = findViewById(R.id.tvBusinessTypeShow)
        businessAddress = findViewById(R.id.tvBusinessAddressShow)
        businessCategory = findViewById(R.id.tvBusinessCategoryShow)
        title = findViewById(R.id.tvTitleShow)
        businessSummary = findViewById(R.id.tvBusinessSummaryShow)
        imageContainer = findViewById(R.id.imageContainer) // Initialize the image container

        database = Firebase.database.reference
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        database.child("Businesses").child(userID!!).get().addOnSuccessListener { snapshot ->
            val company = snapshot.child("companyName").value.toString()
            val regNumber = snapshot.child("registerNumber").value.toString()
            val emailAd = snapshot.child("emailAddress").value.toString()
            val telephoneNum = snapshot.child("telephoneNumber").value.toString()
            val type = snapshot.child("businessType").value.toString()
            val businessAd = snapshot.child("businessAddress").value.toString()
            val businessCat = snapshot.child("businessCategory").value.toString()
            val theTitle = snapshot.child("Listing Data").child("title").value.toString()
            val businessSum = snapshot.child("Listing Data").child("businessSummary").value.toString()

            companyName.text = company
            registerNumber.text = regNumber
            emailAddress.text = emailAd
            telephoneNumber.text = telephoneNum
            businessType.text = type
            businessAddress.text = businessAd
            businessCategory.text = businessCat
            title.text = theTitle
            businessSummary.text = businessSum

            val imageList = snapshot.child("Listing Data").child("stringImages").children.map { it.value.toString() }
            displayImages(imageList)
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        database.child("Listing Data").child(userID!!).get().addOnSuccessListener { snapshot ->
            val theTitle = snapshot.child("title").value.toString()
            val businessSum = snapshot.child("businessSummary").value.toString()
            val imageList = snapshot.child("stringImages").children.map { it.value.toString() }
            displayImages(imageList)
            title.text = theTitle
            businessSummary.text = businessSum
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }


        // Calling the navigation function
        navigationBar()
    }

    //function that will handle the displaying of images on the screen
    private fun displayImages(imageList: List<String>) {
        for ((index, base64Image) in imageList.withIndex()) {
            val imageView = ImageView(this)
            val bitmap = convertBase64ToBitmap(base64Image)
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
                imageView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                imageView.setOnClickListener { onImageClick(index) } // Set a click listener
                imageContainer.addView(imageView)
            }
        }
    }

//Event handler for when user clicks an image - If a user wants to delete an image or update any data
    private fun onImageClick(index: Int) {

        Toast.makeText(this, "Image $index selected", Toast.LENGTH_SHORT).show()
    }

    // Function to convert a Base64 encoded string to a Bitmap
    fun convertBase64ToBitmap(base64String: String): Bitmap? {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun navigationBar() {
        // This will account for event clicking of the navigation bar (similar to if statement format)
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
                    val intent = Intent(this, BusinessPortal::class.java)
                    startActivity(intent)
                }
                else -> {}
            }
            true
        }
    }
}


