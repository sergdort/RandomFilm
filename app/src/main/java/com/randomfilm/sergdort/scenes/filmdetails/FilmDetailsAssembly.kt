package com.randomfilm.sergdort.scenes.filmdetails

import com.randofilm.sergdort.domain.FilmDetails.FilmDetailsUseCase

class FilmDetailsAssembly(val useCase: FilmDetailsUseCase) {

    fun makeViewModelWith(filmID: Int): FilmDetailsViewModel = FilmDetailsViewModel(filmID, useCase)
}
