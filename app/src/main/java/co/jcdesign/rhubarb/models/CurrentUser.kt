package co.jcdesign.rhubarb.models

import android.content.Context
import android.text.format.DateFormat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.util.*

object CurrentUser {

    private val uid: String by lazy {
        FirebaseAuth.getInstance().uid!!
    }

    var photos: ArrayList<String> = ArrayList()
    var name: String? = null
    var birthday: Timestamp? = null
    var gender: Int? = null
    var city: String? = null
    var location: GeoPoint? = null
    var aboutYou: String? = null

    var favFood: String? = null
    var favMusic: String? = null
    var favMovie: String? = null
    var favBook: String? = null

    var preferredDistance: Int? = null
    var preferredMinAge: Int? = null
    var preferredMaxAge: Int? = null
    var preferredGenders: ArrayList<Int> = ArrayList()

    var isDiscoverySettingOn: Boolean? = null
    var isMatchNofiticationsSettingOn: Boolean? = null
    var isMessageNotificationsSettingOn: Boolean? = null

    fun getProfile() {

        FirebaseFirestore.getInstance().collection("users").document(uid).get().addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val document = task.result

                if (document.exists()) {
                    set(document)
                }
            }
        }
    }

    fun formattedBirthday(context: Context): String? {
        val date = birthday?.toDate()
        val dateFormat = DateFormat.getMediumDateFormat(context)
        return dateFormat.format(date)
    }

    private fun set(document: DocumentSnapshot) {
        name = document.getString("name")
        birthday = document.getTimestamp("birthday")
        gender = document.getLong("gender")?.toInt()
        city = document.getString("city")
        location = document.getGeoPoint("location")
        aboutYou = document.getString("about-you")
        favFood = document.getString("fav-food")
        favMusic = document.getString("fav-music")
        favMovie = document.getString("fav-movie")
        favBook = document.getString("fav-book")
        preferredDistance = document.getLong("preferred-distance")?.toInt()
        preferredMinAge = document.getLong("preferred-min-age")?.toInt()
        preferredMaxAge = document.getLong("preferred-max-age")?.toInt()
        //preferredGenders = document.get("genders") as ArrayList<Int>
        isDiscoverySettingOn = document.getBoolean("setting-discovery")
        isMatchNofiticationsSettingOn = document.getBoolean("setting-match-notifications")
        isMessageNotificationsSettingOn = document.getBoolean("setting-message-notifications")
    }
}