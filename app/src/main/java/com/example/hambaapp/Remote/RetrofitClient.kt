package com.example.hambaapp.Remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//----------------------------------------------------------------------------
//Part of Google Maps Place API integration
object RetrofitClient {
    private var retrofit: Retrofit?=null

    fun getClient (baseUrll:String): Retrofit {
        if (retrofit==null) {
            retrofit = Retrofit.Builder().baseUrl(baseUrll)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
//------------------------------------------<<<<<<<<<<<-End Of File->>>>>>>>>>>-------------------------