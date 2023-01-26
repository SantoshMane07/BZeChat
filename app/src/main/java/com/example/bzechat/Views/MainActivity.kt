package com.example.bzechat.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bzechat.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent:Intent = Intent(this, LogIn::class.java)

        Handler().postDelayed({
            startActivity(intent)
            finish()
        },4000)
    }
}