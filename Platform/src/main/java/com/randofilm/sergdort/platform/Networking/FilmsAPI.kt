package com.randofilm.sergdort.platform.Networking

import com.randofilm.sergdort.platform.FilmDetails.*
import io.reactivex.Observable
import retrofit2.http.*

internal interface FilmsAPI {
    @GET("3/movie/{movie_id}")
    fun detailsFor(@Path("movie_id") filmID: String): Observable<FilmDetails>

    @GET("3/movie/{movie_id}/credits")
    fun creditsFor(@Path("movie_id") filmID: String): Observable<Credits>
}
