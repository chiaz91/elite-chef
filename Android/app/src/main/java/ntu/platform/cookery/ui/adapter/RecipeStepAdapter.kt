package ntu.platform.cookery.ui.adapter

import android.view.ViewGroup
import ntu.platform.cookery.BR
import ntu.platform.cookery.databinding.*
import ntu.platform.cookery.data.entity.RecipeStep
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewAdapter
import ntu.platform.cookery.base.BaseRecyclerViewHolder



class RecipeStepAdapter(var clickedListener: BaseClickedListener? = null): BaseRecyclerViewAdapter<RecipeStep>() {
    companion object{
        private const val TYPE_ADD_MORE = 0
        private const val TYPE_ITEM = 1
        const val ACTION_ITEM_CLICK = 1001
        const val ACTION_ADD_MORE_CLICK = 1002

    }

    override fun getItemCount() = if (items!=null) {
        items!!.size+1
    } else {
        1
    }

    override fun getItemViewType(position: Int): Int {
        return when{
            (position+1 == itemCount) -> TYPE_ADD_MORE
            else -> TYPE_ITEM
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        return when(viewType){
            TYPE_ADD_MORE -> BaseRecyclerViewHolder.from(parent, ItemAddMoreBinding::inflate).also { holder ->
                holder.itemView.setOnClickListener {
                    clickedListener?.onClick(ACTION_ADD_MORE_CLICK, holder)
                }
            }
            else ->  BaseRecyclerViewHolder.from(parent, ItemStepBinding::inflate).also {holder ->
                holder.itemView.setOnClickListener {
                    clickedListener?.onClick(ACTION_ITEM_CLICK, holder)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ADD_MORE){
            return
        }

        if (holder.binding is ItemStepBinding){
            (holder.binding as ItemStepBinding).apply {
                this.setVariable(BR.item, items?.get(position))
                this.setVariable(BR.count, position+1)
            }
        }

    }


}