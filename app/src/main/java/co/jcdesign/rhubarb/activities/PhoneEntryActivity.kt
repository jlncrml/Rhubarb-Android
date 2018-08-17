package co.jcdesign.rhubarb.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import co.jcdesign.rhubarb.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_entry.*
import java.util.concurrent.TimeUnit

class PhoneEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_entry)
        //phoneNumberEditText.setOnKeyListener { v, keyCode, event ->
            //continueButton.isEnabled = phoneNumberEditText.text.length >= 10
            //return@setOnKeyListener true
        //}
        continueButton.setOnClickListener { continueToVerification() }
    }

    private fun continueToVerification() {

        val phoneNumber = phoneNumberEditText.text.toString()

        if (phoneNumber.isEmpty()) { return }

        val intent = Intent(this, PhoneVerificationActivity::class.java)
        intent.putExtra("EXTRA_PHONE_NUMBER", phoneNumber)
        startActivity(intent)
    }
}
