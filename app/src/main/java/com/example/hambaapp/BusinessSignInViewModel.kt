package com.example.hambaapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BusinessSignInViewModel : ViewModel() {
    private val _businessSetupStatus = MutableLiveData<BusinessSetupStatus>()
    val businessSetupStatus: LiveData<BusinessSetupStatus> get() = _businessSetupStatus
    private lateinit var firebaseAuthentication: FirebaseAuth
    private val theDatabase = Firebase.database
    private val userID: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val myReference by lazy { userID?.let { theDatabase.getReference("users").child(it).child("Business Information") } }
    private val myReference2 by lazy { userID?.let { theDatabase.getReference("Approved Requests").child(userID!!) } }
    private val myReference3 by lazy { userID?.let { theDatabase.getReference("Account Requests").child(userID!!) } }

    fun checkBusinessSetup(userID: String?, myReference: DatabaseReference?) {
        myReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // If the first layout is set up, check the second layout
                    checkSecondLayout(userID, myReference2, myReference3)
                } else {
                    _businessSetupStatus.value = BusinessSetupStatus.SetupComplete(false, "Your application has not been accepted yet")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _businessSetupStatus.value = BusinessSetupStatus.Error(error.toString())
            }
        })
    }

    fun checkSecondLayout(userID: String?, myReference2: DatabaseReference?, myReference3: DatabaseReference?) {
        myReference2?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    _businessSetupStatus.value = BusinessSetupStatus.SetupComplete(true)
                } else {
                    myReference3?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(innerSnapshot: DataSnapshot) {
                            if (innerSnapshot.exists()) {
                                _businessSetupStatus.value = BusinessSetupStatus.SetupComplete(false, "Your application has not been approved yet")
                            } else {
                                // If neither snapshot exists, indicate an error
                                _businessSetupStatus.value = BusinessSetupStatus.Error("Invalid application state")
                            }
                        }

                        override fun onCancelled(innerError: DatabaseError) {
                            _businessSetupStatus.value = BusinessSetupStatus.Error(innerError.toString())
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _businessSetupStatus.value = BusinessSetupStatus.Error(error.toString())
            }
        })
    }
}
