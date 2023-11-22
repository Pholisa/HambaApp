package com.example.hambaapp.HambaBusiness

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityBusinessListingDataBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.hambaapp.Welcome
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.IOException

class BusinessListingData : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessListingDataBinding
    private lateinit var database: DatabaseReference

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference = theDatabase.getReference("users").child(userID!!).child("Listing Data")
    private val myReference2 = theDatabase.getReference("Businesses")
   // private val myReference = theDatabase.getReference("users").child(userID!!).child("Business Information")
    private lateinit var firebaseAuthentication: FirebaseAuth
    private var stringImage: String = ""

    private val galleryRequestCode = 2

    private lateinit var placesClient: PlacesClient
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var theLocation: LatLng = LatLng(0.0, 0.0)
    private var theLocationStringPersonal: String = ""
    private var theLocationStringPublic: String = ""
    private var selectedCategory: String = ""

    private val predictions: MutableList<AutocompletePrediction> = mutableListOf()


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

        //calling category function
        getSelectedCategory()
        //calling nav bar function
        navigationBar()

        /////////////////////////////////////
        // Initialize the Places API with your API key
        Places.initialize(applicationContext, "AIzaSyD78Ws9Y4GtZVZtYP9pWBXjHjMNDwGJRbQ")
        placesClient = Places.createClient(this)

        // Initialize AutoCompleteTextView
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)

        // Set up the adapter for autocomplete suggestions
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        autoCompleteTextView.setAdapter(adapter)

        // Set a listener for text changes to trigger autocomplete predictions
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length >= 2) { // You may adjust the minimum length as needed
                    getAutocompletePredictions(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Set a listener for item selection
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            theLocationStringPersonal = autoCompleteTextView.text.toString()

            getLatLngFromAddress(autoCompleteTextView.text.toString())
        }

        binding.btnBusDescNext.setOnClickListener {
            ValidateData()

        }

    }

    private fun getAutocompletePredictions(query: String) {
        // Create a request for autocomplete predictions
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                val predictionStrings = mutableListOf<String>()

                for (prediction in predictions) {
                    predictionStrings.add(prediction.getFullText(null).toString())
                }

                // Update the adapter with the new predictions
                val adapter = autoCompleteTextView.adapter as ArrayAdapter<String>
                adapter.clear()
                adapter.addAll(predictionStrings)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Autocomplete", "Prediction fetching failed: ${exception.localizedMessage}")
            }
    }

    private fun getLatLngFromAddress(address: String) {
        try {
            val geocoder = Geocoder(this)
            val addresses: List<Address>? = geocoder.getFromLocationName(address, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val location = addresses[0]
                val latLng = LatLng(location.latitude, location.longitude)

                Log.d("Autocomplete", "LatLng: $latLng")

               // saveToFirebase(latLng)
               // theLocationString = latLng.toString()
                theLocationStringPublic = latLng.latitude.toString()+","+latLng.longitude.toString()
            } else {
                Log.e("Autocomplete", "No result found for the given address.")
            }
        } catch (e: IOException) {
            Log.e("Autocomplete", "Geocoding failed: ${e.localizedMessage}")
        }
    }

    private fun getSelectedCategory()
    {
        val states = listOf("Accommodation", "Food & Entertainment", "Travel", "Other")
        val dropdownMenu: Spinner = findViewById(R.id.dropdownMenu)
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, states)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdownMenu.adapter = adapter

        dropdownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = dropdownMenu.selectedItem?.toString() ?: ""
                Toast.makeText(applicationContext, "You selected: $selectedCategory", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // Handle nothing selected event if needed
            }
        }
    }


    private fun ValidateData()
    {
        val title = binding.ETBusTitle.editText?.text.toString()
        val location = theLocationStringPublic
        val number = binding.ETBusNumber.editText?.text.toString()
        val email = binding.ETBusEmail.editText?.text.toString()
        val price = binding.ETBusPrice.editText?.text.toString()

        val businessSummary = binding.ETBusDescrip.editText?.text.toString()
       // val selectedCategory = getSelectedCategory()
        if (binding.ETBusTitle.editText?.text.toString().isNotEmpty()
            ||theLocationStringPersonal.isNotEmpty()||
            binding.ETBusTitle.editText?.text.toString().isNotEmpty() ||
            binding.ETBusEmail.editText?.text.toString().isNotEmpty() ||
            binding.ETBusDescrip.editText?.text.toString().isNotEmpty() ||selectedCategory.isNotEmpty()
        )
        {
            //saving data to thr database
            val description = BusinessDetail(title, theLocationStringPersonal,price, businessSummary, stringImage)
            val descriptionPublic = BusinessDetailPublic1(title, location,theLocationStringPersonal ,selectedCategory,price, businessSummary, stringImage, email, number)
             myReference.push().setValue(description).addOnSuccessListener {
                 myReference2.push().setValue(descriptionPublic)
                // Toast.makeText(this, "selected category is $selectedCategory", Toast.LENGTH_SHORT).show()
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
                val intent = Intent(this, Welcome::class.java)
                startActivity(intent)
            }
            .show()
    }
}