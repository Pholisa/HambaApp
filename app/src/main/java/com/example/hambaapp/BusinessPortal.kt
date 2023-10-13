package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.hambaapp.databinding.ActivityMoreOptionsBinding

class BusinessPortal : AppCompatActivity() {



    private lateinit var viewListImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_portal)


        viewListImageView = findViewById(R.id.iv_viewList)

        //Allows user to click image view
        //Will take user to the More Info screen after clicking
        viewListImageView.setOnClickListener {
            val intent = Intent(this, ViewListing::class.java)
            startActivity(intent)
        }
    }


}