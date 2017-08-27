package com.randomfilm.sergdort.scenes.filmdetails

import com.randofilm.sergdort.domain.FilmDetails.*
import com.randomfilm.sergdort.common.relay.*
import com.randomfilm.sergdort.extensions.*
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class FilmDetailsViewModel {
    val input = Input()
    val output: Output

    constructor(filmID: Int,
                filmDetailsUseCase: FilmDetailsUseCase,
                lifecycleProvider: LifecycleProvider<ActivityEvent>) {

        val loadingIndicator = LoadingIndicator()
        val pair = input.reload.asObservable()
                .startWith(Unit)
                .switchMap {
                    filmDetailsUseCase.detailsFor(filmID)
                            .combineLatestWith(filmDetailsUseCase.creditsFor(filmID))
                            .subscribeOn(Schedulers.io())
                            .trackLoading(loadingIndicator)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntilDestroyOf(lifecycleProvider)
                .shareReplayLatestWhileConnected()

        val detailsViewData = pair
                .map { mapToViewData(it.first) }
                .shareReplayLatestWhileConnected()

        val castNames = pair
                .map {
                    it.second.cast
                            .filter { it.profilePath != null }
                            .distinctBy { it.id }
                }
                .shareReplayLatestWhileConnected()
        val crewNames = pair
                .map {
                    it.second.crew
                            .filter { it.profilePicturePath != null }
                            .distinctBy { it.id }
                }
                .shareReplayLatestWhileConnected()

        this.output = Output(loadingIndicator.asObservable(), detailsViewData, castNames, crewNames)
    }

    private fun mapToViewData(details: FilmDetails): DetailsViewData {
        val calendar = GregorianCalendar()
        calendar.time = details.releaseDate
        val year = "${calendar.get(Calendar.YEAR)}"

        return DetailsViewData(
                details.title,
                details.overview.wrap().defaultTo(""),
                "https://image.tmdb.org/t/p/w342/${details.posterPath}",
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

    class Input {
        val reload: Relay<Unit> = PublishRelay<Unit>()
    }

    class Output(val loading: Observable<Boolean>,
                 val detailsViewData: Observable<DetailsViewData>,
                 val cast: Observable<List<Cast>>,
                 val crew: Observable<List<Crew>>)
}
