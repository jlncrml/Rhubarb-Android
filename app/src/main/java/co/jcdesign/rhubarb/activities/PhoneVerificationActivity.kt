package co.jcdesign.rhubarb.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import co.jcdesign.rhubarb.R
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_verification.*
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SECONDS: Long = 60
private const val KEY_IS_VERIFICATION_IN_PROGRESS = "IS_VERIFICATION_IN_PROGRESS"

class PhoneVerificationActivity : AppCompatActivity() {

    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks by lazy {

        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                isVerificationInProgress = false
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                isVerificationInProgress = false
                if (p0 != null) { Toast.makeText(baseContext, p0.localizedMessage, Toast.LENGTH_LONG).show() }
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                isVerificationInProgress = true
                verificationId = p0
                resendingToken = p1
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String?) {
                super.onCodeAutoRetrievalTimeOut(p0)
                isVerificationInProgress = false
                resendCodeButton.visibility = View.VISIBLE
            }
        }
    }

    private val auth = FirebaseAuth(FirebaseApp.getInstance())

    private var isVerificationInProgress: Boolean = true
    private var phoneNumber: String = ""
    private var verificationId: String? = null
    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)
        phoneNumber = intent.extras.getString("EXTRA_PHONE_NUMBER")
        if (isVerificationInProgress) { verifyPhoneNumber() }
        resendCodeButton.setOnClickListener { resendVerificationCode()  }
        verifyButton.setOnClickListener { validateVerificationCode() }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putBoolean(KEY_IS_VERIFICATION_IN_PROGRESS, isVerificationInProgress)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            isVerificationInProgress = savedInstanceState.getBoolean(KEY_IS_VERIFICATION_IN_PROGRESS)
        }
    }

    private fun verifyPhoneNumber() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS,
                this,
                callbacks
        )
    }

    private fun resendVerificationCode() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS,
                this,
                callbacks,
                resendingToken
        )
    }

    private fun validateVerificationCode() {

        val verificationCode = verificationCodeEditText.text.toString()

        if (verificationId == null || verificationCode.isEmpty()) { return }

        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId!!, verificationCode)
        signInWithPhoneAuthCredential(phoneAuthCredential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {

                val user = task.getResult().user

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            } else {

            }
        }
    }
}
