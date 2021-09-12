package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Login.LoginActivity

class Intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        val thread = Thread(Runnable {
            Thread.sleep(1000)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }).start()
    }
}