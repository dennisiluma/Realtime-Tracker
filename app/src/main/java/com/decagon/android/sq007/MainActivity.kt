package com.decagon.android.sq007

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.mapapp.ui.MapsActivity
import com.decagon.android.sq007.pokemonapp.ui.PokemonActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //call map and pokemon id from xml
        val btMapApp = findViewById<Button>(R.id.btMapApp)
        val btPokemonApp = findViewById<Button>(R.id.btPokemonApp)


        //intent to move to map Activity
        btMapApp.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
        //intent to move to pokemon Activity
        btPokemonApp.setOnClickListener{
            startActivity(Intent(this, PokemonActivity::class.java))
        }





    }
}