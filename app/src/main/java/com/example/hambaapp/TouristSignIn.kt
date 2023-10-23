package com.example.hambaapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.hambaapp.databinding.ActivityTouristSignInBinding
import com.google.firebase.auth.FirebaseAuth


class TouristSignIn : AppCompatActivity() {

    private lateinit var binding: ActivityTouristSignInBinding
    private lateinit var firebaseAuthentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTouristSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthentication = FirebaseAuth.getInstance()

        var register = findViewById<TextView>(R.id.tvTourstReg)
        val login = findViewById<Button>(R.id.btnLogin)
        val resetPass = findViewById<TextView>(R.id.tvForgot)

        //Login button click event
       login.setOnClickListener {
            val username = binding.edLoginEmailAddress.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {

                firebaseAuthentication.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, Dashboard::class.java)
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

        binding.tvTourstReg.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        //resetting password is user forgot
        resetPass.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.activity_forgot_password, null)
            val userEmailAddress = view.findViewById<EditText>(R.id.editBox)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmailAddress)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()

            }
            if(dialog.window != null)
            {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            }
            dialog.show()
        }

        //Going to Register screen if user does not have an account already
        register.setOnClickListener {
            val signupIntent = Intent(this, Register::class.java)
            startActivity(signupIntent)
        }

    }

    private fun compareEmail(email: EditText)
    {
        if(email.text.toString().isEmpty())
        {
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches())
        {
            return
        }
        firebaseAuthentication.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
            task ->
            if(task.isSuccessful)
            {
                Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}