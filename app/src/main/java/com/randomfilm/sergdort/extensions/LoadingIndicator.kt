package com.randomfilm.sergdort.extensions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

class LoadingIndicator {
    private val loading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun <T> trackLoadingOf(source: Observable<T>): Observable<T> {
        return source
                .doOnSubscribe {
                    loading.onNext(true)
                }
                .doOnNext {
                    loading.onNext(false)
                }
                .doFinally {
                    loading.onNext(false)
                }
    }

    fun asObservable(): Observable<Boolean> {
        return loading
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Observable<T>.trackLoading(loadingIndicator: LoadingIndicator): Observable<T> {
    return loadingIndicator.trackLoadingOf(this)
}
