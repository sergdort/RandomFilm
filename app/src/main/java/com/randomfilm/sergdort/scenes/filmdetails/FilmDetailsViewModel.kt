package com.randomfilm.sergdort.scenes.filmdetails

import com.randofilm.sergdort.domain.FilmDetails.*
import com.randomfilm.sergdort.common.viewmodel.ViewModel
import com.randomfilm.sergdort.extensions.shareReplayLatestWhileConnected
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FilmDetailsViewModel(private val filmID: Int,
                           private val filmDetailsUseCase: FilmDetailsUseCase) : ViewModel<FilmDetailsViewModel.Input, FilmDetailsViewModel.Output> {

    override fun transform(input: Input): Output {
        val filmDetails = input.reloadTrigger
                .startWith(Unit)
                .switchMap {
                    filmDetailsUseCase.detailsFor(filmID)
                            .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .shareReplayLatestWhileConnected()

        return Output(filmDetails)
    }

    class Input(val reloadTrigger: Observable<Unit>)
    class Output(val filmDetails: Observable<FilmDetails>)
}
