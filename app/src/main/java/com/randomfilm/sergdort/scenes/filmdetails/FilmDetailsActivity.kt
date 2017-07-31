package com.randomfilm.sergdort.scenes.filmdetails

import android.content.*
import android.os.Bundle
import android.util.Log
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.randomfilm.R
import com.randomfilm.sergdort.Dependencies
import com.randomfilm.sergdort.extensions.takeUntilDestroyOf
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable

class FilmDetailsActivity : RxAppCompatActivity() {

    //    TODO: Use correct id
    private val viewModel: FilmDetailsViewModel
        get() = Dependencies.instance
                .makeFilmDetailsAssembly()
                .makeViewModelWith(321612, this)

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
        bindViewModel()
    }

    private fun bindViewModel() {
        val input = FilmDetailsViewModel.Input(Observable.empty())
        val output = viewModel.transform(input)

        output.filmDetails
                .takeUntilDestroyOf(this)
                .subscribe {
                    Log.d("ITEMS", "$it")
                }
    }
}
