package co.jcdesign.rhubarb.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.jcdesign.rhubarb.models.CurrentUser
import co.jcdesign.rhubarb.R
import co.jcdesign.rhubarb.activities.EditFavsActivity
import co.jcdesign.rhubarb.activities.EditInfoActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        editInfoButton.setOnClickListener {
            val intent = Intent(context, EditInfoActivity::class.java)
            startActivity(intent)
        }
        editFavsButton.setOnClickListener {
            val intent = Intent(context, EditFavsActivity::class.java)
            startActivity(intent)
        }
        setupProfile()
    }

    private fun setupProfile() {
        nameTextView.text = CurrentUser.name
        birthdayTextView.text = CurrentUser.formattedBirthday(context!!)
        genderTextView.text = CurrentUser.gender.toString()
        aboutYouTextView.text = CurrentUser.aboutYou
        favFoodTextView.text = CurrentUser.favFood
        favMusicTextView.text = CurrentUser.favMusic
        favMovieTextView.text = CurrentUser.favMovie
        favBookTextView.text = CurrentUser.favBook
    }
}
