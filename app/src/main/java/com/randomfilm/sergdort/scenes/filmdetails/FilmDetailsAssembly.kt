package com.randomfilm.sergdort.scenes.filmdetails

import android.content.Context
import com.randofilm.sergdort.domain.FilmDetails.FilmDetailsUseCase

class FilmDetailsAssembly(val useCase: FilmDetailsUseCase) {

    fun makeViewModelWith(filmID: Int, context: Context): FilmDetailsViewModel = FilmDetailsViewModel(filmID, useCase)
}
