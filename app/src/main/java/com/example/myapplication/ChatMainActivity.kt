package com.example.myapplication

import com.example.myapplication.Main.Fragment.ChatFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.myapplication.databinding.ActivityMainBinding

class ChatMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat)
    }
}

////     ChatFragment로 프래그먼트 교체 (LoginFragment에서 호출할 예정)
//    fun replaceFragment(bundle: Bundle) {
//        val destination = ChatFragment()
//        destination.arguments = bundle      // 닉네임을 받아옴
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.layout_frame, destination)
//            .commit()
//    }
//}