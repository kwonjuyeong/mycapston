package com.example.myapplication.Main.Fragment.BoardFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Fragment.BoardFragment.repo.Repo
import com.example.myapplication.Main.Fragment.MapFragment.MapRepo
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.frag_board.*


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
    private var boardListAdapter = BoardListAdapter(datalist, contentsUid)

    init {
        repo = Repo.StaticFunction.getInstance()
    }


    // 메모리에 적제 되었을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onDetach() {
        super.onDetach()
    }

    // 뷰가 생성되었을때
    // 프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        //  inflater 레이아웃과 frag를 연결해줌
        val view = inflater.inflate(R.layout.frag_board, container, false)
        getBoarddata()



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기다가 view 구현하는거 정의 하면 됨 딴거 다 쓰잘때기 없음
        //val thread :Thread? = null
        //swipeRefresh()
        board_fagement_recycler_view.apply {
            var boardlistadapter: BoardListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            boardlistadapter = BoardListAdapter(datalist, contentsUid)
            adapter = boardlistadapter


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        boardListViewmodel.getListdata().observe (viewLifecycleOwner, Observer{
            datalist.add(it)
            boardListAdapter!!.notifyDataSetChanged()
        })
        boardListViewmodel.getlistuid().observe (viewLifecycleOwner, Observer{
            contentsUid.add(it)
            boardListAdapter!!.notifyDataSetChanged()
        })
    }

    private fun swipeRefresh() {
        boradSwiprefresh.setOnRefreshListener {
            boradSwiprefresh.isRefreshing = false
            boardListAdapter!!.notifyDataSetChanged()
        }
    }

    private fun getBoarddata() {
        datalist.clear()
        contentsUid.clear()
        var data = repo.getboarddata()
        var uid = repo.getboardUid()
        for (i in data) {
            datalist.add(i)
            boardListAdapter!!.notifyDataSetChanged()
        }
        for (j in uid) {
            contentsUid.add(j)
            boardListAdapter!!.notifyDataSetChanged()
        }
    }
}