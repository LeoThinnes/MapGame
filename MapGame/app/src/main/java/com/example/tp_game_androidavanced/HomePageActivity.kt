package com.example.tp_game_androidavanced

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //suppression de la bar d'action
        supportActionBar?.hide()


        val homeButton = findViewById<Button>(R.id.btHome)
        val monIntent =  Intent(this,ListGamesActivity::class.java)
        homeButton.setOnClickListener{
            startActivity(monIntent)
        }
    }

}

