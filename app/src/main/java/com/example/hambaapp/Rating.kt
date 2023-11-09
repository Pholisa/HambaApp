package com.example.hambaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class Rating : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val builder = AlertDialog.Builder(this )

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val rateButton = findViewById<Button>(R.id.btnRateNow)
        val ratingText = findViewById<TextView>(R.id.ratingText)
        val ratingComment = findViewById<EditText>(R.id.ratingComment)

        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingText.text = rating.toString()
            when (ratingBar.rating.toInt()){
                1 -> ratingText.text = "Very Bad"
                2 -> ratingText.text = "Bad"
                3 -> ratingText.text = "Good"
                4 -> ratingText.text = "Great"
                5 -> ratingText.text = "Amazing"
                else -> ratingText.text = " "
            }
        }

        rateButton.setOnClickListener {

        }
    }
}