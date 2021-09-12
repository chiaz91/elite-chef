package ntu.platform.cookery.base

import androidx.recyclerview.widget.RecyclerView

fun interface BaseClickedListener{
    fun onClick(action: Int, viewHolder: RecyclerView.ViewHolder)
}
