package com.example.hambaapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hambaapp.HambaBusiness.Information
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyViewModel : ViewModel() {

    private val _businessList = MutableLiveData<List<Information>>()
    val businessList: LiveData<List<Information>> get() = _businessList

    private lateinit var databaseReference: DatabaseReference

    fun retrieveBusinessDataFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Account Requests")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val businesses = mutableListOf<Information>()
                for (businessSnapshot in snapshot.children) {
                    val myBiz = businessSnapshot.getValue(Information::class.java)
                    myBiz?.let { businesses.add(it) }
                }
                _businessList.value = businesses
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
