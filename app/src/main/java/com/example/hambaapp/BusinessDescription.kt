package com.example.hambaapp

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
import com.example.hambaapp.databinding.BusinessDescriptionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream


class BusinessDescription : AppCompatActivity() {

    private lateinit var binding: BusinessDescriptionBinding
    private lateinit var database: DatabaseReference

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("Listing Data").child(userID!!)
    private lateinit var firebaseAuthentication: FirebaseAuth
    private var stringImage: String = ""

    private val galleryRequestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BusinessDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBusImageUpload.setOnClickListener {
            galleryCheckPermission()
            val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.type = "image/*"
            ActivityResultLauncher.launch(myfileintent)
        }

        binding.btnBusDescNext.setOnClickListener {
            ValidateData()
            val intentNext = Intent(this, BusinessPrev::class.java)
            startActivity(intentNext)
        }

        //calling nav bar function
        navigationBar()
    }

    private fun ValidateData() {
        val title = binding.ETBusTitle.text.toString()
        val summary = binding.ETBusDescrip.text.toString()

        if (binding.ETBusTitle.text.toString().isEmpty() ||
            binding.ETBusDescrip.text.toString().isEmpty() || stringImage.isEmpty()
        ) {
            Toast.makeText(this, "Please enter all fields and select images", Toast.LENGTH_SHORT).show()
        } else {
            val description = BusinessDetails(title, summary, stringImage)
            myReference.setValue(description).addOnSuccessListener {
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()
            }
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

    //Checkinh permissions in gallery
    private fun galleryCheckPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    gallery()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@BusinessDescription,
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