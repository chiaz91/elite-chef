package ntu.platform.cookery.base

import androidx.recyclerview.widget.RecyclerView


// https://stackoverflow.com/questions/60423596/how-to-use-viewbinding-in-a-recyclerview-adapter
abstract class BaseRecyclerViewAdapter<T: Any>: RecyclerView.Adapter<BaseRecyclerViewHolder>() {
    var items: List<T>? = mutableListOf()

    override fun getItemCount() = items?.size ?: 0
}