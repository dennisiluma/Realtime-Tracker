package com.decagon.android.sq007.pokemonapp.ui

import com.decagon.android.sq007.pokemonapp.data.PokeAll
import com.decagon.android.sq007.pokemonapp.data.PokiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


//This interface will be used by the retrofit, when it has been build, to make a call and receive data
interface ApiInterface {

    //make a get request call
    @GET("pokemon")
    fun getData(): Call<PokeAll>


    @GET("pokemon/{id}")
    fun getPokey(@Path("id") id: Int): Call<PokiData>
}