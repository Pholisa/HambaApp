package com.example.hambaapp.Model

//----------------------------------------------------------------------------
//Classes created for locating nearby places on map using Places API
class Results {
    var  business_status: String?=null
    var  geometry: Geometry?=null
    var icon: String?=null
    var icon_background_color: String?=null
    var icon_mask_base_uri: String?=null
    var name: String?=null
    var photos: Array<Photos>?=null
    var place_id: String?=null
    var plus_code:PlusCode?=null
    var price_level: Int=0
    var rating: Double=0.0
    var reference: String?=null
    var scope: String?=null
    var types: Array<String>?=null
    var user_ratings_total: Int=0
    var vicinity: String?=null
    var opening_hours:OpeningHours?=null
    var permanently_closed: Boolean = false
}
//------------------------------------------<<<<<<<<<<<-End Of File->>>>>>>>>>>-------------------------