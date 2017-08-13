package com.randomfilm.sergdort.scenes.filmdetails

import android.content.*
import android.os.Bundle
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.randomfilm.R
import com.randomfilm.sergdort.Dependencies
import com.randomfilm.sergdort.common.relay.bindTo
import com.randomfilm.sergdort.extensions.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_film_details.*

class FilmDetailsActivity : RxAppCompatActivity() {

    private val viewModel: FilmDetailsViewModel
        get() = Dependencies.instance
                .makeFilmDetailsAssembly()
                .makeViewModelWith(FilmDetailsActivity.filmIdFrom(intent), this)

    companion object {
        private val extraFilmID = "EXTRA_FILM_ID"

        fun createIntentWith(film: Film, context: Context): Intent {
            val intent = Intent(context, FilmDetailsActivity::class.java)
            intent.putExtra(extraFilmID, film.id)
            return intent
        }

        fun filmIdFrom(intent: Intent): Int {
            return intent.getIntExtra(extraFilmID, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_details)
        bindViewModel()
    }

    private fun bindViewModel() {
        val viewModel = this.viewModel

        val output = viewModel.output
        val input = viewModel.input

//        Bind input
        swipeRefresh.rx_refresh()
                .takeUntilDestroyOf(this)
                .bindTo(input.reload)

//        Bind output

        output.detailsViewData
                .takeUntilDestroyOf(this)
                .subscribe(this::setFilmDetailsViewData)

        output.castNames
                .takeUntilDestroyOf(this)
                .subscribe(castTextView::setText)

        output.crewNames
                .takeUntilDestroyOf(this)
                .subscribe(crewTextView::setText)

        output.loading.subscribe(swipeRefresh::setRefreshing)
    }

    fun setFilmDetailsViewData(detailsViewData: FilmDetailsViewModel.DetailsViewData) {
        titleTextView.text = detailsViewData.title
        overviewTextView.text = detailsViewData.overview
        backDropImageView.setImageFromUrl(detailsViewData.backDropImageURL)
        posterImageView.setImageFromUrl(detailsViewData.posterImageURL)
        yearTextView.text = detailsViewData.year
        ratingTextView.text = detailsViewData.rating
    }
}
