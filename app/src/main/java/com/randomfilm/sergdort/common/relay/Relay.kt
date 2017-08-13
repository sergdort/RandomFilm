package com.randomfilm.sergdort.common.relay

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

interface Relay<T> {

    fun accept(value: T)

    fun asObservable(): Observable<T>
}

class PublishRelay<T> : Relay<T> {
    private val subject = PublishSubject.create<T>()

    override fun accept(value: T) = subject.onNext(value)

    override fun asObservable(): Observable<T> = subject
}

fun <T> Observable<T>.bindTo(relay: Relay<T>): Disposable {
    return this.subscribe(relay::accept, {
        kotlin.assert(true, { "bind error to Relay" })
    })
}
