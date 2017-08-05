package com.randomfilm.sergdort.extensions

data class Optional<out T>(val some: T?)

fun <T> T?.wrap() = Optional(this)
