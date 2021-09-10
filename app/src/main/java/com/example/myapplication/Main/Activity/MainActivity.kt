package com.example.myapplication.Main.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.myapplication.Main.Fragment.ChatFragment
import com.example.myapplication.Main.Fragment.HomeFragment
import com.example.myapplication.Main.Fragment.MapFragment
import com.example.myapplication.Main.Fragment.SettingFragment
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import com.kakao.sdk.common.util.Utility
import kotlinx.android.synthetic.main.frag_home.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(){
    // MainActivity가 가지고 있는 멤버 변수 선언
    private lateinit var homeFragment: HomeFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var chatFragment: ChatFragment
    private lateinit var settingFragment: SettingFragment

    //private lateinit var photoAdapter: PhotoAdapter //1
    //private var dataList = mutableListOf<DataModel>()   //2

    companion object {
        const val TAG: String = "로그"
    }

    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity - onCreate: called")
        bottomNavi.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.frame_container, homeFragment).commit()


    }

    //바텀 네비게이션 아이템 클릭 리스너
    private val onBottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        Log.d(TAG, "main-onNavigationItemSelected:  called")

        when (it.itemId) {
            R.id.action_home -> {
                homeFragment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, homeFragment).commit()
            }
            R.id.action_Map -> {
                mapFragment= MapFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, mapFragment).commit()
            }
            R.id.action_chat -> {
                chatFragment= ChatFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, chatFragment).commit()
            }
            R.id.action_setting -> {
                settingFragment = SettingFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, settingFragment).commit()
            }
        }
        true
    }

}