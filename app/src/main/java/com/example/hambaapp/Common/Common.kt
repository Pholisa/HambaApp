package com.example.hambaapp.Common

import com.example.hambaapp.Remote.IGoogleAPIService
import com.example.hambaapp.Remote.RetrofitClient

//----------------------------------------------------------------------------
//This object will be used to fetch the Service API
object Common {
    private val GOOGLE_API_URL="https://maps.googleapis.com/"

    val googleApiService: IGoogleAPIService
        get()= RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}
//------------------------------------------<<<<<<<<<<<-End Of File->>>>>>>>>>>-------------------------