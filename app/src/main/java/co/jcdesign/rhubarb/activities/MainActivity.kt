package co.jcdesign.rhubarb.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import co.jcdesign.rhubarb.models.CurrentUser
import co.jcdesign.rhubarb.R
import co.jcdesign.rhubarb.fragments.ChatFragment
import co.jcdesign.rhubarb.fragments.MatchFragment
import co.jcdesign.rhubarb.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_match -> {
                setFragment(matchFragment)
                supportActionBar?.title = "Rhubarb"
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat -> {
                setFragment(chatFragment)
                supportActionBar?.title = "Messages"
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                setFragment(profileFragment)
                supportActionBar?.title = "Profile"
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val matchFragment = MatchFragment()
    private val chatFragment = ChatFragment()
    private val profileFragment = ProfileFragment()

    private var settingsMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkCurrentUser()
        setContentView(R.layout.activity_main)
        setFragment(matchFragment)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        settingsMenuItem = menu?.findItem(R.id.profile_menu_settings)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item) {
            settingsMenuItem -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkCurrentUser() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            CurrentUser.getProfile()
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(frame.id, fragment)
        fragmentTransaction.commit()
    }
}
