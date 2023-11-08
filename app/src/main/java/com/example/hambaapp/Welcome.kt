package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Welcome : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val touristUR = findViewById<Button>(R.id.btn_tourist)
        val businessUR = findViewById<Button>(R.id.btn_business)
        val admin = findViewById<TextView>(R.id.tvAdmnSignIn)

        //To access tourist UI
        touristUR.setOnClickListener {
            var intent = Intent(this, TouristSignIn::class.java)
            startActivity(intent)
        }

        //To access business UI
        businessUR.setOnClickListener {
            var intent = Intent(this, BusinessOption::class.java)
            startActivity(intent)
        }

        //To access business UI
        admin.setOnClickListener {
            var intent = Intent(this, SignInAdmin::class.java)
            startActivity(intent)
        }
    }
}