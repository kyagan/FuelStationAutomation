package com.kaanyagan.petrolofisi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener{
            val intent = Intent(this,BuyActivity::class.java)
            startActivity(intent)
        }

    }
}