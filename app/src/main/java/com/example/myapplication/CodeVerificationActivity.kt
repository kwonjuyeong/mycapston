package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.Main.Activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_code_verification.*

class CodeVerificationActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)

        mAuth = FirebaseAuth.getInstance()

        val code = intent.getStringExtra("code")

        val codeInput: EditText = findViewById(R.id.codeInput)
        val verifyButton: Button = findViewById(R.id.auth_btn)

        verifyButton.setOnClickListener{
            verifyCode(code!!, codeInput.text.toString())
        }
    }

    private fun verifyCode(authCode: String, enteredCode: String){
        val credential = PhoneAuthProvider.getCredential(authCode, enteredCode)
        signInWithCredentials(credential)
    }

    private fun signInWithCredentials(credentials: PhoneAuthCredential){
        mAuth!!.signInWithCredential(credentials).addOnCompleteListener{
            if(it.isSuccessful){
                //send user to profile
                val intent = Intent(this@CodeVerificationActivity, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                //show error
                Toast.makeText(this@CodeVerificationActivity, it.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}