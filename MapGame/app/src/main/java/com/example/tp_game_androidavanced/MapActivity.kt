package com.example.tp_game_androidavanced

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NavUtils
import com.example.tp_game_androidavanced.service.ApiClient
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MapActivity : AppCompatActivity() {

    private lateinit var mapview: MapView
    var name =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_map)

        name = intent.getStringExtra(ListPointsActivity.EXTRA_GAME_NAME).toString()

        val nomCarte = name.uppercase()

        getMarkers()
        //modification de la bar d'action
        supportActionBar?.setTitle("CARTE " + nomCarte)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //gestion de la carte
        mapview = findViewById(R.id.mapview)
        mapview.onCreate(savedInstanceState)
        // Obtention de la carte de manière asynchrone

    }

    public override fun onStart() {
        super.onStart()
        mapview.onStart()
    }
    public override fun onResume() {
        super.onResume()
        mapview.onResume()
    }
    public override fun onPause() {
        super.onPause()
        mapview.onPause()
    }
    public override fun onStop() {
        super.onStop()
        mapview.onStop()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapview.onLowMemory()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapview.onDestroy()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapview.onSaveInstanceState(outState)
    }

    //fonction recupérant la position des markers et les affichent
    private fun getMarkers() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getGameByName(name)
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    val size = response.body()?.size
                        mapview.getMapAsync { map ->
                            map.setStyle(Style.OUTDOORS) { style ->
                            }
                            for (i: Int? in 0 until size!!){
                                val latitude = content?.get(i!!)?.latitude
                                val longitude = content?.get(i!!)?.longitude
                                val type = content?.get(i!!)?.type.toString()
                                if (type !== "null"){
                                    map.addMarker(MarkerOptions().title(type).position(LatLng(latitude!!, longitude!!)))
                                }else{
                                    map.addMarker(MarkerOptions().position(LatLng(latitude!!, longitude!!)))
                                }

                            }
                    }

                } else {
                    Toast.makeText(
                        this@MapActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MapActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}