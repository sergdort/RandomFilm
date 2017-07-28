package com.randomfilm.sergdort.extensions

import android.support.v7.widget.*
import com.randomfilm.sergdort.scenes.randomfilms.*
import io.reactivex.Observable
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.takeUntilDestroyOf(lifecycleProvider: LifecycleProvider<ActivityEvent>): Observable<T> {
    return this.compose(lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY))
}

fun <E, T : List<E>> Observable<T>.bindTo(adapter: ListRecycleViewAdapter<E>): Disposable {
    return this.subscribe {
        adapter.updateItems(it)
    }
}
