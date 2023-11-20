package com.example.hambaapp.HambaBusiness

data class BusinessDetail(
    val title: String? = null,
    val location: String? = null,
    val price: String? = null,
    val businessSummary: String? = null,
    val stringImage: String? = null
)

data class User(val fullName: String? = null,val phoneNumber: String? = null)
data class Information(
    val companyName: String? = null,
    val registerNumber: String? = null,
    val emailAddress: String? = null,
    val telephoneNumber: String? = null,
    val businessType: String? = null,
    val businessAddress: String? = null,
    val businessCategory: String? = null,
    val stringImage: String? = null
)