package com.randofilm.sergdort.platform.UseCaseFactory
import com.randofilm.sergdort.domain.Film.FilmUseCase
import com.randofilm.sergdort.domain.FilmDetails.FilmDetailsUseCase
import com.randofilm.sergdort.platform.Networking.APIProvider

class PlatformUseCaseFactory {
    private val apiProvider = APIProvider()

    fun makeFilmsUseCase(): FilmUseCase {
        return com.randofilm.sergdort.platform.Film.FilmUseCase(apiProvider.makeDiscoverAPI())
    }

    fun makeFilmDetailsUseCase(): FilmDetailsUseCase {
        return com.randofilm.sergdort.platform.FilmDetails.FilmDetailsUseCase(apiProvider.makeFilmsAPI())
    }
}
