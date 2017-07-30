package com.randofilm.sergdort.domain.Film

import io.reactivex.Observable

interface FilmUseCase {
    fun randomFilms(): Observable<FilmResults>
}
