package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Main.Fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_change_information.*
import kotlinx.android.synthetic.main.activity_infomation_law.*

class ChangeInformationLaw : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_information)

        back_button3.setOnClickListener({
            val intent = Intent(this, SettingFragment::class.java)
            startActivity(intent)


        })
    }
}