package com.example.hambaapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.hambaapp.databinding.ActivityBusinessSignInBinding
import com.example.hambaapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class BusinessSignIn : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessSignInBinding
    private lateinit var firebaseAuthentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding = ActivityBusinessSignInBinding.inflate(layoutInflater)

        firebaseAuthentication = FirebaseAuth.getInstance()
        binding.btnBusSignIn.setOnClickListener{
            val username = binding.edLoginEmailBus.text.toString()
            val pass = binding.edLoginPasswordBus.text.toString()

            if (username.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuthentication.signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, BusinessInfo::class.java)
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

        binding.tvBusForgot.setOnClickListener{
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