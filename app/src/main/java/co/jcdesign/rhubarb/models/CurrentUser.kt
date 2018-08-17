package co.jcdesign.rhubarb.models

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.text.format.DateFormat
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap

private const val COMPRESSION_AMOUNT = 70
private const val CONTENT_TYPE = "image/jpg"

object CurrentUser {

    val uid: String by lazy {
        FirebaseAuth.getInstance().uid!!
    }

    var photos = ArrayList<Photo>()
        private set
    var name: String? = null
        private set
    var birthday: Timestamp? = null
        private set
    var gender: Gender? = null
        private set
    var city: String? = null
        private set
    var location: GeoPoint? = null
        private set
    var aboutYou: String? = null
        private set

    var favFood: String? = null
        private set
    var favMusic: String? = null
        private set
    var favMovie: String? = null
        private set
    var favBook: String? = null
        private set

    var preferredDistance: Int? = null
        private set
    var preferredMinAge: Int? = null
        private set
    var preferredMaxAge: Int? = null
        private set
    var preferredGenders: ArrayList<Int> = ArrayList()
        private set

    var isDiscoverySettingOn: Boolean? = null
        private set
    var isMatchNofiticationsSettingOn: Boolean? = null
        private set
    var isMessageNotificationsSettingOn: Boolean? = null
        private set

    val profilePhoto
        get() = if (photos.size > 0) photos.first() else null

    val ref = {
        FirebaseFirestore.getInstance().collection("users").document(uid)
    }()

    fun getProfile() {

        ref.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

            if (firebaseFirestoreException != null) {
                TODO()
            }

            if (documentSnapshot != null) {
                set(documentSnapshot)
            }
        }
    }

    fun updateInfo(name: String?, birthday: Date?, gender: Gender?, aboutYou: String?, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {

        this.name = name
        this.birthday = Timestamp(birthday)
        this.gender = gender
        this.aboutYou = aboutYou

        val data = HashMap<String, Any>()
        data["name"] = this.name as Any
        data["birthday"] = this.birthday as Any
        data["gender"] = this.gender?.ordinal as Any
        data["about-you"] = this.aboutYou as Any

        ref.update(data)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener)
    }

    fun updateFavs(favFood: String?, favMusic: String?, favMovie: String?, favBook: String?, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {

        this.favFood = favFood
        this.favMusic = favMusic
        this.favMovie = favMovie
        this.favBook = favBook

        val data = HashMap<String, Any>()
        data["fav-food"] = this.favFood as Any
        data["fav-music"] = this.favMusic as Any
        data["fav-movie"] = this.favMovie as Any
        data["fav-book"] = this.favBook as Any

        ref.update(data)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener)
    }

    fun uploadImage(bitmap: Bitmap, onSuccessListener: OnSuccessListener<Photo>, onFailureListener: OnFailureListener) {

        val filename = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/users/$uid/$filename")

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_AMOUNT, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        val metadata = StorageMetadata.Builder()
                .setContentType(CONTENT_TYPE)
                .build()

        storageRef.putBytes(data, metadata)
                .addOnSuccessListener { taskSnapshot ->
                    val photo = Photo(uid, taskSnapshot.metadata!!.name!!, 1, 1)
                    onSuccessListener.onSuccess(photo)
                }
                .addOnFailureListener(onFailureListener)
    }

    fun formattedBirthday(context: Context): String? {
        val date = birthday?.toDate()
        val dateFormat = DateFormat.getMediumDateFormat(context)
        return dateFormat.format(date)
    }

    private fun set(document: DocumentSnapshot) {

        name = document.getString("name")
        birthday = document.getTimestamp("birthday")
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

        val photos = document.get("photos") as List<*>

        for (photo in photos) {

            val photoHashMap = photo as? HashMap<*, *>

            if (photoHashMap?.get("filename") == null) { return }

            val newPhoto = Photo(uid, photoHashMap?.get("filename") as String, 1, 1)
            this.photos.add(newPhoto)
        }

        val genderInt = document.getLong("gender")?.toInt()
        if (genderInt != null) {
            gender = Gender.values()[genderInt]
        }
    }
}