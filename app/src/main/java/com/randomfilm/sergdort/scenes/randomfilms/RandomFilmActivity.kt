package com.randomfilm.sergdort.scenes.randomfilms

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.randomfilm.R
import com.randomfilm.sergdort.Dependencies
import com.randomfilm.sergdort.common.adapters.ListRecycleViewAdapter
import com.randomfilm.sergdort.common.relay.bindTo
import com.randomfilm.sergdort.extensions.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_random_film.*
import kotlinx.android.synthetic.main.film_cell_item.view.*

class RandomFilmActivity : RxAppCompatActivity() {

    private val viewModel: RandomFilmViewModel
        get() = Dependencies.instance
                .makeRandomFilmAssembly()
                .makeViewModelWith(this, this)

    private val listViewAdapter = ListRecycleViewAdapter<Film>({
        ListRecycleViewAdapter.ViewHolder<Film>(it.inflate(R.layout.film_cell_item), { view, film ->
            Unit
            val url = "https://image.tmdb.org/t/p/w342/${film.posterPath}"
            view.castImageView.setImageFromUrl(url)
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
        val viewModel = this.viewModel
        val input = viewModel.input
        val output = viewModel.output

        swipeRefresh.rx_refresh()
                .takeUntilDestroyOf(this)
                .bindTo(input.refreshTrigger)

        recycleView.rx_nearBottomEdge()
                .takeUntilDestroyOf(this)
                .bindTo(input.nextPageTrigger)

        listViewAdapter.selection
                .takeUntilDestroyOf(this)
                .bindTo(input.selection)

        output.loading
                .subscribe(swipeRefresh::setRefreshing)
        output.films
                .bindTo(listViewAdapter)

        recycleView.rx_nearBottomEdge().takeUntilDestroyOf(this)
                .subscribe({
                    println("!!!!!!!!!!LOAAADDDD!!!!!!!!")
                })
    }
}

