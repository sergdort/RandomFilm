package com.randomfilm.sergdort.scenes.randomfilms

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.randomfilm.R
import com.randomfilm.sergdort.common.adapters.ListRecycleViewAdapter
import com.randomfilm.sergdort.domain.Networking.APIProvider
import com.randomfilm.sergdort.extensions.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_random_film.*
import kotlinx.android.synthetic.main.film_cell_item.view.*

class RandomFilmActivity : RxAppCompatActivity() {
    private val apiProvider = APIProvider()
    private val filmAPI = apiProvider.makeFilmsAPI()
    private val listViewAdapter = ListRecycleViewAdapter<Film>({
        Log.d("CLICK", "$it")
    }, {
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

        filmAPI.discoverFilms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.results }
                .takeUntilDestroyOf(this)
                .bindTo(listViewAdapter)
    }

    private fun configureRecycleView() {
        recycleView.layoutManager = GridLayoutManager(this, 4)
        recycleView.adapter = listViewAdapter
        recycleView.setHasFixedSize(true)
    }
}
