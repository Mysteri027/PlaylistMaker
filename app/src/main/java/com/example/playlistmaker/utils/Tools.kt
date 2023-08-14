package com.example.playlistmaker.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R

fun ImageView.setImage(uri: Uri, cornerRadius: Int) {
    Glide
        .with(this.context)
        .load(uri)
        .transform(CenterCrop(), RoundedCorners(cornerRadius))
        .into(this)
}

fun ImageView.setImage(url: String = "", cornerRadius: Int) {

    val requestOptions = RequestOptions()
        .placeholder(R.drawable.track_placeholder)
        .error(R.drawable.track_placeholder)


    Glide
        .with(this.context)
        .load(R.drawable.track_placeholder)
        .apply(requestOptions)
        .transform(CenterCrop(), RoundedCorners(cornerRadius))
        .into(this)
}