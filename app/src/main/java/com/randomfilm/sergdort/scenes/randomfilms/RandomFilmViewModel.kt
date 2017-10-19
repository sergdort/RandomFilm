package com.randomfilm.sergdort.scenes.randomfilms

import com.randofilm.sergdort.domain.Film.*
import com.randomfilm.sergdort.common.relay.*
import com.randomfilm.sergdort.common.rxfeedback.*
import com.randomfilm.sergdort.extensions.*
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RandomFilmViewModel {

    val input = Input()
    val output: Output

    constructor(filmsUseCase: FilmUseCase,
                navigator: IRandomFilmNavigator,
                lifecycle: LifecycleProvider<ActivityEvent>) {
        val loadNextFeedback = Feedback<State, Event> {
            it.switchMap {
                if (it.paging) {
                    Observable.empty<Event>()
                } else {
                    input.nextPageTrigger.asObservable().map { _ -> Event.Paging() }
                }
            }
        }

        val refreshFeedback = Feedback<State, Event> {
            it.switchMap {
                if (it.refreshing) {
                    Observable.empty<Event>()
                } else {
                    input.refreshTrigger.asObservable().map { _ -> Event.Refreshing() }
                }
            }
        }

        val loadingFeedback = react<State, Unit, Event>({
            it.loadingControl()
        }, {
            filmsUseCase.randomFilms()
                    .subscribeOn(Schedulers.io())
                    .catchErrorJustComplete()
                    .map { Event.Refreshed(it) }
        })

        val pagingFeedback = react<State, FilmResults, Event>({
            it.nextBatchControl()
        }, {
            filmsUseCase.randomFilmsAfter(it)
                    .subscribeOn(Schedulers.io())
                    .catchErrorJustComplete()
                    .map { Event.Loaded(it) }
        })

        val feedback = listOf(loadNextFeedback, refreshFeedback, loadingFeedback, pagingFeedback)

        val state = system<State, Event>(State.empty, Reducer()::reduce, AndroidSchedulers.mainThread(), feedback)
                .log()
                .shareReplayLatestWhileConnected()
        val filmResult = state.map {
            it.films
        }
        val loading = state.map {
            it.refreshing
        }

        output = Output(filmResult, loading)
    }


    class Input {
        val refreshTrigger: Relay<Unit> = PublishRelay()
        val nextPageTrigger: Relay<Unit> = PublishRelay()
        val selection: Relay<Film> = PublishRelay()
    }

    class Output(val films: Observable<List<Film>>,
                 val loading: Observable<Boolean>)

    data class State(val refreshing: Boolean,
                     val canRefresh: Boolean,
                     val paging: Boolean,
                     val canPageNext: Boolean,
                     val batch: FilmResults,
                     val films: List<Film>) {

        companion object {
            val empty: State
                get() = State(false, true, false, false, FilmResults.initial(), listOf())
        }

        override fun toString(): String {
            return "State(refreshing: ${refreshing}, canRefresh: ${canRefresh}, paging: ${paging}, canPageNext: ${canPageNext}, batch: ${batch.page}, films: ${films.count()})"
        }

        fun nextBatchControl(): Optional<FilmResults> {
            if (canPageNext) {
                return Optional(batch)
            }
            return Optional(null)
        }

        fun loadingControl(): Optional<Unit> {
            if (canRefresh) {
                return Optional(Unit)
            }
            return Optional(null)
        }

        fun byApplyingRefreshing(): State {
            return State(true, true, paging, canPageNext, batch, films)
        }

        fun byApplyingRefreshed(refreshed: Event.Refreshed): State {
            return State(false, false, paging, canPageNext, refreshed.filmResults, refreshed.filmResults.results)
        }

        fun byApplyingPaging(): State {
            return State(refreshing, canRefresh, true, true, batch, films)
        }

        fun byApplyingLoaded(loaded: Event.Loaded): State {
//TODO: Check batch if we can load next
            return State(refreshing, canRefresh, false, false, loaded.filmResults, films + loaded.filmResults.results)
        }
    }

    sealed class Event {
        class Refreshing : Event()
        class Paging : Event()
        data class Refreshed(val filmResults: FilmResults) : Event()
        data class Loaded(val filmResults: FilmResults) : Event()
    }

    class Reducer {
        fun reduce(state: State, event: Event): State = when (event) {
            is Event.Refreshing -> state.byApplyingRefreshing()
            is Event.Paging -> state.byApplyingPaging()
            is Event.Refreshed -> state.byApplyingRefreshed(event)
            is Event.Loaded -> state.byApplyingLoaded(event)
        }
    }
}
