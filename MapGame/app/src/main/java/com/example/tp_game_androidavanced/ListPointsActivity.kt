package com.example.tp_game_androidavanced

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.example.tp_game_androidavanced.adapter.ListPointsAdapter
import com.example.tp_game_androidavanced.model.Model_target
import com.example.tp_game_androidavanced.service.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListPointsActivity : AppCompatActivity() {

    companion object{

        const val EXTRA_GAME_NAME: String = "EXTRA_GAME_NAME"

    }
    var name =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_points)

        //modification de la bar d'action
        supportActionBar?.title = "LISTE DES POINTS"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name = intent.getStringExtra(EXTRA_GAME_NAME).toString()
        val buttonAddPoint = findViewById<FloatingActionButton>(R.id.fabAdd)
        val buttonRename = findViewById<Button>(R.id.btRename)
        val editTextRename = findViewById<EditText>(R.id.etRename)

        findGame()
        buttonAddPoint.setOnClickListener{
            val intentAdd =  Intent(this,AddPointActivity::class.java)
            intentAdd.putExtra(AddPointActivity.EXTRA_GAME_NAME, name)
            startActivity(intentAdd)
        }

        buttonRename.setOnClickListener {
            if (editTextRename.text.toString() == ""){
                Toast.makeText(this, "Il faut saisir un nom pour renomer", Toast.LENGTH_SHORT).show()
            }else{
                RenameMap()
                Toast.makeText(this, "Carte renomée", Toast.LENGTH_SHORT).show()

            }

        }

    }

    fun findGame() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getGameByName(name)
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as MutableList<Model_target>
                    newList(content)
                } else {
                    Toast.makeText(
                        this@ListPointsActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ListPointsActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun newList(points: MutableList<Model_target>){
        val validPoints: MutableList<Model_target>? = points
        if (validPoints != null) {
            val listview: ListView = findViewById(R.id.lvPoints)
            val adapter = ListPointsAdapter(this, points, name)
            listview.adapter = adapter
        }
    }

    fun RenameMap(){
        GlobalScope.launch(Dispatchers.Main) {
            val editTextRename = findViewById<EditText>(R.id.etRename)
            try {
                val response = ApiClient.apiService.getGameByName(name)
                if (response.isSuccessful && response.body() != null) {
                    var content = response.body()
                    val delete = ApiClient.apiService.deleteGameById(name)
                    if (delete.isSuccessful){
                        ApiClient.apiService.createGameByName(editTextRename.text.toString(),content)
                        editTextRename.setText("")
                    }
                } else {
                    Toast.makeText(
                        this@ListPointsActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ListPointsActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}