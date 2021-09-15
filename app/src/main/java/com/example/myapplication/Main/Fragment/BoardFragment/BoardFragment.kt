package com.example.myapplication.Main.Fragment.BoardFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.frag_board.*


// 호출시 HomeFragment.newInstance() 를 이용해서 외부에서 호출
class BoardFragment : Fragment() {
    companion object {
        const val TAG: String = "로그"
        fun newInstance(): BoardFragment {
            return BoardFragment()
        }
    }

    //private lateinit var boardlistadapter : BoardListAdapter
    private var datalist = mutableListOf<BoardDTO>()
    private var contentsUid : ArrayList<String> = arrayListOf()
    private var firestore = FirebaseFirestore.getInstance()

    private fun getBoarddata() {

        firestore.collection("Board").orderBy("timestamp")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                datalist.clear()    //   초기화
                contentsUid.clear()
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(BoardDTO::class.java)
                    datalist.add(item!!)
                    contentsUid.add(snapshot.id)
                    board_fagement_recycler_view.adapter!!.notifyDataSetChanged()
                }
            }
    }
    // 메모리에 적제 되었을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBoarddata()

    }

    override fun onResume() {
        super.onResume()

    }

    // Activity 안에 Fragment가 들어가게 되는데, onAttach가 Fragment와 Activity에 붙게됨(의존)
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기다가 view 구현하는거 정의 하면 됨 딴거 다 쓰잘때기 없음
        //val thread :Thread? = null
        board_fagement_recycler_view.apply {
            var boardlistadapter: BoardListAdapter
            //activity?.runOnUiThread() {
              //  Thread.sleep(500)
                layoutManager = LinearLayoutManager(requireContext())
                boardlistadapter = BoardListAdapter(datalist,contentsUid)
//            boardlistadapter.notifyItemRangeInserted()
                adapter = boardlistadapter
            //}
        }
    }
}