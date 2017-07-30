package com.randomfilm.sergdort.scenes.filmdetails

import android.content.*
import android.os.Bundle
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.randomfilm.R
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

class FilmDetailsActivity : RxAppCompatActivity() {
    companion object {
        private val extraFilmID = "EXTRA_FILM_ID"
        fun createIntentWith(film: Film, context: Context): Intent {
            val intent = Intent(context, FilmDetailsActivity::class.java)
            intent.putExtra(FilmDetailsActivity.extraFilmID, film.id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)
    }
}
