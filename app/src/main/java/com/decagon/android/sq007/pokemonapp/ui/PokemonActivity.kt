package com.decagon.android.sq007.pokemonapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.pokemonapp.NetworkConnection
import com.decagon.android.sq007.pokemonapp.adapter.RecyclerAdapter
import com.decagon.android.sq007.pokemonapp.data.PokeAll
import retrofit2.*

class PokemonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

        val recyclerView = findViewById<RecyclerView>(R.id.rvRecyclerView)
        recyclerView.setHasFixedSize(true)


        val mAdapter = RecyclerAdapter(this)
        recyclerView.adapter = mAdapter

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        //newtwork connection observing our ui for network connectivity
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer {isConnected->
            if (isConnected){
                /* get data from retrofit builder. The "getData() is the get function defined in the APiInerface.
            we were able to access it because we have map map the ApiInterface::claass.java via the create method when we were
            building our retrofit
             */

                //instanciate a retrofit builder
                val retrofitData = RetrofitClient.retrofitBuilder.getData()

                retrofitData.enqueue(object : Callback<PokeAll?> {
                    override fun onFailure(call: Call<PokeAll?>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<PokeAll?>, response: Response<PokeAll?>) {
                        val responseBody = response.body()
                        responseBody?.results?.let { mAdapter.setPokemonList(it) }

                        mAdapter.notifyDataSetChanged()

                        Toast.makeText(this@PokemonActivity, "It has worked",Toast.LENGTH_LONG).show()
                    }
                })
                Toast.makeText(this, "You are Connected", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "You are not connected, Please Switched on Your network", Toast.LENGTH_LONG).show()
            }
        })
        }
    }