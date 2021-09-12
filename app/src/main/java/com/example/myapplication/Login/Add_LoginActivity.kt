package com.example.myapplication.Login

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.example.myapplication.DTO.Add_UserInfo
import com.example.myapplication.Main.Activity.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_login.*
import java.text.SimpleDateFormat
import java.util.*

class Add_LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    var PICK_IMAGE_FROM_ALBUM = 1
    var photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
    var storage : FirebaseStorage? = null
    var firestore: FirebaseFirestore? = null

    var photoUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_login)

        //fireStorage 초기화
        storage = FirebaseStorage.getInstance()
        //fireStore Database
        firestore = FirebaseFirestore.getInstance()
        //firebase Auth
        auth = FirebaseAuth.getInstance()

        //갤러리 오픈
        // ACTION_GET_CONTENT : YOU CAN CHOOSE SOMETHING BASED ON MIME type
        // or ACTION_PICK


//        var photoPickerInetet = Intent(Intent.ACTION_GET_CONTENT)
//        photoPickerInetet.type = "image/*"
//        startActivityForResult(photoPickerInetet,PICK_IMAGE_FROM_ALBUM)

        //이미지 업로드 이벤트 처리
        upload_image.setOnClickListener{

            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoPickerIntent.type = "image/*"
           // startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }
        upload.setOnClickListener{
            contentUpload()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_FROM_ALBUM){
            println(data?.data)
            photoUri = data?.data
            upload_image.setImageURI(photoUri)
        }else{
            finish()
        }
    }
    fun contentUpload(){
        //ProgressBar <<< 효과 넣을지 말지? 살짝 구시대적 ui
        val timeStamp = SimpleDateFormat("yyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_"+timeStamp+"_.png"
        // images/(imageFilename) 위치를 가리키는 참조 변수-> 를 putFile로 storage서버에 업로드
        val storageRef = storage?.reference?.child("images")?.child(imageFileName)
        // storageRef?.putFile()의 반환값은 StorageTask
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {

            // 확인 Toast, 올라가면 지워야함
            Toast.makeText(this,"업로드 성공",Toast.LENGTH_SHORT).show()

            //firebase Strage 서버에 저장된 파일 다운로드 URL 가져옴
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val userinfoDTO = Add_UserInfo()


                userinfoDTO.nickname = add_login_nickname_edit.toString()
                userinfoDTO.name = add_login_name_text.toString()
                userinfoDTO.uid = auth?.currentUser?.uid
                userinfoDTO.userId = auth?.currentUser?.email
                userinfoDTO.photoUrl = uri.toString()
                userinfoDTO.explain = photo_explain.text.toString()
                userinfoDTO.timestamp = System.currentTimeMillis().toString()

                firestore?.collection("image")?.document()?.set(userinfoDTO)
                setResult(RESULT_OK)
                finish()

            }
        }
            ?.addOnFailureListener({
                Toast.makeText(this, "업로드 실패", Toast.LENGTH_SHORT).show()
            })


    }
}