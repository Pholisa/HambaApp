package com.example.hambaapp.HambaBusiness

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityBusinessRegFormBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream

class BusinessRegForm : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessRegFormBinding
    private lateinit var database: DatabaseReference

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("users").child(userID!!).child("Business Information")
    private lateinit var firebaseAuthentication: FirebaseAuth
    private var stringImage : String = ""
    private val galleryRequestCode = 2
    private var rationaleDialogShown = false


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessRegFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //calling the navigation bar
        navigationBar()

        val buttonNext = findViewById<Button>(R.id.btnBusNext)

        binding.btnUploadImage.setOnClickListener {
            galleryCheckPermission()

        }

        buttonNext.setOnClickListener {
            validateData()
            val intentNext = Intent(this, BusinessPrev::class.java)
            startActivity(intentNext)

        }

    }
         private fun validateData()
         {

             val companyName = binding.ETBusCompName.editText?.text.toString()
             val registerNumber =binding.ETBusCompRegNo.editText?.text.toString()
             val emailAddress = binding.ETBusEmailAd.editText?.text.toString()
             val telephoneNumber=binding.ETBusTelNo.editText?.text.toString()
             val businessType = binding.ETBusType.editText?.text.toString()
             val businessAddress =binding.ETBusAddress.editText?.text.toString()
             val businessCategory =binding.ETBusCat.editText?.text.toString()

        if (binding.ETBusCompName.editText?.text.toString().isEmpty() ||
            binding.ETBusCompRegNo.editText?.text.toString().isEmpty() ||
            binding.ETBusEmailAd.editText?.text.toString().isEmpty() ||
            binding.ETBusTelNo.editText?.text.toString().isEmpty() ||
            binding.ETBusType.editText?.text.toString().isEmpty() ||
            binding.ETBusAddress.editText?.text.toString().isEmpty() ||
            binding.ETBusCat.editText?.text.toString().isEmpty()
        )
        {
            Toast.makeText(this,"Please enter all fields", Toast.LENGTH_SHORT).show()
        }
        else
        {

            val Info = Information(companyName, registerNumber, emailAddress, telephoneNumber, businessType,businessAddress,businessCategory,stringImage)
            myReference.push().setValue(Info).addOnSuccessListener {
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()
            }
        }

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

    //Checking permissions in gallery
    private fun galleryCheckPermission()
    {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?)
                {
                    // gallery()
                    val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
                    myfileintent.type = "image/*"
                    ActivityResultLauncher.launch(myfileintent)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (!rationaleDialogShown)
                    {
                        Toast.makeText(
                            this@BusinessRegForm,
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

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, galleryRequestCode)
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
                    val intent = Intent(this, ActiveBusinesses::class.java)
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
}