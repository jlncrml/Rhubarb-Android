package co.jcdesign.rhubarb.models

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Photo(uid: String, filename: String, width: Int, height: Int) {

    var ref: StorageReference = FirebaseStorage.getInstance().getReference("users/$uid/$filename")

}