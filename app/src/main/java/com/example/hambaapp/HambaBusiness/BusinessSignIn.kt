package com.example.hambaapp.HambaBusiness

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.hambaapp.R
import com.example.hambaapp.Register
import com.example.hambaapp.databinding.ActivityBusinessSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BusinessSignIn : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessSignInBinding
    private lateinit var firebaseAuthentication: FirebaseAuth
    private val theDatabase = Firebase.database
    private lateinit var myReference: DatabaseReference
    private var companyName: String = ""
   private var myReference1 = theDatabase.getReference("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthentication = FirebaseAuth.getInstance()
        setupUI()

        binding.tvRegisterRedirectText2.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.tvBusForgot.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun setupUI() {
        binding.btnBusSignIn.setOnClickListener {
            val username = binding.edLoginEmailBus.editText?.text.toString()
            val pass = binding.edLoginPasswordBus.editText?.text.toString()

            if (username.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuthentication.signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var userID = FirebaseAuth.getInstance().currentUser?.uid
                            checkBusinessSetup(userID!!)
                        } else {
                            Toast.makeText(this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkBusinessSetup(userID:String) {
        myReference = theDatabase.getReference("users").child(userID!!).child("Business Information")
        myReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    startAppropriateActivity(true)
                } else
                {
                    startAppropriateActivity(false)
                    //    Toast.makeText(applicationContext, "No business set up yet.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BusinessSignIn, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startAppropriateActivity(a: Boolean) {
        val intent = if (a.equals(true))
        {
            Intent(this, BusinessDashboard::class.java)
        }
        else
        {
            Intent(this, BusinessListIntro::class.java)
        }
        startActivity(intent)
        finish()  // Optional: finish the current activity to prevent going back to the sign-in screen
    }

    private fun showForgotPasswordDialog() {
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

        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        dialog.show()
    }

    private fun compareEmail(email: EditText) {
        if (email.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            return
        }

        firebaseAuthentication.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
