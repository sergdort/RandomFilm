package com.randofilm.sergdort.platform.Film

import com.randofilm.sergdort.domain.Film.FilmResults
import com.randofilm.sergdort.domain.Film.FilmUseCase
import com.randofilm.sergdort.platform.Networking.DiscoverAPI
import io.reactivex.Observable

internal class FilmUseCase(val discoverAPI: DiscoverAPI): FilmUseCase {
    override fun randomFilms(): Observable<FilmResults> = discoverAPI.discoverFilms().map { it.asDomain() }

    override fun randomFilmsAfter(results: FilmResults): Observable<FilmResults> {
        val queryMap = mapOf(Pair("page", "${results.page + 1}"))
        return discoverAPI.discoverFilms(queryMap).map { it.asDomain() }
    }
}
