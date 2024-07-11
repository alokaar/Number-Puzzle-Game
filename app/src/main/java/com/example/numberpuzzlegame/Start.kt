package com.example.numberpuzzlegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class Start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val firstbutton = findViewById<Button>(R.id.button1)


        firstbutton.setOnClickListener{
            val intent = Intent (this,MainActivity::class.java)
            startActivity(intent)
        }
        // Inside your StartActivity.kt

        fun openHighScoreActivity(view: View) {
            val intent = Intent(this, HighScoreActivity::class.java)
            startActivity(intent)
        }

    }
}
