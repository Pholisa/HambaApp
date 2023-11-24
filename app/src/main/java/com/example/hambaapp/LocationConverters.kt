package com.example.hambaapp
import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.*

class LocationConverters(private val context: Context) {

    fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addressText = ""

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty())
            {
                val address: Address = addresses[0]
                addressText = address.getAddressLine(0) // Get the first address line
            } else {
                addressText = "Address not found"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            addressText = "Error occurred while retrieving address"
        }

        return addressText
    }
}