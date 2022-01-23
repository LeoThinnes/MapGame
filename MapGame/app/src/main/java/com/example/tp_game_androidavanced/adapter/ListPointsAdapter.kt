package com.example.tp_game_androidavanced.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tp_game_androidavanced.ListPointsActivity
import com.example.tp_game_androidavanced.R
import com.example.tp_game_androidavanced.UpdatePointActivity
import com.example.tp_game_androidavanced.model.Model_target
import com.example.tp_game_androidavanced.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListPointsAdapter(var context: AppCompatActivity, var points:MutableList<Model_target>,var name:String): BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return points.size
    }

    override fun getItem(position: Int): Any {
        return points.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var retView = convertView

        if(convertView == null){
            viewHolder = ViewHolder()
            retView = this.inflater.inflate(R.layout.list_item_points,null)

            viewHolder.longitude = retView.findViewById(R.id.tvLongitude)
            viewHolder.latitude = retView.findViewById(R.id.tvLatitude)
            viewHolder.btDelete = retView.findViewById(R.id.btDelete)
            viewHolder.btEdit = retView.findViewById(R.id.btEdit)
            viewHolder.ivPoints = retView.findViewById(R.id.ivPoints)

            retView.setTag(viewHolder)
        }else{
            viewHolder = retView?.getTag() as (ViewHolder)
        }

        switchImage(viewHolder,position)
        viewHolder.latitude.setText("lat : "+points.get(position).latitude.toString())
        viewHolder.longitude.setText("lon : " +points.get(position).longitude.toString())
        viewHolder.btEdit.setTag(position)
        viewHolder.btDelete.setTag(position)

        viewHolder.btDelete.setOnClickListener {
            deletePoint(position)
            Toast.makeText(context, "Point supprimé", Toast.LENGTH_SHORT).show()
        }
        viewHolder.btEdit.setOnClickListener {
            val monIntent =  Intent(context, UpdatePointActivity::class.java)
            monIntent.putExtra(UpdatePointActivity.EXTRA_GAME_NAME, name)
            monIntent.putExtra(UpdatePointActivity.EXTRA_POSITION, position)
            context.startActivity(monIntent)
        }
        retView = retView as View
        return retView
    }

    class ViewHolder(){
        lateinit var latitude: TextView
        lateinit var longitude: TextView
        lateinit var btDelete: ImageButton
        lateinit var btEdit: ImageButton
        lateinit var ivPoints: ImageView
    }

    private fun deletePoint(position: Int){
        points.removeAt(position)
        deleteCarte()
        val monIntent =  Intent(context, ListPointsActivity::class.java)
        monIntent.putExtra(ListPointsActivity.EXTRA_GAME_NAME, name)
        context.startActivity(monIntent)
    }


    private fun switchImage(viewholder: ViewHolder,position: Int){
        when (points.get(position).type){
            "BONUS" -> viewholder.ivPoints.setImageResource(R.drawable.ic_boost)
            "TARGET" -> viewholder.ivPoints.setImageResource(R.drawable.ic_target)
            "CHECKPOINT" -> viewholder.ivPoints.setImageResource(R.drawable.ic_checkpoint)
            "ATTACK" -> viewholder.ivPoints.setImageResource(R.drawable.ic_sword)
        }
    }

    fun deleteCarte() {

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.createGameByName(name,points)
                if (response.isSuccessful) {
                    Log.i("reponse","ok")
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