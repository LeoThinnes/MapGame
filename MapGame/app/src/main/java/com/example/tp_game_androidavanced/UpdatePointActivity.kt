package com.example.tp_game_androidavanced

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tp_game_androidavanced.model.Model_target
import com.example.tp_game_androidavanced.model.PointType
import com.example.tp_game_androidavanced.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UpdatePointActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_GAME_NAME: String = "EXTRA_GAME_NAME"
        const val EXTRA_POSITION:String = "EXTRA_POSITION"
    }
    var name = ""
    var position = 0
    private lateinit var listNewPoint : MutableList<Model_target>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_point)
        name = intent.getStringExtra(AddPointActivity.EXTRA_GAME_NAME).toString()
        position = intent.getIntExtra(EXTRA_POSITION,position)
        findPoint(this,name)

        //modification de la bar d'action
        supportActionBar?.setTitle("MODIFICATION D'UN POINT")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //récuprération des éléments de l'activité
        val buttonUpdatePoint = findViewById<Button>(R.id.btAddPoint)
        val spinner = findViewById<Spinner>(R.id.spAddType)
        val editTextPointName = findViewById<EditText>(R.id.etAddPointName)
        val editTextLatitude = findViewById<EditText>(R.id.etAddLatitude)
        val editTextLongitude = findViewById<EditText>(R.id.etAddLongitude)

        buttonUpdatePoint.text = "MODIFIER LE POINT"

        //ajout des propositions dans la liste déroulante
        spinner.adapter = ArrayAdapter<PointType>(this,android.R.layout.simple_list_item_1,PointType.values())


        //click sur le bouton ajouter
        buttonUpdatePoint.setOnClickListener {
            //vérification si les champs obligatoirs sont remplis
            if (editTextLatitude.text.toString() == "" || editTextLongitude.text.toString() == ""){
                Toast.makeText(this, "Les champs latitude et longitude doivent être remplis pour continuer", Toast.LENGTH_LONG).show()
            }else{
                //vérification des valeurs de la latitude et de la longitude car elles doivent etre entre -90.0 et 90.0
                if (editTextLatitude.text.toString().toDouble() < 90.0 && editTextLongitude.text.toString().toDouble() < 90.0
                    && editTextLatitude.text.toString().toDouble() > -90.0 && editTextLongitude.text.toString().toDouble() > -90.0) {
                    listNewPoint.removeAt(position)
                    listNewPoint.add(
                        Model_target(
                            editTextPointName.text.toString(),
                            spinner.getSelectedItem().toString(),
                            editTextLatitude.text.toString().toDouble(),
                            editTextLongitude.text.toString().toDouble()
                        )
                    )
                    updatePoint(this)
                    Toast.makeText(this, "Point modifié", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "la latitude et la longitude doit être entre -90.0 et 90.0", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun updatePoint(context: UpdatePointActivity) {

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.createGameByName(name,context.listNewPoint)
                if (response.isSuccessful) {
                    val monIntent =  Intent(this@UpdatePointActivity, ListPointsActivity::class.java)
                    monIntent.putExtra(ListPointsActivity.EXTRA_GAME_NAME, name)
                    this@UpdatePointActivity.startActivity(monIntent)
                } else {
                    Toast.makeText(
                        this@UpdatePointActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.i("TAG", response.message())
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@UpdatePointActivity,
                    "Error Occurred catch : ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun findPoint(context: UpdatePointActivity, nameMap: String){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getGameByName(nameMap)
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as MutableList<Model_target>
                    context.listNewPoint = content
                    showValues(context.listNewPoint)
                } else {
                    Toast.makeText(
                        this@UpdatePointActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@UpdatePointActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun showValues(list: MutableList<Model_target>){
        val editTextNomPoint = findViewById<EditText>(R.id.etAddPointName)
        val editTextLatitude = findViewById<EditText>(R.id.etAddLatitude)
        val editTextLongitude = findViewById<EditText>(R.id.etAddLongitude)
        editTextNomPoint.setText(list.get(position).name.toString())
        editTextLatitude.setText(list.get(position).latitude.toString())
        editTextLongitude.setText(list.get(position).longitude.toString())
    }
}