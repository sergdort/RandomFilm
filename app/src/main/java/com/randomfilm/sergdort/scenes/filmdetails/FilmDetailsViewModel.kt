package com.randomfilm.sergdort.scenes.filmdetails

import com.randofilm.sergdort.domain.FilmDetails.*
import com.randomfilm.sergdort.common.viewmodel.ViewModel
import com.randomfilm.sergdort.extensions.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FilmDetailsViewModel(private val filmID: Int,
                           private val filmDetailsUseCase: FilmDetailsUseCase) : ViewModel<FilmDetailsViewModel.Input, FilmDetailsViewModel.Output> {

    override fun transform(input: Input): Output {
        val loadingIndicator = LoadingIndicator()

        val filmDetails = input.reloadTrigger
                .startWith(Unit)
                .switchMap {
                    filmDetailsUseCase.detailsFor(filmID)
                            .subscribeOn(Schedulers.io())
                            .trackLoading(loadingIndicator)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .shareReplayLatestWhileConnected()

        val title = filmDetails
                .map { it.title }
                .shareReplayLatestWhileConnected()

        val backDropImageURL = filmDetails
                .map {
                    it.backdropPath.wrap()
                }
                .skipNil()
                .map { "https://image.tmdb.org/t/p/w780/$it" }
                .shareReplayLatestWhileConnected()

        val posterImageURL = filmDetails
                .map { it.posterPath.wrap() }
                .skipNil()
                .map { "https://image.tmdb.org/t/p/w500/$it" }


        return Output(title, backDropImageURL, posterImageURL, loadingIndicator.asObservable())
    }

    class Input(val reloadTrigger: Observable<Unit>)
    class Output(val title: Observable<String>,
                 val backDropImageURL: Observable<String>,
                 val posterImageURL: Observable<String>,
                 val loading: Observable<Boolean>)
}
