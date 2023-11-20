package com.example.hambaapp.HambaBusiness

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityBusinessListingDataBinding
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


class BusinessListingData : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessListingDataBinding
    private lateinit var database: DatabaseReference

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("users").child(userID!!).child("Listing Data")
   // private val myReference = theDatabase.getReference("users").child(userID!!).child("Business Information")
    private lateinit var firebaseAuthentication: FirebaseAuth
    private var stringImage: String = ""

    private val galleryRequestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessListingDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBusImageUpload.setOnClickListener {
            galleryCheckPermission()
            val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.type = "image/*"
            ActivityResultLauncher.launch(myfileintent)
        }

        binding.btnBusDescNext.setOnClickListener {
            ValidateData()

        }

        //calling nav bar function
        navigationBar()
    }

    private fun ValidateData()
    {
        val title = binding.ETBusTitle.editText?.text.toString()
        val location = binding.ETBusAddress.editText?.text.toString()
        val price = binding.ETBusPrice.editText?.text.toString()
        val businessSummary = binding.ETBusDescrip.editText?.text.toString()

        if (binding.ETBusTitle.editText?.text.toString().isNotEmpty()
            ||binding.ETBusAddress.editText?.text.toString().isNotEmpty() ||
            binding.ETBusDescrip.editText?.text.toString().isNotEmpty()
        )
        {
            //savind data to thr database
            val description = BusinessDetail(title, location,price, businessSummary, stringImage)
             myReference.push().setValue(description).addOnSuccessListener {
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()
                val intentNext = Intent(this, BusinessDashboard::class.java)
                startActivity(intentNext)
            }


        }
        else
        {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private val ActivityResultLauncher =
        registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data!!.data
                try {
                    val inputStream = contentResolver.openInputStream(uri!!)
                    val myBitmap = BitmapFactory.decodeStream(inputStream)
                    val stream = ByteArrayOutputStream()
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val bytes = stream.toByteArray()
                    stringImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                    //stringImages.add(stringImage)
                    // You can add the image to an ImageView or display some feedback to the user here.
                    inputStream!!.close()
                } catch (ex: Exception) {
                    Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
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
                        this@BusinessListingData,
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