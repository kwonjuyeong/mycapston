package com.example.myapplication.Main.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.api.LogDescriptor

// 호출시 HomeFragment.newInstance() 를 이용해서 외부에서 호출
class MapFragment : Fragment(){
    companion object{
        const val TAG : String ="로그"

        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance() : MapFragment{
            return MapFragment()
        }
    }


    // 메모리에 적제 되었을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"HomeFragment - onCreate() called")
    }
    // Activity 안에 Fragment가 들어가게 되는데, onAttach가 Fragment와 Activity에 붙게됨(의존)
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: onAttach called")
    }
    // 뷰가 생성되었을때
    // 프레그먼트와 레이아웃을 연결시켜주는 부분
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        //return super.onCreateView(inflater, container, savedInstanceState)
//        Log.d(TAG, "onCreateView: called")
//        //  inflater 레이아웃과 frag를 연결해줌
//        val view = inflater.inflate(R.layout.frag_map, container, false)
//
//        return  view
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기다가 view 구현하는거 정의 하면 됨 딴거 다 쓰잘때기 없음
    }
}