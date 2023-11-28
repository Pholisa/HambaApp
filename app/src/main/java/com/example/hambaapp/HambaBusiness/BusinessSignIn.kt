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

        // Initialize myReference here


        var signIn = findViewById<Button>(R.id.btn_busSignIn)
        signIn.setOnClickListener {
            val username = binding.edLoginEmailBus.text.toString()
            val pass = binding.edLoginPasswordBus.text.toString()

            if (username.isNotEmpty() && pass.isNotEmpty()) {
                signIn(username, pass)
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegisterRedirectText2.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.tvBusForgot.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun signIn(username: String, pass: String) {
        firebaseAuthentication.signInWithEmailAndPassword(username, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userID: String? = FirebaseAuth.getInstance().currentUser?.uid
                    getCurrentBusinessName(userID)
                    checkBusinessSetup1(userID)
                } else {
                    Toast.makeText(this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getCurrentBusinessName(userID: String?) {
        myReference1.child(userID!!).child("Business Information").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                companyName = dataSnapshot.child("companyName").value.toString()
            }
        }.addOnFailureListener {
            Toast.makeText(this@BusinessSignIn, "Error getting current business name", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    private fun checkBusinessSetup(userID: String?) {
        myReference = theDatabase.getReference("Approved Requests")
        if (!this::myReference.isInitialized) {
            // Initialize myReference here
            myReference = theDatabase.getReference("Approved Requests")
        }

        myReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val companyName1 = snapshot.child("companyName").value?.toString()
                if (companyName1 == companyName) {
                    Toast.makeText(this@BusinessSignIn, "approved req and name match found", Toast.LENGTH_SHORT).show()
                   // startAppropriateActivity(true)
                    val intent = Intent(this, BusinessDashboard::class.java)
                    startActivity(intent)
                }
                else
                {
                    // Handle case where company names do not match
                    Toast.makeText(this@BusinessSignIn, "1:$companyName1 and 2:$companyName", Toast.LENGTH_SHORT).show()
                }
            } else {
                checkSecondLayout(userID)
            }
        }.addOnFailureListener {
            Toast.makeText(this@BusinessSignIn, "Error checking Approved Requests", Toast.LENGTH_SHORT).show()
        }
    }

     */

    private fun checkBusinessSetup1(userID: String?) {
        val approvedRequestsReference = theDatabase.getReference("Approved Requests")

        approvedRequestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var keyFound: String? = null

                for (childSnapshot in dataSnapshot.children) {
                    val companyName1 = childSnapshot.child("companyName").value?.toString()

                    if (companyName1 == companyName) {
                       // Toast.makeText(this@BusinessSignIn, "1:$companyName1 and 2:$companyName", Toast.LENGTH_SHORT).show()
                        keyFound = childSnapshot.key
                        break
                    }
                }

                if (keyFound != null) {
                    // Key found, you can use it to get the specific entry
                    val specificEntryReference = approvedRequestsReference.child(keyFound)

                    specificEntryReference.get().addOnSuccessListener { snapshot ->
                        // Continue your existing logic
                        Toast.makeText(this@BusinessSignIn, "approved req and name match found", Toast.LENGTH_SHORT).show()
                        // startAppropriateActivity(true)
                        val intent = Intent(this@BusinessSignIn, BusinessDashboard::class.java)
                        startActivity(intent)
                    }.addOnFailureListener {
                        Toast.makeText(this@BusinessSignIn, "Error checking specific entry", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle the case where companyName is not found
                    Toast.makeText(this@BusinessSignIn, "Company Name not found in Approved Requests", Toast.LENGTH_SHORT).show()
                   // checkSecondLayout(userID)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Toast.makeText(this@BusinessSignIn, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun checkSecondLayout(userID: String?) {
        myReference = theDatabase.getReference("Account Requests").child(userID!!)
        myReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists())
            {
                val companyName1 = snapshot.child("companyName").value?.toString()
                if (companyName1 == companyName)
                {
                    Toast.makeText(applicationContext, "Application still pending", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this@BusinessSignIn, "no match name for account requests", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                //startAppropriateActivity(false)
                val intent = Intent(this, BusinessListIntro::class.java)
                startActivity(intent)
            }
        }.addOnFailureListener {
            Toast.makeText(this@BusinessSignIn, "Error checking Account Requests", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startAppropriateActivity(a: Boolean) {
        val intent = if (a) {
            Intent(this, BusinessDashboard::class.java)
        }
        else
        {
            Intent(this, BusinessListIntro::class.java)
        }
        startActivity(intent)
        finish()
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
            // Handle invalid email
        }
    }

}
