package com.randomfilm.sergdort.scenes.randomfilms

import android.content.Context
import com.randofilm.sergdort.domain.Film.FilmUseCase
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent

class RandomFilmAssembly(val useCase: FilmUseCase) {

    fun makeViewModelWith(context: Context,
                          lifecycle: LifecycleProvider<ActivityEvent>): RandomFilmViewModel {
        return RandomFilmViewModel(useCase, RandomFilmNavigator(context), lifecycle)
    }
}
