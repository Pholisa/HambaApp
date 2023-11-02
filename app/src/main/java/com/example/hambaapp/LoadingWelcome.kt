package com.example.hambaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

class LoadingWelcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_welcome)


        Loading()

    }

    private fun Loading() {

        val builder = AlertDialog.Builder(this@LoadingWelcome)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        val welcomeIntent = Intent(this, Welcome::class.java)
        startActivity(welcomeIntent)



    }


}