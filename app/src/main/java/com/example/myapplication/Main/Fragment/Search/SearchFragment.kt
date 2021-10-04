package com.example.myapplication.Main.Fragment.Search
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Activity.MainActivity
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_now_my_place.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.search_item.view.*
import kotlinx.android.synthetic.main.view_item_layout.view.*

class SearchFragment :AppCompatActivity() {

    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()

        search_recyclerview.adapter = RecyclerViewAdapter()
        search_recyclerview.layoutManager = LinearLayoutManager(this)

        var searchOption = "search_title"

        // 스피너 옵션에 따른 동작
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (spinner.getItemAtPosition(position)) {
                    "제목" -> {
                        searchOption = "postTitle"
                    }
                    "내용" -> {
                        searchOption = "contents"
                    }
                    "닉네임" -> {
                        searchOption = "nickname"
                    }
                }
            }
        }

        // 검색 옵션에 따라 검색
        searchBtn.setOnClickListener {
            (search_recyclerview.adapter as RecyclerViewAdapter).search(searchWord.text.toString(), searchOption)
        }


        search_cancel.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Post 클래스 ArrayList 생성성
        var Post : ArrayList<BoardDTO> = arrayListOf()

        init {  // BoardDTO의 문서를 불러온 뒤 Post로 변환해 ArrayList에 담음
            firestore?.collection("Board")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                Post.clear()

                for (snapshot in querySnapshot!!.documents) {
                    val item = snapshot.toObject(BoardDTO::class.java)
                    Post.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
            return ViewHolder(view)
        }
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            viewHolder.search_title.text = Post[position].postTitle
            viewHolder.search_contents.text = Post[position].contents
            viewHolder.search_nickname.text = Post[position].nickname
            viewHolder.search_date.text = Post[position].Writed_date
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return Post.size
        }
        fun search(searchWord : String, option : String) {
            firestore?.collection("Board")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                Post.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.getString(option)!!.contains(searchWord)) {
                        val item = snapshot.toObject(BoardDTO::class.java)
                        Post.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        }
    }
}



