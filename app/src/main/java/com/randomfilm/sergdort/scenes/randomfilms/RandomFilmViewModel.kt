package com.randomfilm.sergdort.scenes.randomfilms

import com.randofilm.sergdort.domain.Film.*
import com.randomfilm.sergdort.common.viewmodel.ViewModel
import com.randomfilm.sergdort.extensions.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RandomFilmViewModel(val filmsUseCase: FilmUseCase) : ViewModel<RandomFilmViewModel.Input, RandomFilmViewModel.Output> {

    override fun transform(input: Input): Output {
        val loadingIndicator = LoadingIndicator()
        val films = input.refreshTrigger
                .startWith(Unit)
                .switchMap {
                    filmsUseCase.randomFilms()
                            .trackLoading(loadingIndicator)
                            .subscribeOn(Schedulers.io())
                            .catchErrorJustComplete()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.results }
                .shareReplayLatestWhileConnected()

        return Output(films, loadingIndicator.asObservable())
    }

    class Input(val refreshTrigger: Observable<Unit>) {}
    class Output(val films: Observable<List<Film>>, val loading: Observable<Boolean>) {}
}
