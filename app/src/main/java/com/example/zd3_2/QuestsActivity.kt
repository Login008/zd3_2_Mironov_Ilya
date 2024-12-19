package com.example.zd3_2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.json.JSONObject

class QuestsActivity : AppCompatActivity() {

    lateinit var stringArray : Set<String>

    lateinit var gridLayout: GridLayout
    lateinit var view : View
    lateinit var edit : EditText
    lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quests)
        gridLayout = findViewById(R.id.gridLayout)
        view = findViewById(R.id.questsView)
        edit = findViewById(R.id.editSearch)
        pref = getSharedPreferences("PREF", MODE_PRIVATE)
        stringArray = pref.getStringSet("Array", setOf())!!

        val itemWidth = 1800 / 3 // Width for 3 columns

        var rows: Int = 0
        var columns: Int = 0

        if (stringArray != null) {
            for (item in stringArray)
            {
                var url = "https://www.omdbapi.com/?apikey=8424b5c9&t=" + item

                val queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(
                    com.android.volley.Request.Method.GET,
                    url,
                    {
                            responce ->
                        val obj = JSONObject(responce)

                        var poster = obj.getString("Poster")
                        var title = obj.getString("Title")
                        var year = obj.getString("Year")

                        val linearLayout = LinearLayout(this@QuestsActivity).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = GridLayout.LayoutParams().apply {
                                rowSpec = GridLayout.spec(rows)
                                columnSpec = GridLayout.spec(columns)
                            }
                        }

                        // Создаём и задаем TextView для названия и года
                        val titleTextView = TextView(this@QuestsActivity).apply {
                            text = title
                            textSize = 20F
                            layoutParams = LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT)
                            gravity = Gravity.CENTER
                        }
                        titleTextView.setTextColor(getResources().getColor(R.color.white))

                        val yearTextView = TextView(this@QuestsActivity).apply {
                            text = year
                            textSize = 20F
                            layoutParams = LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT)
                            gravity = Gravity.CENTER
                        }
                        yearTextView.setTextColor(getResources().getColor(R.color.white))

                        linearLayout.addView(titleTextView)
                        linearLayout.addView(yearTextView)

                        // Создаем ImageView для постера
                        val imageView = ImageView(this@QuestsActivity).apply {
                            var params = LinearLayout.LayoutParams(itemWidth, LayoutParams.MATCH_PARENT)
                            params.setMargins(20, 10, 20, 10)
                            layoutParams = params
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            Picasso.get()
                                .load(poster)
                                .placeholder(R.drawable.holder)
                                .into(this)
                            adjustViewBounds = true
                        }


                        linearLayout.addView(imageView)

                        gridLayout.addView(linearLayout)

                        columns += 1
                        if (columns == 3) {
                            rows += 1
                            columns = 0
                        }
                    },
                    {
                        Snackbar.make(view, "Не удалось выполнить запрос", Snackbar.LENGTH_LONG).show()
                    }
                )
                queue.add(stringRequest)
            }
        }
    }

    fun Search(view: View) {
        if (edit.text.toString().isNotEmpty())
        {
            if (stringArray != null) {
                for (item in stringArray) {
                    if (item.toLowerCase().contains(edit.text.toString().toLowerCase())) {
                        var url = "https://www.omdbapi.com/?apikey=8424b5c9&t=" + item

                        val queue = Volley.newRequestQueue(this)
                        val stringRequest = StringRequest(
                            com.android.volley.Request.Method.GET,
                            url,
                            { responce ->
                                val obj = JSONObject(responce)

                                var poster = obj.getString("Poster")
                                var title = obj.getString("Title")
                                var year = obj.getString("Year")
                                var genre = obj.getString("Genre")
                                var plot = obj.getString("Plot")

                                pref.edit().putString("poster", poster).apply()
                                pref.edit().putString("title", title).apply()
                                pref.edit().putString("year", year).apply()
                                pref.edit().putString("genre", genre).apply()
                                pref.edit().putString("plot", plot).apply()
                            },
                            {
                                Snackbar.make(view, "Не удалось выполнить запрос", Snackbar.LENGTH_LONG)
                                    .show()
                            }
                        )
                        queue.add(stringRequest)

                        val intent = Intent(this, Result::class.java)
                        startActivity(intent)
                        return
                    }
                }
            }
            val alert = AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Такой фильм не был найден")
                .setPositiveButton("Ok", null)
                .create()
                .show()

        }
        else
        {
            val alert = AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Заполните пустое поле")
                .setPositiveButton("Ok", null)
                .create()
                .show()
        }
    }

    fun goBack(view: View) {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }
}