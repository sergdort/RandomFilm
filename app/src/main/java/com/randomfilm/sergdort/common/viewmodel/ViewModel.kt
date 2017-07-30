package com.randomfilm.sergdort.common.viewmodel

interface ViewModel<I, O> {

    fun transform(input: I): O
}
