package com.example.myapplication.Login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.Main.Activity.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_login.*
import kotlinx.android.synthetic.main.view_item_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class Add_LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    var PICK_IMAGE_FROM_ALBUM = 1
    var photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
    var storage : FirebaseStorage? = null
    var firestore: FirebaseFirestore? = null

    private var photoUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_login)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        //fireStorage 초기화
        storage = FirebaseStorage.getInstance()
        //fireStore Database
        firestore = FirebaseFirestore.getInstance()
        //firebase Auth
        auth = FirebaseAuth.getInstance()

        //갤러리 오픈
        // ACTION_GET_CONTENT : YOU CAN CHOOSE SOMETHING BASED ON MIME type
        // or ACTION_PICK

        //이미지 업로드 이벤트 처리
        upload.setOnClickListener{

            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }
        upload_sign.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //contentUpload()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        photoUri = Uri.parse("android.resource://com.example.myapplication/ic_baseline_account_circle_24")
        if(requestCode == PICK_IMAGE_FROM_ALBUM){
            photoUri = data?.data!!
            upload_image.setImageURI(photoUri)
        }else{
            finish()
        }
    }
    /*fun contentUpload(){
        //ProgressBar <<< 효과 넣을지 말지? 살짝 구시대적 ui
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_"+timeStamp+"_.png"
        // images/(imageFilename) 위치를 가리키는 참조 변수-> 를 putFile로 storage서버에 업로드
        val storageRef = storage?.reference?.child("Profiles")?.child(imageFileName)
        // storageRef?.putFile()의 반환값은 StorageTask
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener { taskSnapshot ->
            //firebase Strage 서버에 저장된 파일 다운로드 URL 가져옴
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var uid = FirebaseAuth.getInstance().currentUser!!.uid

                var UR = uri.toString()
                var NM = add_login_nickname_edit.text.toString()
                var profile_timestamp = System.currentTimeMillis()

                firestore?.collection("userid")?.document(uid)?.update(mapOf(
                    "profileUrl" to UR,
                    "nickname" to NM,
                    "profile_timestamp" to profile_timestamp
                ))
                setResult(RESULT_OK)
                finish()
            }
        }
            ?.addOnFailureListener({
                Toast.makeText(this, "업로드 실패", Toast.LENGTH_SHORT).show()
            })


    }*/
}