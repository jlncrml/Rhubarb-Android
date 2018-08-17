package co.jcdesign.rhubarb.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import co.jcdesign.rhubarb.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        getStartedButton.setOnClickListener {
            val intent = Intent(this, PhoneEntryActivity::class.java)
            startActivity(intent)
        }
    }
}
