package com.example.myapplication

import android.content.Intent
import android.icu.util.TimeUnit
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_phone_auth.*
import javax.xml.datatype.DatatypeConstants.SECONDS

class PhoneAuthActivity: AppCompatActivity() {

    var mAuth: FirebaseAuth ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        mAuth = FirebaseAuth.getInstance()

        val sendVerificationCodeButton: Button = findViewById(R.id.sendVerificationCodeButton)
        val phoneNumberInput: EditText = findViewById(R.id.phoneNumberInput)

       sendVerificationCodeButton.setOnClickListener {
            val phoneNumber = "+82" + phoneNumberInput.text.toString()
            sendVerificationCode(phoneNumber)
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack:PhoneAuthProvider.OnVerificationStateChangedCallbacks=
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                val intent = Intent(this@PhoneAuthActivity, CodeVerificationActivity::class.java)
                intent.putExtra("code", p0)
                startActivity(intent)
            }
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@PhoneAuthActivity,p0.message, Toast.LENGTH_LONG).show()
            }
        }

}



/*

  private lateinit var auth : FirebaseAuth
    var storage : FirebaseStorage? = null
    var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

    }


//DD3
override fun onVerificationCompleted(credential: PhoneAuthCredential) {
    // This callback will be invoked in two situations:
    // 1 - Instant verification. In some cases the phone number can be instantly
    //     verified without needing to send or enter a verification code.
    // 2 - Auto-retrieval. On some devices Google Play services can automatically
    //     detect the incoming verification SMS and perform verification without
    //     user action.
    Log.d(TAG, "onVerificationCompleted:$credential")
    signInWithPhoneAuthCredential(credential)
}

override fun onVerificationFailed(e: FirebaseException) {
    // This callback is invoked in an invalid request for verification is made,
    // for instance if the the phone number format is not valid.
    Log.w(TAG, "onVerificationFailed", e)

    if (e is FirebaseAuthInvalidCredentialsException) {
        // Invalid request
    } else if (e is FirebaseTooManyRequestsException) {
        // The SMS quota for the project has been exceeded
    }

    // Show a message and update the UI
}

override fun onCodeSent(
    verificationId: String,
    token: PhoneAuthProvider.ForceResendingToken
) {
    // The SMS verification code has been sent to the provided phone number, we
    // now need to ask the user to enter the code and then construct a credential
    // by combining the code with a verification ID.
    Log.d(TAG, "onCodeSent:$verificationId")

    // Save verification ID and resending token so we can use them later
    storedVerificationId = verificationId
    resendToken = token
}

//DD2

//DD1
val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
 */





