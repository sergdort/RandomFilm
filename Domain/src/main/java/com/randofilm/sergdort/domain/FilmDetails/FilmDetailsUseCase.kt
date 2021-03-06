package com.randofilm.sergdort.domain.FilmDetails

import io.reactivex.Observable

interface FilmDetailsUseCase {
    fun detailsFor(filmID: Int): Observable<FilmDetails>

    fun creditsFor(filmID: Int): Observable<Credits>
}
