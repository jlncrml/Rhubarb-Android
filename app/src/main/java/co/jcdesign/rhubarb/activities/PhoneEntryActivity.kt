package co.jcdesign.rhubarb.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import co.jcdesign.rhubarb.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_entry.*
import java.util.concurrent.TimeUnit

private const val KEY_IS_VERIFICATION_IN_PROGRESS = "isVerificationInProgress"

class PhoneEntryActivity : AppCompatActivity() {

    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks by lazy {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) { isVerificationInProgress = false }
            override fun onVerificationFailed(p0: FirebaseException?) {
                if (p0 != null) { Toast.makeText(baseContext, p0.localizedMessage, Toast.LENGTH_LONG).show() }
                isVerificationInProgress = false
            }
            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                isVerificationInProgress = true
                val intent = Intent(baseContext, PhoneVerificationActivity::class.java)
                intent.putExtra("EXTRA_VERIFICATION_ID", p0)
                startActivity(intent)
            }
        }
    }

    private var isVerificationInProgress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_entry)
        if (isVerificationInProgress) { verifyPhoneNumber() }
        continueButton.setOnClickListener { verifyPhoneNumber() }
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

        val phoneNumber = phoneNumberEditText.text.toString()

        if (phoneNumber.isEmpty()) { return }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks
        )
    }
}
