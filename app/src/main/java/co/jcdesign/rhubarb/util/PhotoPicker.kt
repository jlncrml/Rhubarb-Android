package co.jcdesign.rhubarb.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.util.*



class PhotoPicker(val activity: Activity) {

    companion object {
        const val REQUEST_TAKE_PHOTO = 0
        const val REQUEST_PICK_PHOTO = 1
    }

    private fun takePhoto() {
        val photoDeviceUri = getPhotoDeviceUri()

        if (photoDeviceUri == null) {

            Toast.makeText(activity, "Cannot access storage.", Toast.LENGTH_LONG).show()

        } else {

            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoDeviceUri)
            activity.startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO)

        }
    }

    private fun pickPhoto() {
        val pickPhotoIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickPhotoIntent.type = "image/*"
        activity.startActivityForResult(pickPhotoIntent, REQUEST_PICK_PHOTO)
    }

    private fun getPhotoDeviceUri(): Uri? {

        if (isExternalStorageAvailable()) {

            val photoStorageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val fileName = UUID.randomUUID().toString()
            val fileType = ".jpg"

            val photoFile: File

            try {
                photoFile = File.createTempFile(fileName, fileType, photoStorageDir)
                return Uri.fromFile(photoFile)
            } catch (e: IOException) {
                //Log.e(TAG, "Error creating File: " + photoStorageDir!!.absolutePath + fileName + fileType)
            }
        }

        return null
    }

    private fun isExternalStorageAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }
}