/*package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.Login.Add_LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_code_verification.*
import kotlinx.android.synthetic.main.activity_phone_auth.*
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.Main.Activity.MainActivity
import com.google.firebase.auth.AuthResult
import kotlinx.android.synthetic.main.activity_login.*


class  CodeVerificationActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null
    private var firestore: FirebaseFirestore? = null
    private var phoneNumber: String? = null
    private var userEmail: String?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)

        mAuth = FirebaseAuth.getInstance()
        userEmail = FirebaseAuth.getInstance().toString()

        val code = intent.getStringExtra("code")
        val codeInput: EditText = findViewById(R.id.codeInput)
        val verifyButton: Button = findViewById(R.id.verifyButton)
        phoneNumber = intent.getStringExtra("phoneNumber").toString()


        verifyButton.setOnClickListener{
            verifyCode(code!!, codeInput.text.toString())
        }
    }


    private fun verifyCode(authCode: String, enteredCode: String) {
        val credential = PhoneAuthProvider.getCredential(authCode, enteredCode)
        //logInSuccess(credential)
        signInWithCredentials(credential)
    }


    private fun signInWithCredentials(credentials: PhoneAuthCredential){

        mAuth!!.signInWithCredential(credentials).addOnCompleteListener{
            if(it.isSuccessful){
                //send user to profile
                firestore = FirebaseFirestore.getInstance()
                val uid = mAuth!!.currentUser!!.uid
                firestore?.collection("userid")?.document(uid)?.update(
                    mapOf(
                        "phoneNumber" to phoneNumber
                    )
                )
                setResult(RESULT_OK)
                val intent =
                    Intent(this@CodeVerificationActivity, Add_LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                //show error
                Toast.makeText(this@CodeVerificationActivity, it.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }


/*
    private fun logInSuccess(credentials: PhoneAuthCredential) {
        // val userinfoDTO = UserinfoDTO()
            mAuth?.signInWithEmailAndPassword(login_Id.text.toString(), login_Pw.text.toString())
                ?.addOnCompleteListener {{
            if(it.isSuccessful){
                //send user to profile
                firestore = FirebaseFirestore.getInstance()
                val uid = mAuth!!.currentUser!!.uid
                firestore?.collection("userid")?.document(uid)?.update(
                    mapOf(
                        "phoneNumber" to phoneNumber
                    )
                )
                setResult(RESULT_OK)
                val intent =
                    Intent(this@CodeVerificationActivity, Add_LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                //show error
                Toast.makeText(this@CodeVerificationActivity, it.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }*/

  /*  private fun signInWithCredentials(credentials: PhoneAuthCredential){

        mAuth!!.signInWithCredential(credentials).addOnCompleteListener{
            if(it.isSuccessful){
                //send user to profile
                val intent = Intent(this@CodeVerificationActivity, Add_LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                //show error
                Toast.makeText(this@CodeVerificationActivity, it.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }
   */
}



 */
