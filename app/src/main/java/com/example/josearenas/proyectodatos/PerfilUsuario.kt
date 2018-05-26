package com.example.josearenas.proyectodatos

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_perfil_usuario.*
import java.util.zip.Inflater
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.view.LayoutInflater
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_row.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList


class PerfilUsuario : AppCompatActivity() {

    var username = ""

    fun recommend(v: View){
        val intent = Intent(this, Recomendaciones::class.java)
        intent.putExtra("usuario", username)
        startActivity(intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)
        username = intent.getStringExtra("usuario")
        fetchJson(username)
    }

    fun fetchJson(username: String){
        val url = "https://video-game-recommendation.herokuapp.com/users/"+username
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println(body)

                val jsonResponse = JSONObject(body)
                val games: JSONArray
                games = jsonResponse.getJSONArray("games")
                val gamesArray = ArrayList<String>()


                for (i in 0..(games.length() - 1)) {
                    val item = games.getString(i)
                    gamesArray.add(item.toString())
                }

                runOnUiThread {
                    listaUsuario.adapter = ListAdapter(gamesArray)
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


