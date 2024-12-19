package com.example.zd3_2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso


class Result : AppCompatActivity() {
    lateinit var Title : TextView
    lateinit var Year : TextView
    lateinit var Poster : ImageView
    lateinit var Genre : TextView
    lateinit var Plot : TextView
    lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        pref = getSharedPreferences("PREF", MODE_PRIVATE)
        Title = findViewById(R.id.Title)
        Year = findViewById(R.id.Year)
        Poster = findViewById(R.id.Poster)
        Genre = findViewById(R.id.Genre)
        Plot = findViewById(R.id.Plot)

        val text = pref.getString("title", "text")
        val text1 = pref.getString("year", "text")
        val text2 = pref.getString("genre", "text")
        val text3 = pref.getString("plot", "text")
        val text4 = pref.getString("poster", "text")

        Title.text = text
        Year.text = text1
        Genre.text = text2
        Plot.text = text3

        Picasso.get()
            .load(text4)
            .placeholder(R.drawable.holder)
            .into(Poster)
    }

    fun goBack(view: View) {
        val intent = Intent(this, QuestsActivity::class.java)
        startActivity(intent)
    }
}