package com.example.myapplication.Main.Board

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import bolts.Task
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_login.*
import kotlinx.android.synthetic.main.activity_board_test.*
import com.google.android.gms.tasks.*
import java.text.SimpleDateFormat
import java.util.*

class BoardTest : AppCompatActivity() {
    var boardUid : String? = null
    var boardNickName : String? = null

    var PICK_IMAGE_FROM_ALBUM = 1
    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    private var photoUri: Uri? = null
    private var ET: String? = null
    private var uid: String? = null
    var boardDTOs : ArrayList<BoardDTO> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_test)
        // uid 초기화(접속한 계정의 uid)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        //fireStorage 초기화
        storage = FirebaseStorage.getInstance()
        //fireStore Database
        firestore = FirebaseFirestore.getInstance()
        //firebase Auth
        auth = FirebaseAuth.getInstance()
        firestore?.collection("userid")?.document(uid!!)?.get()
        /*
        val docRef = firestore?.collection("userid")?.document(uid.toString())
        firestore?.collection(uid!!)?.orderBy("timestamp", Query.Direction.DESCENDING)
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                UserinfoDTO.clear()
*/
        upload_image.setOnClickListener {

            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }
        btn_write.setOnClickListener {
            boardUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            photoUri = data?.data
            upload_BoardImage.setImageURI(photoUri)
        } else {
            finish()
        }
    }

    fun boardUpload() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_Board.png"
        ET = imgaeExplain.text.toString()
        val storageRef = storage?.reference?.child("Board")?.child(imageFileName)
        // promise 방식
        storageRef?.putFile(photoUri!!)?.continueWithTask { task : com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            val boardDTO = BoardDTO()

            boardDTO.contents = board_context.text.toString()
            boardDTO.postTitle = title_text.text.toString()
            boardDTO.Writed_date = timeStamp
            boardDTO.imageUrlWrite = uri.toString()
            boardDTO.imageWriteExplain = ET
            // System.currentTimeMillis() : 올린 시간을 mm/sec으로 변환
            boardDTO.timestamp = System.currentTimeMillis()
            //boardDTO.nickname =
            //boardDTO.uid = auth?.currentUser?.uid
            boardDTO.userEmail = auth?.currentUser?.email
            //nickName = firestore.collection("")
        }


    }
}