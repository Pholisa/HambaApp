package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hambaapp.databinding.ActivityAdminSignInBinding
import com.google.firebase.auth.FirebaseAuth

class AdminSignIn : AppCompatActivity() {

    private lateinit var binding: ActivityAdminSignInBinding
    private lateinit var firebaseAuthentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthentication = FirebaseAuth.getInstance()

        //If signing in button is clicked
        binding.btnSignInAd.setOnClickListener{
            val username = binding.edLoginEmailAd.text.toString()
            val pass = binding.edLoginPasswordAd.text.toString()

            if (username.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuthentication.signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, AdminDashboard::class.java)
                            startActivity(intent)
                        } else
                        {
                            Toast.makeText(this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else
                {

                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }