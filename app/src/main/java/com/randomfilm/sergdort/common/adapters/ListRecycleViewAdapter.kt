package com.randomfilm.sergdort.common.adapters

import android.support.v7.widget.RecyclerView
import android.view.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ListRecycleViewAdapter<T>(val viewHolderProvider: (ViewGroup) -> ViewHolder<T>) : RecyclerView.Adapter<ListRecycleViewAdapter.ViewHolder<T>>() {
    val selection: Observable<T> get() = selectionSubject
    private var items = listOf<T>()
    private val selectionSubject = PublishSubject.create<T>()


    fun updateItems(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> = viewHolderProvider(parent)

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) = holder.bind(items[position], selectionSubject::onNext)

    override fun getItemCount(): Int = items.count()

    class ViewHolder<T>(view: View, val binding: (View, T) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: T, listener: (T) -> Unit) = with(itemView) {
            binding(itemView, item)
            setOnClickListener { listener(item) }
        }
    }
}
