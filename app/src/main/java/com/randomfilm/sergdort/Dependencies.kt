package com.randomfilm.sergdort

import com.randofilm.sergdort.platform.UseCaseFactory.PlatformUseCaseFactory
import com.randomfilm.sergdort.scenes.randomfilms.RandomFilmAssembly

class Dependencies private constructor(){
    companion object {
        val instance = Dependencies()
    }

    private val useCaseFactory = PlatformUseCaseFactory()

    fun makeRandomFilmAssembly(): RandomFilmAssembly = RandomFilmAssembly(useCaseFactory.makeFilmsUseCase())
}
