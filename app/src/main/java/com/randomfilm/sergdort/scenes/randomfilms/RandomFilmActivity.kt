package com.randomfilm.sergdort.scenes.randomfilms

import android.os.Bundle
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import com.randofilm.sergdort.domain.Film.Film
import com.randofilm.sergdort.randomfilm.R
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

class ListRecycleViewAdapter<T>(val clickListener: (T) -> Unit,
                                val viewHolderProvider: (ViewGroup) -> ViewHolder<T>) : RecyclerView.Adapter<ListRecycleViewAdapter.ViewHolder<T>>() {
    private var items = listOf<T>()

    fun updateItems(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> = viewHolderProvider(parent)

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) = holder.bind(items[position], clickListener)

    override fun getItemCount(): Int = items.count()

    class ViewHolder<T>(view: View, val binding: (View, T) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: T, listener: (T) -> Unit) = with(itemView) {
            binding(itemView, item)
            setOnClickListener { listener(item) }
        }
    }
}

fun ViewGroup.inflate(layoutResource: Int): View {
    return LayoutInflater.from(context).inflate(layoutResource, this, false)
}
