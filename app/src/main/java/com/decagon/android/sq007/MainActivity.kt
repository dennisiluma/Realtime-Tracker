package com.decagon.android.sq007

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.mapapp.ui.MapsActivity
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val btPokemonApp = findViewById<Button>(R.id.btPokemonApp)
        val btMapApp = findViewById<Button>(R.id.btMapApp)

        // Write a message to the database
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

//        myRef.setValue("Hello, Dennis!")

        //intent to move to map Activity
        btMapApp.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }





    }
}