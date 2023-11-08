package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hambaapp.databinding.ActivitySignInAdminBinding
import com.google.firebase.auth.FirebaseAuth

class SignInAdmin : AppCompatActivity() {

    private lateinit var binding: ActivitySignInAdminBinding
    private lateinit var firebaseAuthentication: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthentication = FirebaseAuth.getInstance()

        binding.btnSignInAd.setOnClickListener {
            val username = binding.edLoginEmailAd.text.toString()
            val password = binding.edLoginPasswordAd.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {

                firebaseAuthentication.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, DashboardAdmin::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {

                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }


    }
}