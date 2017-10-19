package com.randomfilm.sergdort.extensions

import android.util.Log
import com.randomfilm.sergdort.common.adapters.ListRecycleViewAdapter
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import java.util.concurrent.atomic.*

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

fun <T> Observable<T>.paginate(nextPageTrigger: Observable<Unit>,
                               hasNextPage: (T) -> Boolean,
                               nextPageFactory: (T) -> Observable<T>): Observable<T> {
    return switchMap {
        Pagination(it).paginate(nextPageTrigger, hasNextPage, nextPageFactory)
    }
}

fun <T> Observable<T>.log(): Observable<T> {
    val tag = "Observable"
    return this
            .doOnSubscribe { Log.d(tag, "Subscribed") }
            .doOnNext { Log.d(tag, "Next: ${it}") }
            .doOnError { Log.d(tag, "Error: ${it}") }
            .doOnComplete { Log.d(tag, "Completed") }
}

fun <T, U> Observable<T>.withLatestFrom(other: Observable<U>): Observable<U> {
    return this.withLatestFrom(other, io.reactivex.functions.BiFunction<T, U, U> { first, second -> second })
}

fun <T, U> Observable<T>.rx_scan(initial: U, accumulator: (U, T) -> U): Observable<U> {
    return this.scan(initial, io.reactivex.functions.BiFunction<U, T, U> { t1, t2 -> accumulator(t1, t2) })
}

fun <T, U> Observable<T>.combineLatestWith(other: Observable<U>): Observable<Pair<T, U>> {
    return Observable.combineLatest(this, other, BiFunction<T, U, Pair<T, U>> { first, second -> Pair(first, second) })
}

fun <E, T : List<E>> Observable<T>.bindTo(adapter: ListRecycleViewAdapter<E>): Disposable {
    return this.subscribe {
        adapter.updateItems(it)
    }
}

class Pagination<T>(private val page: T) {
    fun paginate(nextPageTrigger: Observable<Unit>,
                 hasNextPage: (T) -> Boolean,
                 nextPageFactory: (T) -> Observable<T>): Observable<T> {

        if (!hasNextPage(page)) {
            return Observable.just(page)
        }
        val arrayOfObservables = listOf(Observable.just(page),
                Observable.never<T>().takeUntil(nextPageTrigger),
                nextPageFactory(page))
        return Observable.concat(arrayOfObservables)
    }
}
