package com.decagon.android.sq007.pokemonapp.data

// Data Class that receives the first set of data from which the results list is extracted from.
data class PokeAll(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
)
