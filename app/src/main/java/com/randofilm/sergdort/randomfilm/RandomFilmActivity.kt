package com.randofilm.sergdort.randomfilm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.randomfilm.sergdort.domain.Networking.APIProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RandomFilmActivity : AppCompatActivity() {
    private val apiProvider = APIProvider()
    private val filmAPI = apiProvider.makeFilmsAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_film)

        filmAPI.discoverFilms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    print(it.toString())
                }, {
                    print(it.toString())
                })
    }
}
