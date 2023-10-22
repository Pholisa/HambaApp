package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonRegister = findViewById<Button>(R.id.btnSignUp)

        buttonLogin.setOnClickListener {

            startActivity(Intent(this, TouristSignIn::class.java))

        }
        buttonRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}