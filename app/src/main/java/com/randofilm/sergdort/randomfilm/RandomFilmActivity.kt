package com.randofilm.sergdort.randomfilm

import android.os.Bundle
import com.jakewharton.rxbinding2.widget.textChangeEvents
import com.randomfilm.sergdort.domain.Networking.APIProvider
import com.randomfilm.sergdort.extensions.takeUntilDestroyOf
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_random_film.*

class RandomFilmActivity : RxAppCompatActivity() {
    private val apiProvider = APIProvider()
    private val filmAPI = apiProvider.makeFilmsAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_film)

        editTextView.textChangeEvents().subscribe {
            print(it)
        }

        filmAPI.discoverFilms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntilDestroyOf(this)
                .subscribe({
                    print(it.toString())
                }, {
                    print(it.toString())
                })
    }
}
