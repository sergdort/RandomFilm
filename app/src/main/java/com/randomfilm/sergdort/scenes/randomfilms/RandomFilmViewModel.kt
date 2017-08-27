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

        val films = filmsUseCase.randomFilms()
                .subscribeOn(Schedulers.io())
                .shareReplayLatestWhileConnected()

        val refreshed: Observable<FilmCommand> = input.refreshTrigger.asObservable()
                .startWith(Unit)
                .switchMap { _ -> films }
                .map { Refreshed(it.resultsWithPosters()) }

        val loaded: Observable<FilmCommand> = input.nextPageTrigger.asObservable()
                .take(1)
                .withLatestFrom(films)
                .switchMap {
                    FilmsPagination.paginate(filmsUseCase, it, input.nextPageTrigger.asObservable())
                            .subscribeOn(Schedulers.io())
                }
                .map { Loaded(it.resultsWithPosters()) }

        val filmResult = Observable.merge(refreshed, loaded)
                .scan(listOf<Film>(), FilmsPagination()::reduce)
                .observeOn(AndroidSchedulers.mainThread())
                .shareReplayLatestWhileConnected()

        input.selection.asObservable()
                .doOnNext(navigator::toFilmDetails)
                .mapTo(Unit)
                .takeUntilDestroyOf(lifecycle)
                .subscribe()

        output = Output(filmResult, loadingIndicator.asObservable())
    }


    class Input {
        val refreshTrigger: Relay<Unit> = PublishRelay()
        val nextPageTrigger: Relay<Unit> = PublishRelay()
        val selection: Relay<Film> = PublishRelay()
    }

    class Output(val films: Observable<List<Film>>,
                 val loading: Observable<Boolean>)

}

sealed class FilmCommand
data class Refreshed(val films: List<Film>) : FilmCommand()
data class Loaded(val films: List<Film>) : FilmCommand()

class FilmsPagination {
    companion object {
        fun paginate(useCase: FilmUseCase,
                     batch: FilmResults = FilmResults.initial(),
                     nextPageTrigger: Observable<Unit>): Observable<FilmResults> {
            val hasNextPage: (FilmResults) -> Boolean = { it -> true }
            return useCase.randomFilmsAfter(batch)
                    .paginate(nextPageTrigger, hasNextPage, {
                        FilmsPagination.paginate(useCase, it, nextPageTrigger)
                                .subscribeOn(Schedulers.io())
                    })
        }
    }

    fun reduce(films: List<Film>, command: FilmCommand): List<Film> = when (command) {
        is Refreshed -> command.films
        is Loaded -> films + command.films
    }
}
