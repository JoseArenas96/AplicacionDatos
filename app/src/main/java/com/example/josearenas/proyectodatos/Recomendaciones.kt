package com.example.josearenas.proyectodatos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_perfil_usuario.*
import kotlinx.android.synthetic.main.activity_recomendaciones.*
import kotlinx.android.synthetic.main.custom_row.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

class Recomendaciones : AppCompatActivity() {

    var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones)
        username = intent.getStringExtra("usuario")
        fetchJson(username
        )
    }

    fun fetchJson(username: String){
        val url = "https://video-game-recommendation.herokuapp.com/recommend?username="+username
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println(body)

                val jsonResponse = JSONArray(body)
                val games: JSONArray
                val gamesArray = ArrayList<String>()


                for (i in 0..(jsonResponse.length() - 1)) {
                    val item = jsonResponse.getJSONArray(i)
                    gamesArray.add(item.getString(0))
                }

                runOnUiThread {
                    listaRecomendaciones.adapter = ListAdapter(gamesArray)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("TODO EXPLOTO :,v")
            }
        })
    }

    class ListAdapter(val dataSource: ArrayList<String>) : BaseAdapter(){


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val customRow = layoutInflater.inflate(R.layout.custom_row, parent, false)
            customRow.gameName.text = dataSource.get(position)
            return customRow
        }

        override fun getItem(position: Int): Any {
            return "TEST"
        }

        override fun getItemId(position: Int): Long {
            return 1
        }

        override fun getCount(): Int {
            return dataSource.size

        }

    }
}
