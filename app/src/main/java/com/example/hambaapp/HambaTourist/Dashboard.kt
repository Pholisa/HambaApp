package com.example.hambaapp.HambaTourist

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hambaapp.R
import com.example.hambaapp.databinding.ActivityDashboardBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var recyclerViewTourism: RecyclerView
    val businessArrayList = mutableListOf<BusinessDetailPublic1>() //dictionary to store business data from database
    val businessArrayList1 = mutableListOf<BusinessDetailPublic1>()
    //private val userID = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var databaseReference: DatabaseReference
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //setting the recycler viewer
        recyclerViewTourism = findViewById(R.id.rvBusinessDisplaying)
        recyclerViewTourism.layoutManager = LinearLayoutManager(this)

        //getting business data from database
        getBusinessData()

        //displaying data based in categories
        bindingsForCategoryClickListeners()

        //calling navigation bar function
        navigationBar()
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //function for on click listeners for categories
    private fun bindingsForCategoryClickListeners()
    {
        //displaying business in accomodation categroy
        binding.layBed.setOnClickListener {
            businessArrayList1.clear()
            travelCategory("Accommodation")
        }

        //displaying business in travel categroy
        binding.layCar.setOnClickListener {
            businessArrayList1.clear()
            travelCategory("Travel")
        }

        //displaying business in other categroy
        binding.layGear.setOnClickListener {
            businessArrayList1.clear()
            travelCategory("Other")
        }

        //displaying business in food & entertainment categroy
        binding.layEntertain.setOnClickListener {
            businessArrayList1.clear()
            travelCategory("Food & Entertainment")
        }
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //Displaying business data based on categories
    private fun travelCategory(travel:String)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Businesses")
        val query: Query = databaseReference.orderByChild("category").equalTo(travel)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) //if value exists
                {
                    //businessArrayList1.clear()
                    var businessCount = dataSnapshot.childrenCount.toInt()
                    var businessTotal = findViewById<TextView>(R.id.tv_heading2)
                    businessTotal.text = "("+businessCount.toString()+")"+" places"
                    for (businessSnapshot in dataSnapshot.children) //for each loop to go scan every value in the database
                    {
                        val businessItem = businessSnapshot.getValue(BusinessDetailPublic1::class.java)
                        businessItem?.let {
                            businessArrayList1.add(it) //adding found datasnapshot to arraylist
                        }
                    }
                    //calling bottom sheet behaviour class
                    adapterSetUp(businessArrayList1)
                }
                else //if value doesnt exist
                {
                    Toast.makeText(applicationContext, "cant find data", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError)
            {
                // Handle error
            }
        })
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //Function that will retrieve all business data irrespective of category
    private fun getBusinessData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Businesses")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    // Count the number of items
                     var businessCount = snapshot.childrenCount.toInt()
                    var businessTotal = findViewById<TextView>(R.id.tv_heading2)
                    businessTotal.text = "("+businessCount.toString()+")"+" places"
                    for (businessSnapshot in snapshot.children)
                    {
                        val busines = businessSnapshot.getValue(BusinessDetailPublic1::class.java)
                        businessArrayList.add(busines!!)
                    }
                    //calling bottom sheet behaviour class
                    adapterSetUp(businessArrayList)
                }
                else //no businesses in database
                {
                    // Toast.makeText(applicationContext, "You currently have no businesses saved", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(this@Dashboard, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //bottom sheet
    private fun adapterSetUp(theList: MutableList<BusinessDetailPublic1>)
    {
        val adapter = TourismAdapter(this@Dashboard, theList,
            onItemClickListener = { userProfile  ->
                // Bottom sheet data
                val sheet1 = findViewById<FrameLayout>(R.id.sheet1)
                BottomSheetBehavior.from(sheet1).apply {
                    peekHeight = 0
                    state = BottomSheetBehavior.STATE_EXPANDED
                    //calling function that populates sheet with data from database
                    sheetPopulation(userProfile )
                }
            }
        )
        recyclerViewTourism.adapter = adapter
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //function that will populate bottom sheet with data
    private fun sheetPopulation(userProfile: BusinessDetailPublic1)
    {
        // Set up your views in the bottom sheet
        val coverImage: ImageView = binding.openImageDBV
        val businessName: TextView = binding.busTitleDBV
        val businessLocation : TextView = binding.contLocationDBV
        val businessPrice: TextView = binding.tvPriceDBV
        val businessSummary: TextView = binding.tvBusDescptionDBV
        val number: TextView = binding.contPhoneDBV
        val email: TextView = binding.ContEmailDBV

        // Setting image
        val imageBytes = Base64.decode(userProfile.stringImage, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        coverImage.setImageBitmap(bitmap)

        // Set text values
        businessName.text = userProfile.title
        businessLocation.text = userProfile.locationString
        businessPrice.text = userProfile.price
        businessSummary.text = userProfile.businessSummary
        number.text = userProfile.telephoneNo
        email.text = userProfile.emailAd
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //displaying navigation function
    private fun navigationBar()
    {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // You are already in the Dashboard, so no need to navigate again.
                }

                R.id.location -> {
                    //mapActivity()
                    val intent = Intent(this, MapsActivity::class.java)
                   startActivity(intent)
                }

                R.id.favourites -> {
                    val intent = Intent(this, Favourites::class.java)
                    startActivity(intent)
                }
                R.id.profile -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
            true
        }
    }
    //----------------------------------------------------------------------------------------------
}
//------------------------------------------ooo000EndOfFile000ooo-----------------------------------
