package com.randomfilm.sergdort.extensions

import com.randomfilm.sergdort.common.adapters.ListRecycleViewAdapter
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

fun <T> Observable<T>.takeUntilDestroyOf(lifecycleProvider: LifecycleProvider<ActivityEvent>): Observable<T> {
    return this.compose(lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY))
}

fun <T> Observable<T>.shareReplayLatestWhileConnected(): Observable<T> {
    return this.replay(1).refCount()
}

fun <T, U> Observable<T>.mapTo(value: U): Observable<U> = this.map { value }

fun <T> Observable<T>.catchErrorJustComplete(): Observable<T> = this.onErrorResumeNext(Observable.empty())

fun <T> Observable<Optional<T>>.skipNil(): Observable<T> {
    return flatMap {
        it.some.let { Observable.just(it) } ?: Observable.empty()
    }
}


fun <T, U> Observable<T>.combineLatestWith(other: Observable<U>): Observable<Pair<T, U>> {
    return Observable.combineLatest(this, other, BiFunction<T, U, Pair<T, U>>{ first, second -> Pair(first, second) })
}

fun <E, T : List<E>> Observable<T>.bindTo(adapter: ListRecycleViewAdapter<E>): Disposable {
    return this.subscribe {
        adapter.updateItems(it)
    }
}
