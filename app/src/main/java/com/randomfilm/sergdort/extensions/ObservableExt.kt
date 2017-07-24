package com.randomfilm.sergdort.extensions
import io.reactivex.Observable
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent

fun <T> Observable<T>.takeUntilDestroyOf(lifecycleProvider: LifecycleProvider<ActivityEvent>): Observable<T> {
    return this.compose(lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY))
}
