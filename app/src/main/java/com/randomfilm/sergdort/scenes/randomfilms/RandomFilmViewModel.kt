package com.randomfilm.sergdort.scenes.randomfilms

import com.randofilm.sergdort.domain.Film.*
import com.randomfilm.sergdort.common.relay.*
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
        val loadingIndicator = LoadingIndicator()

        val films = input.refreshTrigger.asObservable()
                .startWith(Unit)
                .switchMap {
                    filmsUseCase.randomFilms()
                            .trackLoading(loadingIndicator)
                            .subscribeOn(Schedulers.io())
                            .catchErrorJustComplete()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.results }
                .takeUntilDestroyOf(lifecycle)
                .shareReplayLatestWhileConnected()

        input.selection.asObservable()
                .doOnNext(navigator::toFilmDetails)
                .mapTo(Unit)
                .takeUntilDestroyOf(lifecycle)
                .subscribe()

        output = Output(films, loadingIndicator.asObservable())
    }

    class Input {
        val refreshTrigger: Relay<Unit> = PublishRelay()
        val selection: Relay<Film> = PublishRelay()
    }

    class Output(val films: Observable<List<Film>>,
                 val loading: Observable<Boolean>)
}
