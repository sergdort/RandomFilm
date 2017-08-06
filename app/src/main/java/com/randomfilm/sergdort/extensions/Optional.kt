package com.randomfilm.sergdort.extensions

data class Optional<T>(val some: T?) {
    fun defaultTo(default: T): T = some.let { it } ?: default
}

fun <T> T?.wrap() = Optional(this)
