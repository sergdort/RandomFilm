package com.randomfilm.sergdort.extensions

import android.support.v4.widget.SwipeRefreshLayout
import io.reactivex.*
import io.reactivex.disposables.Disposable

fun SwipeRefreshLayout.rx_refresh(): Observable<Unit> {
    val refresh = Observable.create<Unit> { subscriber ->
        this.setOnRefreshListener {
            subscriber.onNext(Unit)
        }
    }
    return refresh.share()
}
