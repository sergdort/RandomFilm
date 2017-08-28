package com.randomfilm.sergdort.common.rxfeedback

import com.randomfilm.sergdort.extensions.Optional

import io.reactivex.*
import io.reactivex.subjects.ReplaySubject

// Kotlin RxFeedback implementation https://github.com/kzaher/RxFeedback

class Feedback<State, Event>(val closure: (Observable<State>) -> Observable<Event>)

fun <State, Event> system(initialState: State,
                          reduce: (State, Event) -> State,
                          scheduler: Scheduler, feedback: Iterable<Feedback<State, Event>>): Observable<State> {
    return Observable.defer {
        val replaySubject = ReplaySubject.create<State>(1)
        val events = Observable.merge(feedback.map { it.closure(replaySubject) })

        events.scan(initialState, reduce)
                .doOnNext(replaySubject::onNext)
                .doOnSubscribe { replaySubject.onNext(initialState) }
                .observeOn(scheduler)
    }
}

fun <State, Control, Event> react(query: (State) -> Optional<Control>,
                                  effect: (Control) -> Observable<Event>): Feedback<State, Event> {
    return Feedback { state ->
        state.map(query)
                .distinctUntilChanged()
                .switchMap {
                    if (it.some != null) effect(it.some) else Observable.empty()
                }
    }
}

