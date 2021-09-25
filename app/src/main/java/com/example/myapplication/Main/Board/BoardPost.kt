package com.example.myapplication.Main.Board

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.app.ActivityCompat
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.KeyboardVisibilityUtils
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.Main.Board.Detail.BoardDetail
import com.example.myapplication.Main.Fragment.BoardFragment.BoardFragment
import com.example.myapplication.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_login.*
import com.google.android.gms.tasks.*
import kotlinx.android.synthetic.main.activity_board_post.*
import kotlinx.android.synthetic.main.activity_board_post.sv_root
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*

class BoardPost : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 1
    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    private var photoUri: Uri? = null
    private var uid: String? = null
    private var NM: String? = null
    private var profile: String? = null
    private var longitude: Double? = null
    private var latitude: Double? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_post)

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = { keyboardHeight ->
                sv_root.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })  //키보드 움직이기
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //fireStorage 초기화
        storage = FirebaseStorage.getInstance()
        //fireStore Database
        firestore = FirebaseFirestore.getInstance()
        //firebase Auth
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                latitude = location!!.latitude
                longitude = location!!.longitude
            }.addOnFailureListener {
            }


        firestore?.collection("userid")?.document(uid!!)?.get()?.addOnSuccessListener {
            if (it != null) {
                NM = it["nickname"].toString()
                profile = it["profileUrl"].toString()

            }
        }
        upload_BoardImage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
            hide_layout.visibility = View.VISIBLE
        }
        btn_write.setOnClickListener {
            boardUpload()

        }
    }


    //geoCoder 사용해 현재 위치 가져온 후 Log로 출력하는 함수.
    private fun getAddress(position: LatLng) {
        val geoCoder = Geocoder(this@BoardPost, Locale.getDefault())
        val address =
            geoCoder.getFromLocation(position.latitude, position.longitude, 1).first()
                .getAddressLine(0)
        Log.e("Address", address)
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
        val storageRef = storage?.reference?.child("Board")?.child(imageFileName)
        // promise 방식
        val boardDTO = BoardDTO()
        boardDTO.contents = board_context.text.toString()
        boardDTO.postTitle = title_text.text.toString()
        boardDTO.Writed_date = timeStamp
        boardDTO.imageWriteExplain = imgaeExplain.text.toString()
        boardDTO.ProfileUrl = profile
        // System.currentTimeMillis() : 올린 시간을 mm/sec으로 변환
        boardDTO.timestamp = System.currentTimeMillis()
        boardDTO.nickname = NM
        boardDTO.uid = auth.currentUser?.uid
        boardDTO.longitude = longitude
        boardDTO.latitude = latitude
        if (photoUri != null) {
            storageRef?.putFile(photoUri!!)
                ?.continueWithTask { task: com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> ->
                    return@continueWithTask storageRef.downloadUrl
                }?.addOnSuccessListener { uri ->
                    boardDTO.imageUrlWrite = uri.toString()

                    FirebaseFirestore.getInstance().collection("Board").document()
                        .set(boardDTO)
                    setResult(RESULT_OK)

                }
        } else {
            FirebaseFirestore.getInstance().collection("Board").document()
                .set(boardDTO)
            setResult(RESULT_OK)
        }
//        val intent = Intent(this, Main::class.java)

        //startActivity(intent)
        finish()
    }
}