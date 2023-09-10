package com.example.hambaapp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.*
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





class BusinessDescription : AppCompatActivity() {

     private lateinit var binding : BusinessDescriptionBinding
    private lateinit var database: DatabaseReference


    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("users").child(userID!!).child("Business Description")
    private lateinit var firebaseAuthentication: FirebaseAuth


    private var imageUri: Uri? = null
    var imageURL: String? = null

    private val galleryRequestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BusinessDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBusDescUpload.setOnClickListener {
            galleryCheckPermission()
            ValidateData()

        }
        navigationBar()


    }
    private fun ValidateData() {

        val title = binding.ETBusTitle.text.toString()
        val summary =binding.ETBusDescrip.text.toString()


        if (binding.ETBusTitle.text.toString().isEmpty() ||
            binding.ETBusDescrip.text.toString().isEmpty()
        ) {
            Toast.makeText(this,"Please enter all fields", Toast.LENGTH_SHORT).show()
        }
        else
        {
            /*val businessTitle = findViewById<EditText>(R.id.ETBusTitle)
            var title = businessTitle.text.toString().trim()

            val businessDescript = findViewById<EditText>(R.id.ETBusDescrip)
            var description = businessDescript.text.toString().trim()



            myReference.push().setValue(title)
            myReference.push().setValue(UploadImage())
            myReference.push().setValue(description)*/
            database = FirebaseDatabase.getInstance().getReference("Business Description")
            val description = BusinessDetails(title, summary)
            database.child(userID!!).setValue(description).addOnSuccessListener {
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun UploadImage()
    {
        val storageReference = FirebaseStorage.getInstance().reference.child("Task Image")
            .child(imageUri!!.lastPathSegment!!)

        var builder = AlertDialog.Builder(this@BusinessDescription)
        builder.setCancelable(false)
        //builder.setView(R.layout.progress_layout)
        var dialog = builder.create()
        dialog.show()

        storageReference.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            //UploadData()
            dialog.dismiss()

        }.addOnFailureListener {
            dialog.dismiss()
        }


    }


    private fun galleryCheckPermission(){

        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener (object : PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    gallery()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@BusinessDescription,
                        "You have denied the storage permission to select image",
                        Toast.LENGTH_SHORT).show()
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



    private fun gallery()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        //startActivityForResult(intent, galleryRequestCode)
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

