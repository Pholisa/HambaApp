package com.example.hambaapp.HambaBusiness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hambaapp.R

class DisplayBusinessDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_business_data)

        // Sample business data, replace with your actual data
        val businessName = "Sample Business"
        val email = "sample@example.com"
        val address = "123 Main Street"
        val phoneNumber = "123-456-7890"

        // Display business data
        //tv_name.text = businessName
        //tv_email.text = email
        //tv_address.text = address
        //tv_phone_number.text = phoneNumber

        // Edit button click listener
        //btn_edit.setOnClickListener {
            // Start the BusinessSettings activity for editing
           // val intent = Intent(this, BusinessSettings::class.java)
           // startActivity(intent)
        //}
    }
}