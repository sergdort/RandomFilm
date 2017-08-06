package com.randomfilm.sergdort.scenes.filmdetails

import com.randofilm.sergdort.domain.FilmDetails.*
import com.randomfilm.sergdort.common.viewmodel.ViewModel
import com.randomfilm.sergdort.extensions.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class FilmDetailsViewModel(private val filmID: Int,
                           private val filmDetailsUseCase: FilmDetailsUseCase) : ViewModel<FilmDetailsViewModel.Input, FilmDetailsViewModel.Output> {

    override fun transform(input: Input): Output {
        val loadingIndicator = LoadingIndicator()

        val pair = input.reloadTrigger
                .startWith(Unit)
                .switchMap {
                    filmDetailsUseCase.detailsFor(filmID)
                            .combineLatestWith(filmDetailsUseCase.creditsFor(filmID))
                            .subscribeOn(Schedulers.io())
                            .trackLoading(loadingIndicator)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .shareReplayLatestWhileConnected()

        val detailsViewData = pair
                .map { mapToViewData(it.first) }
                .shareReplayLatestWhileConnected()


        val castNames = pair
                .map {
                    it.second.cast.take(10).map { it.name }.joinToString(", ")
                }
                .shareReplayLatestWhileConnected()
        val crewNames = pair.map { it.second.crew.take(10).map { it.name }.joinToString(", ") }
                .shareReplayLatestWhileConnected()

        return Output(loadingIndicator.asObservable(), detailsViewData, castNames, crewNames)
    }

    private fun mapToViewData(details: FilmDetails): DetailsViewData {
        val calendar = GregorianCalendar()
        calendar.time = details.releaseDate
        val year = "${calendar.get(Calendar.YEAR)}"

        return DetailsViewData(
                details.title,
                details.overview.wrap().defaultTo(""),
                "https://image.tmdb.org/t/p/w500/${details.posterPath}",
                "https://image.tmdb.org/t/p/w780/${details.backdropPath}",
                year,
                "${details.voteAverage}")
    }

    data class DetailsViewData(
            val title: String,
            val overview: String,
            val posterImageURL: String,
            val backDropImageURL: String,
            val year: String,
            val rating: String)

    class Input(val reloadTrigger: Observable<Unit>)
    class Output(val loading: Observable<Boolean>,
                 val detailsViewData: Observable<DetailsViewData>,
                 val castNames: Observable<String>,
                 val crewNames: Observable<String>)
}
