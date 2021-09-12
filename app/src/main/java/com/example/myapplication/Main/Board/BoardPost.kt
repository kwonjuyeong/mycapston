package com.example.myapplication.Main.Board

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Board.Detail.BoardDetail
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_login.*
import com.google.android.gms.tasks.*
import kotlinx.android.synthetic.main.activity_board_post.*
import java.text.SimpleDateFormat
import java.util.*

class BoardPost : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 1
    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    private var photoUri: Uri? = null
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_post)

        //fireStorage 초기화
        storage = FirebaseStorage.getInstance()
        //fireStore Database
        firestore = FirebaseFirestore.getInstance()
        //firebase Auth
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid

        upload_BoardImage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }
        btn_write.setOnClickListener {
            boardUpload()
            startActivity(Intent(this, BoardDetail::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            photoUri = data?.data
            layout_imageUrlWrite.setImageURI(photoUri)
        } else {
            finish()
        }
    }

    fun boardUpload() {
        val timeStamp = SimpleDateFormat("yyyy.MM.dd_HH:mm").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_Board.png"
        var NM: String? =null
        var profile : String? =null
        firestore?.collection("userid")?.get()?.addOnCompleteListener{
            if(it.isSuccessful){
                for(document in it.result!!){
                    NM = document.data.getValue("nickname").toString()
                    profile = document.data.getValue("profileUrl").toString()
                    break
                }
            }
        }
        val storageRef = storage?.reference?.child("Board")?.child(imageFileName)
        // promise 방식
        storageRef?.putFile(photoUri!!)?.continueWithTask { task : com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            val boardDTO = BoardDTO()
            boardDTO.contents = board_context.text.toString()
            boardDTO.postTitle = title_text.text.toString()
            boardDTO.Writed_date = timeStamp
            boardDTO.imageWriteExplain = imgaeExplain.text.toString()
            boardDTO.imageUrlWrite = uri.toString()
            boardDTO.ProfileUrl = profile
            // System.currentTimeMillis() : 올린 시간을 mm/sec으로 변환
            boardDTO.timestamp = System.currentTimeMillis()
            boardDTO.nickname = NM
            boardDTO.uid = auth?.currentUser?.uid

            FirebaseFirestore.getInstance().collection("Board").document()
                .set(boardDTO)
            setResult(RESULT_OK)

            finish()
        }
    }
}