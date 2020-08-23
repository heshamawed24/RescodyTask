package com.example.rescodytask.data.models

import com.google.android.gms.maps.model.LatLng


data class Messages(
    var id: String,
    var title: String,
    var category: String,
    var location: LatLng?

)