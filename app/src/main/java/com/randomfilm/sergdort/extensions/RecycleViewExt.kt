package com.randomfilm.sergdort.extensions

import android.support.v7.widget.RecyclerView
import io.reactivex.Observable

fun RecyclerView.rx_nearBottomEdge(): Observable<Unit> {
    return Observable.create<Unit> { emitter ->
        val nearBottomEdgeAdapter = NearBottomEdgeAdapter(44, {
            emitter.onNext(Unit)
        })
        this.addOnScrollListener(nearBottomEdgeAdapter)

        emitter.setCancellable {
            this.removeOnScrollListener(nearBottomEdgeAdapter)
        }
    }
}

class NearBottomEdgeAdapter(private val triggerLimit: Int,
                            private val emitter: (Unit) -> Unit)
    : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val verticalScrollRange = recyclerView.computeVerticalScrollRange()
        val verticalScrollOffset = recyclerView.computeVerticalScrollOffset()
        val height = recyclerView.height
        if (verticalScrollRange - (height + verticalScrollOffset) < triggerLimit) {
            emitter(Unit)
        }
    }
}
