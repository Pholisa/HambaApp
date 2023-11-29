package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hambaapp.HambaBusiness.BusinessSignIn
import com.example.hambaapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuthentication: FirebaseAuth
    private val theDatabase = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthentication = FirebaseAuth.getInstance()


        binding.btnSignUp.setOnClickListener{
            val email = binding.edEmailAddress.editText?.text.toString()
            val password = binding.edPassword.editText?.text.toString()
            val confirmPassword = binding.edPasswordConfirm.editText?.text.toString()
            val name = binding.edFullName.editText?.text.toString()
            val number = binding.edPhoneNumber.editText?.text.toString()


            if(email.isNotEmpty() && password.isNotEmpty()&& name.isNotEmpty() && confirmPassword.isNotEmpty())
            {
                if(password == confirmPassword)
                {
                    firebaseAuthentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            val userID = FirebaseAuth.getInstance().currentUser?.uid
                            val myReference = theDatabase.getReference("users").child(userID!!).child("Personal Details")

                          //  val User = User(email,name, number)
                            val User1 = User1(email,name, number,"businessowner")
                            myReference.setValue(User1).addOnSuccessListener {
                                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, BusinessSignIn::class.java)
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
            val loginIntent = Intent(this, BusinessSignIn::class.java)
            startActivity(loginIntent)
        }
    }

    }
