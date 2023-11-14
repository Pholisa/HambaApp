package com.example.hambaapp

data class BusinessDetails(
    val title: String? = null,
    val businessSummary: String? = null,
    val stringImages: String? = null
)

data class User(val fullName: String? = null,val phoneNumber: String? = null)
data class Information(val companyName: String? = null,val registerNumber: String? = null, val emailAddress: String? = null,
                   val telephoneNumber: String? = null,val businessType: String? = null,val businessAddress: String? = null,val businessCategory: String? = null )