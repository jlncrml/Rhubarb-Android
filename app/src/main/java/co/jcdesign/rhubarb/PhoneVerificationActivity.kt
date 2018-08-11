package co.jcdesign.rhubarb

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_verification.*

class PhoneVerificationActivity : AppCompatActivity() {

    private val auth = FirebaseAuth(FirebaseApp.getInstance())
    private var verificationId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)
        verificationId = intent.extras.getString("EXTRA_VERIFICATION_ID")
        verifyButton.setOnClickListener { verifyPhoneNumber() }
    }

    private fun verifyPhoneNumber() {

        val verificationCode = verificationCodeEditText.text.toString()

        if (verificationCode.isEmpty()) { return }

        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, verificationCode)
        signInWithPhoneAuthCredential(phoneAuthCredential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {

                val user = task.getResult().user

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            } else {

            }
        }
    }
}
