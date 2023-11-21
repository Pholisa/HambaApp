package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog

class LoadingWelcome : AppCompatActivity() {

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_welcome)


        handler = Handler()

        handler.postDelayed({
            val intent = Intent(this, Welcome::class.java)
            startActivity(intent)
            finish()

        }, 3000)

    }



}