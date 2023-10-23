package com.example.hambaapp.Remote

import com.example.hambaapp.Model.MyPlaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

//----------------------------------------------------------------------------
//Part of Google Maps Places API integration
interface IGoogleAPIService {
    @GET
    fun getNearbyPlaces(@Url url:String) : Call<MyPlaces>
}
//------------------------------------------<<<<<<<<<<<-End Of File->>>>>>>>>>>-------------------------