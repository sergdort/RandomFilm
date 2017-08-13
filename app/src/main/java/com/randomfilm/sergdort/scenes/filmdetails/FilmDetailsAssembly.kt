package com.randomfilm.sergdort.scenes.filmdetails

import com.randofilm.sergdort.domain.FilmDetails.FilmDetailsUseCase
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent

class FilmDetailsAssembly(val useCase: FilmDetailsUseCase) {

    fun makeViewModelWith(filmID: Int, lifecycle: LifecycleProvider<ActivityEvent>): FilmDetailsViewModel = FilmDetailsViewModel(filmID, useCase, lifecycle)
}
