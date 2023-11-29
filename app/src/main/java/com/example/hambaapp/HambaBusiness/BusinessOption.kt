package com.example.hambaapp.HambaBusiness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hambaapp.R
import com.example.hambaapp.Register

class BusinessOption : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_option)

        //intitilising text viewws
        val registerBusiness = findViewById<Button>(R.id.btnRegisterBus)
        val signInBusiness = findViewById<Button>(R.id.btnSignInBus)

        //To access business register UI
        registerBusiness.setOnClickListener {
            var intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        //To access business signin UI
        signInBusiness.setOnClickListener {
            var intent = Intent(this, BusinessSignIn::class.java)
            startActivity(intent)
        }

    }
    //----------------------------------------------------------------------------------------------
}