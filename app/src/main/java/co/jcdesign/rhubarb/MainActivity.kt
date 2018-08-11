package co.jcdesign.rhubarb

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_match-> {
                setFragment(matchFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat -> {
                setFragment(chatFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                setFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val matchFragment = MatchFragment()
    private val chatFragment = ChatFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFragment(matchFragment)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(frame.id, fragment)
        fragmentTransaction.commit()
    }
}
