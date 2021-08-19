package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.image.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButton.setOnClickListener({
            val intent = Intent(this, Register::class.java)
            startActivity(intent)


        })
        login_btn.setOnClickListener({
            val intent = Intent(this, MyPage::class.java)
            startActivity(intent)
        })

    }

}

