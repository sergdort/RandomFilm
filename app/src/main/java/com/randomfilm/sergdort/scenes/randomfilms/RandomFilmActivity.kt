package com.randomfilm.sergdort.scenes.randomfilms

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.randomfilm.R
import com.randomfilm.sergdort.Dependencies
import com.randomfilm.sergdort.common.adapters.ListRecycleViewAdapter
import com.randomfilm.sergdort.extensions.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_random_film.*
import kotlinx.android.synthetic.main.film_cell_item.view.*

class RandomFilmActivity : RxAppCompatActivity() {

    private val viewModel: RandomFilmViewModel
        get() = Dependencies.instance
                .makeRandomFilmAssembly()
                .makeViewModelFor(this)

    private val listViewAdapter = ListRecycleViewAdapter<Film>({
        ListRecycleViewAdapter.ViewHolder<Film>(it.inflate(R.layout.film_cell_item), { view, film ->
            Unit
            val url = "https://image.tmdb.org/t/p/w500/${film.posterPath}"
            view.imageView.setImageFromUrl(url)
        })
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_film)
        configureRecycleView()
        bindViewModel()
    }

    private fun configureRecycleView() {
        recycleView.layoutManager = GridLayoutManager(this, 4)
        recycleView.adapter = listViewAdapter
        recycleView.setHasFixedSize(true)
    }

    private fun bindViewModel() {
        val input = RandomFilmViewModel.Input(swipeRefresh.rx_refresh(), listViewAdapter.selection)
        val output = viewModel.transform(input)

        output.loading
                .takeUntilDestroyOf(this)
                .subscribe(swipeRefresh::setRefreshing)
        output.films.takeUntilDestroyOf(this)
                .bindTo(listViewAdapter)
        output.filmDetailsNavigation
                .takeUntilDestroyOf(this)
                .subscribe()
    }
}

