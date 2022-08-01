package com.example.shopperista.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

class ImageFileExtension(private val activity: Activity, private val uri: Uri?){

    fun getFileExtension():String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!) )
    }
}