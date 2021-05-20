package com.decagon.android.sq007.pokemonapp.ui

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object RetrofitClient {
    val BASE_URL = "https://pokeapi.co/api/v2/"
    //Retrogit client that is used to call and get the data
        //build the retrofit
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)
}