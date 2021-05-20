package com.decagon.android.sq007.pokemonapp.data

// The Data Class for the Results that are populated on the main recyclerview. The URL is used in also
// getting the ID which is then used to Glide in images.
data class Result(
    val name: String,
    val url: String
)
