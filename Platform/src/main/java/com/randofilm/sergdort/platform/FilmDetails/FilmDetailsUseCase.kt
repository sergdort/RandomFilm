package com.randofilm.sergdort.platform.FilmDetails
import com.randofilm.sergdort.domain.FilmDetails.Credits
import com.randofilm.sergdort.domain.FilmDetails.FilmDetails
import com.randofilm.sergdort.domain.FilmDetails.FilmDetailsUseCase
import com.randofilm.sergdort.platform.Networking.*
import io.reactivex.Observable

internal class FilmDetailsUseCase(val filmsAPI: FilmsAPI): FilmDetailsUseCase {

    override fun detailsFor(filmID: Int): Observable<FilmDetails> = filmsAPI.detailsFor("$filmID").map { it.asDomain() }

    override fun creditsFor(filmID: Int): Observable<Credits> = filmsAPI.creditsFor("$filmID").map { it.asDomain() }
}
