package com.randofilm.sergdort.platform.Networking

import com.randofilm.sergdort.platform.FilmDetails.FilmDetails
import io.reactivex.Observable
import retrofit2.http.*

internal interface FilmsAPI {
    @GET("3/movie/{movie_id}")
    fun detailsFor(@Path("movie_id") filmID: String): Observable<FilmDetails>
}
