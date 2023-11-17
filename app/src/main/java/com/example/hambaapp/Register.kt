package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hambaapp.HambaBusiness.User
import com.example.hambaapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuthentication: FirebaseAuth

    private val theDatabase = Firebase.database
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    val myReference = theDatabase.getReference("users").child(userID ?: "").child("Personal Details")


    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthentication = FirebaseAuth.getInstance()


        binding.btnSignUp.setOnClickListener{
            val username = binding.edEmailAddress.text.toString()
            val password = binding.edPassword.text.toString()
            val confirmPassword = binding.edPasswordConfirm.text.toString()
            val name = binding.edFullName.text.toString()
            val number = binding.edPhoneNumber.text.toString()


            if(username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty())
            {
                if(password == confirmPassword)
                {
                    firebaseAuthentication.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            /*val fullName = findViewById<EditText>(R.id.edFullName)
                            var name = fullName.text.toString().trim()
                            val phoneNumber = findViewById<EditText>(R.id.edPhoneNumber)
                            var number = phoneNumber.text.toString().trim()
                            myReference.push().setValue(name)
                            myReference.push().setValue(number)*/

                            //database = FirebaseDatabase.getInstance().getReference("Users")
                            val User = User(name, number)
                            myReference.setValue(User).addOnSuccessListener {
                                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, TouristSignIn::class.java)
                                startActivity(intent)
                            }

                        }
                        else
                        {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else
                {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        //Currently redirects user to register tourist only
        binding.tvLogin1.setOnClickListener {
            val loginIntent = Intent(this, Welcome::class.java)
            startActivity(loginIntent)
        }
    }

    }
