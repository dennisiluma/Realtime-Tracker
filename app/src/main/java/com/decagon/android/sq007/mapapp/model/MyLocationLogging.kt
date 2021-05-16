package com.decagon.android.sq007.mapapp.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class MyLocationLogging(
    var Latitude: Double? = 0.0,
    var Longitude: Double? = 0.0
)
