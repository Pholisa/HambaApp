package com.example.hambaapp.HambaBusiness

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.EditText
import com.example.hambaapp.CurrentListing
import com.example.hambaapp.DeleteListing
import com.example.hambaapp.R
import com.example.hambaapp.Welcome
import com.example.hambaapp.databinding.ActivityBusinessSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class BusinessSettings : AppCompatActivity() {


    private lateinit var binding: ActivityBusinessSettingsBinding
    private lateinit var viewListImageView: ImageView
    private val galleryRequestCode = 2
    private var rationaleDialogShown = false
    private var stringImage: String = ""
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var myReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()
        var profilePic = findViewById<ImageView>(R.id.iv_profile)
        profilePic.setOnClickListener{
            galleryCheckPermission()
        }

        //retrieve username and email from database
        retrieveUserEmail()
        retrieveName()

        //Retrieve busisiness//company data
        retrieveBusinessData()
        // Set EditText fields as non-editable initially
       // setEditTextsEditable(false)

    }

    private val ActivityResultLauncher =
        registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                try {
                    val inputStream = uri?.let { contentResolver.openInputStream(it) }
                    val myBitmap = BitmapFactory.decodeStream(inputStream)
                    val stream = ByteArrayOutputStream()
                    myBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val bytes = stream.toByteArray()
                    stringImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                    inputStream?.close()
                } catch (ex: Exception) {
                    Toast.makeText(this, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun retrieveBusinessData()
    {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID!!).child("Business Information")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot contains the data at the "Personal Details" node

                    // Assuming "name" is a field under "Personal Details"
                    val companyName = dataSnapshot.child("companyName").getValue(String::class.java)
                    val emailAddress = dataSnapshot.child("emailAddress").getValue(String::class.java)
                    val registerNumber = dataSnapshot.child("registerNumber").getValue(String::class.java)
                    val telephoneNumber = dataSnapshot.child("telephoneNumber").getValue(String::class.java)
                    val businessType = dataSnapshot.child("businessType").getValue(String::class.java)

                    //Initilise business edittextfield
                    //initialiseBusinessTextFields()
                    val name = binding.ETBusCompName.editText?.text.toString()
                    val email = binding.ETBusEmailAd.editText?.text.toString()
                    val registerNumb = binding.ETBusCompRegNo.editText?.text.toString()
                    val telNumber = binding.ETBusTelNo.editText?.text.toString()
                    val businessTyp = binding.ETBusType.editText?.text.toString()


                }
                else
                {
                    Toast.makeText(this@BusinessSettings, "Personal Details node does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error: ${databaseError.message}")
            }
        })
    }

    private fun initialiseBusinessTextFields()
    {

    }
    //function to display user's email to display on screen
    private fun retrieveUserEmail()
    {

        // Get the current user
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail = findViewById<TextView>(R.id.tv_email)
        // making sure user is logged in
        if (user != null)
        {
            // User is signed in
            val email = user.email
            userEmail.text = email

        }
        else
        {
            // User is not signed in or email not found
            userEmail.text = ""
        }
    }

    //method that retrieves a user's name to display on screen
    private fun retrieveName()
    {

        val userName = findViewById<TextView>(R.id.tv_name)

        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID!!).child("Personal Details")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot contains the data at the "Personal Details" node

                    // Assuming "name" is a field under "Personal Details"
                    val name = dataSnapshot.child("fullName").getValue(String::class.java)

                    // Check if name is not null before using it
                    if (name != null)
                    {
                        // User is signed in
                        val name1 = name
                        userName.text = name1
                    }
                    else
                    {
                        userName.text = ""
                        Toast.makeText(this@BusinessSettings, "name not found", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(this@BusinessSettings, "Personal Details node does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error: ${databaseError.message}")
            }
        })
    }
    //----------------------------------------------------------------------------------------------
    //Checking permissions in gallery
    private fun galleryCheckPermission()
    {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?)
                {
                     gallery()

                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (!rationaleDialogShown)
                    {
                        Toast.makeText(
                            this@BusinessSettings,
                            "You have denied the storage permission to select images",
                            Toast.LENGTH_SHORT
                        ).show()
                        // showRationalDialogForPermission()
                        rationaleDialogShown = true
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }


            }).check()
    }
    //----------------------------------------------------------------------------------------------

    private fun gallery()
    {
        val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.type = "image/*"
        ActivityResultLauncher.launch(myfileintent)
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

    private fun logoutUI()
    {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log-out?")
            .setNeutralButton("Dismiss") { dialog, which ->
                dialog.dismiss()
            }

            .setPositiveButton("Sign out") { dialog, which ->
                val intent = Intent(this, Welcome::class.java)
                startActivity(intent)
            }
            .show()
    }
}