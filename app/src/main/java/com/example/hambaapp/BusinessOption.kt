package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class BusinessOption : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_option)


        val registerBusiness = findViewById<Button>(R.id.btnRegisterBus)
        val signInBusiness = findViewById<Button>(R.id.btnSignInBus)


        //To access business register UI
        registerBusiness.setOnClickListener {
            var intent = Intent(this, BusinessInfo::class.java)
            startActivity(intent)
        }

        //To access business signin UI
        signInBusiness.setOnClickListener {
            var intent = Intent(this, BusinessSignIn::class.java)
            startActivity(intent)
        }

    }
}