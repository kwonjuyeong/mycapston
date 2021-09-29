package com.example.myapplication.Main.Fragment.BoardFragment

import com.example.myapplication.SearchFragViewPagerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Login.LoginActivity
import com.example.myapplication.Main.Fragment.BoardFragment.repo.Repo
import com.example.myapplication.R
import com.example.myapplication.Tab1
import com.example.myapplication.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.frag_board.*

private lateinit var tabLayout: TabLayout
private lateinit var viewPager: ViewPager2


private fun getfoodlList(): ArrayList<Int> {
    return arrayListOf<Int>(R.drawable.pizza, R.drawable.coffee, R.drawable.rice)
}
private fun aaaaaaa() : ArrayList<Int>{
    return arrayListOf<Int>(R.layout.fragment_tab1,R.layout.fragment_tab2)
}

class BoardFragment : Fragment() {
    companion object {
        const val BoardTAG: String = "BoardList"

        fun newInstance(): BoardFragment {

            return BoardFragment()
        }
    }

    //private lateinit var boardlistadapter : BoardListAdapter
    private var datalist = mutableListOf<BoardDTO>()
    private var contentsUid: ArrayList<String> = arrayListOf()
    private var repo : Repo
    private var boardListViewmodel = BoardListViewmodel()
    private lateinit var boardListAdapter : BoardListAdapter


    init {
        repo = Repo.StaticFunction.getInstance()
    }


    // 메모리에 적제 되었을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("보드 프레그먼", "onCreate: ", )

    }

    override fun onDetach() {
        super.onDetach()
        Log.e("보드 프레그먼", "onDetach: ", )
    }

    override fun onPause() {
        super.onPause()
        Log.e("보드 프레그먼", "onPause: ", )
    }

    override fun onResume() {
        super.onResume()
        Log.e("보드 프레그먼", "onResume: ", )
    }
    // 뷰가 생성되었을때
    // 프레그먼트와 레이아웃을 연결시켜주는 부분



//    class BoardFragment : Fragment() {
//
//
//
//        companion object {
//            fun newInstance(): BoardFragment = BoardFragment()
//        }
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//        }
//
//        override fun onAttach(context: Context) {
//            super.onAttach(context)
//        }
//    }




        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        //  inflater 레이아웃과 frag를 연결해줌
        val view = inflater.inflate(R.layout.frag_board, container, false)
        //getBoarddata()


            tabLayout = view.findViewById(R.id.frameLayout)
            viewPager = view.findViewById(R.id.viewPager_food)


            val adapter = SearchFragViewPagerAdapter(this)
            viewPager.adapter = adapter


            val tabName = arrayOf("게시글", "최근음식")

            //슬라이드로 이동했을 때, 탭이 같이 변경되도록
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabName[position]
            }.attach()

            //탭이 선택되었을 때, 뷰페이저가 같이 변경되도록
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPager.currentItem = tab!!.position


                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기다가 view 구현하는거 정의 하면 됨 딴거 다 쓰잘때기 없음
        //val thread :Thread? = null
        //swipeRefresh()



        viewPager_food.adapter = ViewPagerAdapter(getfoodlList()) // 어댑터 생성
        viewPager_food.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로

//        tab1 = Tab1() //시험1
//        frameLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {//위아래
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                //여기에 작성
//
//                when(tab?.position) {
//                    0 -> {
//                        //Tab1
//
//                        replaceView(tab1)
//                    }
//                    1 -> {
//                        //Tab2
//                        replaceView(tab2)
//                    }
//                    2 -> {
//                        //Tab3
//                        replaceView(tab3)
//                    }
//                }
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//
//
//        })
//여기까지

        board_fagement_recycler_view.apply {
            var boardlistadapter: BoardListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            boardlistadapter = BoardListAdapter()
            adapter = boardlistadapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDestroyView() {
        super.onDestroyView()
//        boardListViewmodel.getListdata().observe (viewLifecycleOwner, Observer{
//            datalist.add(it)
//            boardListAdapter.notifyDataSetChanged()
//        })
//        boardListViewmodel.getlistuid().observe (viewLifecycleOwner, Observer{
//            contentsUid.add(it)
//            boardListAdapter.notifyDataSetChanged()
//        })
    }

    private fun swipeRefresh() {
        boradSwiprefresh.setOnRefreshListener {
            boradSwiprefresh.isRefreshing = false
            boardListAdapter!!.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBoarddata() {
        datalist.clear()
        contentsUid.clear()
        var data = repo.getboarddata()
        var uid = repo.getboardUid()
        for (i in data) {
            datalist.add(i)
            boardListAdapter.notifyDataSetChanged()
        }
        for (j in uid) {
            contentsUid.add(j)
            boardListAdapter.notifyDataSetChanged()
        }
    }
}