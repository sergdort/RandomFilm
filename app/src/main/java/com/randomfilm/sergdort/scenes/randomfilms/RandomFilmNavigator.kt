package com.randomfilm.sergdort.scenes.randomfilms

import android.content.Context
import com.randofilm.sergdort.domain.Film.Film
import com.randomfilm.sergdort.scenes.filmdetails.FilmDetailsActivity

interface IRandomFilmNavigator {
    fun toFilmDetails(film: Film)
}

class RandomFilmNavigator(val context: Context): IRandomFilmNavigator {
    override fun toFilmDetails(film: Film) {
        context.startActivity(FilmDetailsActivity.createIntentWith(film, context))
    }
}
