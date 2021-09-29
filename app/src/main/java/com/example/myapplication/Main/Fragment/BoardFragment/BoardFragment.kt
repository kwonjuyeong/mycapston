package com.example.myapplication.Main.Fragment.BoardFragment

import android.annotation.SuppressLint
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Fragment.BoardFragment.repo.Repo
import com.example.myapplication.Main.Fragment.MapFragment.MapRepo
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.frag_board.*
import kotlinx.coroutines.launch


class BoardFragment : Fragment() {
    companion object {
        const val BoardTAG: String = "BoardList"
        fun newInstance(): BoardFragment {
            return BoardFragment()
        }
    }

    //private lateinit var boardlistadapter : BoardListAdapter
    private var repo : Repo
    private var boardListAdapter = BoardListAdapter()

    init {
        repo = Repo.StaticFunction.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_board, container, false)
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기다가 view 구현하는거 정의 하면 됨 딴거 다 쓰잘때기 없음
        //val thread :Thread? = null
        //swipeRefresh()
        board_fagement_recycler_view.apply {
            var boardlistadapter: BoardListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            boardlistadapter = BoardListAdapter()
            adapter = boardlistadapter
            boradSwiprefresh.setOnRefreshListener {
                //notices.clear() // 리스트를 한 번 비워주고
                //crawler.activateBot(page) // 리스트에 값을 넣어주고
                boardListAdapter.clear()
                boardListAdapter.notifyDataSetChanged() // 새로고침 하고
                boradSwiprefresh.isRefreshing = false // 새로고침을 완료하면 아이콘을 없앤다.
            }
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

}