package com.example.hambaapp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.hambaapp.databinding.ActivityBusinessInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class BusinessInfo : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessInfoBinding
    private lateinit var database: DatabaseReference

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("users").child(userID!!).child("Business Information")
    private lateinit var firebaseAuthentication: FirebaseAuth

    private val galleryRequestCode = 2


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()

        val buttonNext = findViewById<Button>(R.id.btnBusNext)

        binding.btnUploadImage.setOnClickListener {
            galleryCheckPermission()
            val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.type = "image/*"
        }

        buttonNext.setOnClickListener {
            validateData()
            val intentNext = Intent(this, BusinessPrev::class.java)
            startActivity(intentNext)

        }

    }
         private fun validateData()
         {

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
        )
        {
            Toast.makeText(this,"Please enter all fields", Toast.LENGTH_SHORT).show()
        }
        else
        {

            val Info = Information(companyName, registerNumber, emailAddress, telephoneNumber, businessType,businessAddress,businessCategory)
            myReference.push().setValue(Info).addOnSuccessListener {
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Checking permissions in gallery
    private fun galleryCheckPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    gallery()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@BusinessInfo,
                        "You have denied the storage permission to select images",
                        Toast.LENGTH_SHORT
                    ).show()
                    showRorationalDialogForPermission()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRorationalDialogForPermission()
                }
            }).onSameThread().check()
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, galleryRequestCode)
    }


    private fun showRorationalDialogForPermission(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permissions" +
                "required for this feature. It can be enabled under App settings!!!")
            .setPositiveButton("Go To SETTINGS"){_,_->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                catch(e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL"){dialog,_->
                dialog.dismiss()
            }.show()
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
                    val intent = Intent(this, BusinessPortal::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
}