package co.jcdesign.rhubarb.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import co.jcdesign.rhubarb.R

class EditInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
