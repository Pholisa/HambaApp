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
        val businessUR = findViewById<Button>(R.id.btn_tourist)
        val admin = findViewById<TextView>(R.id.tvAdminSignIn)

        //To access tourist UI
        touristUR.setOnClickListener {
            var intent = Intent(this, BusinessSignIn::class.java)
            startActivity(intent)
        }

        //To access business UI
        businessUR.setOnClickListener {
            var intent = Intent(this, TouristSignIn::class.java)
            startActivity(intent)
        }

        //To access business UI
        admin.setOnClickListener {
            var intent = Intent(this, AdminSignIn::class.java)
            startActivity(intent)
        }
    }
}