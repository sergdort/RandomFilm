package com.randofilm.sergdort.platform.Film

import com.randofilm.sergdort.domain.Film.FilmResults
import com.randofilm.sergdort.domain.Film.FilmUseCase
import io.reactivex.Observable

internal class FilmUseCase(val filmsAPI: FilmsAPI): FilmUseCase {
    override fun randomFilms(): Observable<FilmResults> = filmsAPI.discoverFilms().map { it.asDomain() }
}
