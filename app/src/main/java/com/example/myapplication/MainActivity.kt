package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.*
import kotlinx.android.synthetic.main.activity_main.*
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 로그인이 됐다면 카카오 자동 로그인(로그인 유지)
        UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
            if (error != null){
                Toast.makeText(this, "토튼 정보 보기 실패", Toast.LENGTH_SHORT).show()
            }
            else if (tokenInfo != null){
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Main_Home::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

        //회원가입 페이지
        mButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)

            // 해쉬키
            val keyHash = Utility.getKeyHash(this)
            Log.d("Hash",keyHash)

        }
        // 카카오 로그인 에러
        val callback: (OAuthToken?, Throwable?) -> Unit = {token, error ->
            if(error != null){
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 된(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() ->{
                        Toast.makeText(this,"유효하지 않은 앱",Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this,"유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this,"설정이 올바르지 않음(android key hash", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this,"앱이 요청 권한이 없음",Toast.LENGTH_SHORT).show()
                    }
                    else -> { //Unknown
                        Toast.makeText(this,"기타 에러", Toast.LENGTH_SHORT).show()
                    }
               }
            }
            else if (token != null) {
                Toast.makeText(this,"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Main_Home::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인 아니면 카카오 계정으로 로그인
        kakao_login_button.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this,callback=callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this,callback  = callback)
            }
        }
    }

}