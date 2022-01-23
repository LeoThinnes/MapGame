package com.example.tp_game_androidavanced

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.tp_game_androidavanced.adapter.ListGamesAdapter
import com.example.tp_game_androidavanced.model.Model_target
import com.example.tp_game_androidavanced.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_games)

        //modification de la bar d'action
        supportActionBar?.title = "LISTE DES CARTES"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findAllGame()

        val buttonAddMap = findViewById<Button>(R.id.btAddMap)
        val editTextAddMap = findViewById<EditText>(R.id.etMapName)
        buttonAddMap.setOnClickListener {
            if (editTextAddMap.text.toString() == ""){
                Toast.makeText(this, "Il faut saisir un nom pour créer la carte", Toast.LENGTH_SHORT).show()
            }else{
                addMap()
                findAllGame()
            }

        }
    }


    private fun addMap() {
        val name = findViewById<EditText>(R.id.etMapName)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val list:MutableList<Model_target>? = mutableListOf()
                val response = ApiClient.apiService.createGameByName(name.text.toString(),list )
                if (response.isSuccessful) {
                    val intentAccueil = Intent(this@ListGamesActivity, AddPointActivity::class.java)
                    intentAccueil.putExtra(AddPointActivity.EXTRA_GAME_NAME, name.text.toString())
                    startActivity(intentAccueil)
                } else {
                    Toast.makeText(
                        this@ListGamesActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.i("TAG", response.message())
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ListGamesActivity,
                    "Error Occurred catch : ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun findAllGame() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getGameList()
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as MutableList<String>
                    newList(content)
                } else {
                    Toast.makeText(
                        this@ListGamesActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ListGamesActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun newList(games: MutableList<String>){
        val gameList: MutableList<String>? = games
        if (gameList != null) {
            val listview: ListView = findViewById(R.id.lvGame)
            val adapter = ListGamesAdapter(this, games)
            listview.adapter = adapter
        }

     }


}