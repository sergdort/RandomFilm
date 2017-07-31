package com.randofilm.sergdort.platform.FilmDetails
import com.randofilm.sergdort.domain.FilmDetails.FilmDetails
import com.randofilm.sergdort.domain.FilmDetails.FilmDetailsUseCase
import com.randofilm.sergdort.platform.Networking.FilmsAPI
import io.reactivex.Observable

internal class FilmDetailsUseCase(val filmsAPI: FilmsAPI): FilmDetailsUseCase {

    override fun detailsFor(filmID: Int): Observable<FilmDetails> = filmsAPI.detailsFor("$filmID").map { it.asDomain() }
}
