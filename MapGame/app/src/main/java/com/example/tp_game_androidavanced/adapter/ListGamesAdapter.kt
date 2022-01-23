package com.example.tp_game_androidavanced.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tp_game_androidavanced.ListPointsActivity
import com.example.tp_game_androidavanced.ListGamesActivity
import com.example.tp_game_androidavanced.MapActivity
import com.example.tp_game_androidavanced.R
import com.example.tp_game_androidavanced.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListGamesAdapter(var context: AppCompatActivity, var games:MutableList<String>): BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return games.size
    }

    override fun getItem(position: Int): Any {
        return games.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var retView = convertView

        if(convertView==null){
            viewHolder = ViewHolder()
            retView = this.inflater.inflate(R.layout.list_item_game,null)

            viewHolder.title = retView.findViewById(R.id.tvTitle)
            viewHolder.btDelete = retView.findViewById(R.id.btDelete)
            viewHolder.btEdit = retView.findViewById(R.id.btEdit)
            viewHolder.ivMap = retView.findViewById(R.id.ivMap)

            retView.setTag(viewHolder)
        }else{
            viewHolder = retView?.getTag() as (ViewHolder)
        }

        viewHolder.title.setText(games.get(position))
        viewHolder.btEdit.setTag(position)
        viewHolder.btDelete.setTag(position)
        viewHolder.ivMap.setTag(position)
        viewHolder.ivMap.setOnClickListener {
            val monIntent =  Intent(context, MapActivity::class.java)
            monIntent.putExtra(ListPointsActivity.EXTRA_GAME_NAME, games.get(position))
            context.startActivity(monIntent)
        }
        viewHolder.btDelete.setOnClickListener {
            deleteGame(position)
            context.finish()
            val monIntent =  Intent(context, ListGamesActivity::class.java)
            context.startActivity(monIntent)
            Toast.makeText(context, "Carte supprimée", Toast.LENGTH_SHORT).show()
        }
        viewHolder.btEdit.setOnClickListener {
            val monIntent =  Intent(context, ListPointsActivity::class.java)
            monIntent.putExtra(ListPointsActivity.EXTRA_GAME_NAME, games.get(position))
            context.startActivity(monIntent)
        }
        retView = retView as View
        return retView
    }

    class ViewHolder(){
        lateinit var title: TextView
        lateinit var btDelete: ImageButton
        lateinit var btEdit: ImageButton
        lateinit var ivMap: ImageView
    }

     fun deleteGame(position: Int){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.deleteGameById(games.get(position))
                if (response.isSuccessful) {
                    Log.i("reponse", "ok")
                } else {
                    Toast.makeText(
                        context,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.i("TAG", response.message())
                }
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error Occurred catch : ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}