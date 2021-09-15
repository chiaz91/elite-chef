package ntu.platform.cookery.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseRecyclerViewHolder (open val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root){

    /* Use like:
     * BaseRecyclerViewHolder.from(parent, ItemStepBinding::inflate)
     */
    companion object {
        inline fun from(
                parent: ViewGroup,
                bindingInflater: (inflater: LayoutInflater, container: ViewGroup, attach: Boolean) -> ViewDataBinding
        ): BaseRecyclerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = bindingInflater(layoutInflater, parent, false)
            return BaseRecyclerViewHolder(binding)
        }
    }

}