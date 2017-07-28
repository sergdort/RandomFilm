package com.randomfilm.sergdort.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.setImageFromUrl(url: String) {
    Picasso.with(context).load(url).into(this)
}
