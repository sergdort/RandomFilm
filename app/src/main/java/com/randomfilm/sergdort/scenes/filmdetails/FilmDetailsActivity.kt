package com.randomfilm.sergdort.scenes.filmdetails

import android.content.*
import android.os.Bundle
import android.support.v7.widget.*
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.domain.FilmDetails.*
import com.randofilm.sergdort.randomfilm.R
import com.randomfilm.sergdort.Dependencies
import com.randomfilm.sergdort.common.adapters.ListRecycleViewAdapter
import com.randomfilm.sergdort.common.adapters.ListRecycleViewAdapter.ViewHolder
import com.randomfilm.sergdort.common.relay.bindTo
import com.randomfilm.sergdort.extensions.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_film_details.*
import kotlinx.android.synthetic.main.cast_crew_cell_item.view.*

class FilmDetailsActivity : RxAppCompatActivity() {

    private val viewModel: FilmDetailsViewModel
        get() = Dependencies.instance
                .makeFilmDetailsAssembly()
                .makeViewModelWith(FilmDetailsActivity.filmIdFrom(intent), this)

    private val castViewAdapter = ListRecycleViewAdapter<Cast>({
        ViewHolder<Cast>(it.inflate(R.layout.cast_crew_cell_item), { view, cast ->
            Unit
            val url = "https://image.tmdb.org/t/p/w500/${cast.profilePath}"
            view.castImageView.setImageFromUrl(url)
            view.titleTextView.text = cast.name
        })
    })

    private val crewViewAdapter = ListRecycleViewAdapter<Crew>({
        ViewHolder<Crew>(it.inflate(R.layout.cast_crew_cell_item), { view, crew ->
            Unit
            val url = "https://image.tmdb.org/t/p/w500/${crew.profilePicturePath}"
            view.castImageView.setImageFromUrl(url)
            view.titleTextView.text = crew.name
        })
    })

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
        setupRecycleView()
        bindViewModel()
    }

    private fun setupRecycleView() {
        castRecycleView.adapter = castViewAdapter
        castRecycleView.setHasFixedSize(true)

        crewRecycleView.adapter = crewViewAdapter
        crewRecycleView.setHasFixedSize(true)
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
                .subscribe(this::setFilmDetailsViewData)

        output.cast.bindTo(castViewAdapter)

        output.crew.bindTo(crewViewAdapter)

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
