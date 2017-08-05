package com.randomfilm.sergdort.scenes.randomfilms

import android.content.Context
import com.randofilm.sergdort.domain.Film.FilmUseCase

class RandomFilmAssembly(val useCase: FilmUseCase) {

    fun makeViewModelFor(context: Context): RandomFilmViewModel = RandomFilmViewModel(filmsUseCase = useCase, navigator = RandomFilmNavigator(context))
}
