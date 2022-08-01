package com.example.shopperista.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopperista.R

class GlideLoader(val context : Context) {

    fun loadUserPicture(image : Any, imageView : ImageView){
        Glide
            .with(context)
            .load(image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(imageView)
    }

    fun loadProductPicture(image : Any, imageView : ImageView){
        Glide
            .with(context)
            .load(image)
            .centerCrop()
            .into(imageView)
    }

}