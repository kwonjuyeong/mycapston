package com.example.myapplication.Main.Fragment.HomeFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.Main.Board.BoardPost
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R
import kotlinx.android.synthetic.main.frag_home.*
import kotlinx.android.synthetic.main.frag_home.view.*




// 호출시 HomeFragment.newInstance() 를 이용해서 외부에서 호출
class HomeFragment : Fragment() {
    companion object {
        const val TAG: String = "로그"
        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val repo = Repo.StaticFunction.getInstance()
    private lateinit var photoAdapter: PhotoAdapter
    private var dataList = mutableListOf<DataModel>()

    // 메모리에 적제 되었을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    // Activity 안에 Fragment가 들어가게 되는데, onAttach가 Fragment와 Activity에 붙게됨(의존)
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    // 뷰가 생성되었을때
    // 프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_home, container, false)
        //  inflater 레이아웃과 frag를 연결해줌

        view.btn_board.setOnClickListener {
            GoBorad()
        }


        return view
    }
    fun GoBorad(){
        var intent = Intent(requireActivity(), BoardPost::class.java)
        startActivity(intent)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun startActivity(activity: Activity) {
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)//위 아래 애니메이션코드

        }




        recyclerView.layoutManager = GridLayoutManager( requireContext(), 4)
        photoAdapter = PhotoAdapter( requireContext())
        recyclerView.adapter = photoAdapter

        dataList.add(DataModel("치킨", R.drawable.chiken))
        dataList.add(DataModel("피자", R.drawable.pizza))
        dataList.add(DataModel("중식", R.drawable.jjajang))
        dataList.add(DataModel("한식", R.drawable.rice))
        dataList.add(DataModel("양식", R.drawable.steak))
        dataList.add(DataModel("커피", R.drawable.coffee))
        dataList.add(DataModel("돈까스", R.drawable.gas))
        dataList.add(DataModel("일식", R.drawable.sushi))

        photoAdapter.setDataList(dataList)

    }
    override fun onPause() {
        super.onPause()
        repo.upDateOnlineState("offline")

    }

    override fun onResume() {
        super.onResume()
        repo.upDateOnlineState("online")

    }
}