package com.example.myapplication.Main.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.*
import com.example.myapplication.Login.LoginActivity
import kotlinx.android.synthetic.main.frag_setting.*

//import com.google.api.LogDescriptor

// 호출시 HomeFragment.newInstance() 를 이용해서 외부에서 호출
class SettingFragment : Fragment() {
    companion object {
        const val TAG: String = "로그"

        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }
    private fun getfoodlList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.steak, R.drawable.coffee, R.drawable.sushi)
    }
    private lateinit var viewPager2: ViewPager2


    // 프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "SettingFragment - onCreateView: called")
        //  inflater 레이아웃과 frag를 연결해줌
        val view = inflater.inflate(R.layout.frag_setting, container, false)
        viewPager2 = view.findViewById(R.id.viewPager_food)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2.adapter = ViewPagerAdapter(this, getfoodlList()) // 어댑터 생성
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로

        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.horizon_enter, R.anim.none)//위아래 애니메이션(62~63)


    }


    // 여기다가 view 구현하는거 정의 하면 됨 딴거 다 쓰잘때기 없음

}