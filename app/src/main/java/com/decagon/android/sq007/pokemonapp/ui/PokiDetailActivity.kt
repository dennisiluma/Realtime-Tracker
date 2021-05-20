package com.decagon.android.sq007.pokemonapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.decagon.android.sq007.R
import com.decagon.android.sq007.pokemonapp.data.PokeAll
import com.decagon.android.sq007.pokemonapp.data.PokiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PokiDetailActivity : AppCompatActivity() {

    private lateinit var pokeyName: String
    private lateinit var pokeyUrl: String
    private var pokeyId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poki_detail)

        val name: TextView=findViewById(R.id.tvdName)
        val image: ImageView=findViewById(R.id.ivdImage)
        val heigth: TextView=findViewById(R.id.tvdHeigth)
        val wiigth: TextView=findViewById(R.id.tvdWeight)
        val abilities: TextView=findViewById(R.id.tvdAbilities)
        val base_exp: TextView=findViewById(R.id.tvdBase_Exp)
        val form_list: TextView=findViewById(R.id.tvdForm_List)
        val game_index: TextView=findViewById(R.id.tvdGame_Index)
        val held_itmes: TextView=findViewById(R.id.tvdHeld_Items)
        val movies_list: TextView=findViewById(R.id.tvdMovies_list)
        val order: TextView=findViewById(R.id.tvdOrder)
        val species: TextView=findViewById(R.id.tvdSpecies)
        val stats: TextView=findViewById(R.id.tvdStarts)
        val type:TextView=findViewById(R.id.tvdType)



        // Get the values of the keys that were received on click from Recycler Adapter.
        val bundle = intent
        pokeyName = ""
        pokeyUrl = ""
        pokeyId = 0

        if (bundle != null) {
            pokeyName = bundle.extras?.getString("NAME").toString().toUpperCase(Locale.ROOT) // Get the value of the name and convert it to Uppercase.
            pokeyUrl = bundle.extras?.getString("URL").toString() // Get the value of the URL and set as PokeyURL
            pokeyId = bundle.extras?.getInt("ID") ?: 0 // Get the value of ID
        }


        name.text = pokeyName
        Glide.with(this).load(pokeyUrl).into(image)


        /*
        * Use tetrofit client to pass ApiInterfase get request to collect required details from a paerticular pokimon using her id
        * */
        val retrofitData = RetrofitClient.retrofitBuilder.getPokey(pokeyId)

        retrofitData.enqueue(object : Callback<PokiData?> {
            override fun onFailure(call: Call<PokiData?>, t: Throwable) {
                Toast.makeText(this@PokiDetailActivity, "Fuck You", Toast.LENGTH_LONG).show()


            }

            override fun onResponse(call: Call<PokiData?>, response: Response<PokiData?>) {
                val responseBody = response.body()

                if (responseBody != null) {
                    Toast.makeText(this@PokiDetailActivity, "It has worked", Toast.LENGTH_LONG).show()
                    heigth.text = responseBody.height.toString()
                    wiigth.text = responseBody.weight.toString()
                    abilities.text = responseBody.abilities.joinToString { it.ability.name }
                    base_exp.text = responseBody.base_experience.toString()
                    form_list.text = responseBody.forms.joinToString { it.name }
                    game_index.text = responseBody.game_indices.joinToString { it.game_index.toString() }
                    held_itmes.text = responseBody.held_items.joinToString { it.item.name }
                    movies_list.text = responseBody.moves.joinToString { it.move.name }
                    order.text = responseBody.order.toString()
                    species.text = responseBody.species.name
                    stats.text = responseBody.stats.joinToString { it.stat.name }
                    type.text = responseBody.types.joinToString { it.type.name }
                }

            }
        })
    }
}