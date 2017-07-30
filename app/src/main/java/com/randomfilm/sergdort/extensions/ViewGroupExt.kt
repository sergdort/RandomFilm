package com.randomfilm.sergdort.extensions

import android.view.*

fun ViewGroup.inflate(layoutResource: Int): View {
    return LayoutInflater.from(context).inflate(layoutResource, this, false)
}


