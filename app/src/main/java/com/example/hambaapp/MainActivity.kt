package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hambaapp.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonRegister = findViewById<Button>(R.id.btnSignUp)

        buttonLogin.setOnClickListener {
            //val goToLogin = Intent(this, Login::class.java)
            //startActivity(goToLogin)
            startActivity(Intent(this, Login::class.java))

        }
        buttonRegister.setOnClickListener {
            //val goToRegister = Intent(this, Register::class.java)
            //startActivity(goToRegister)
            startActivity(Intent(this, Register::class.java))
        }
    }
}