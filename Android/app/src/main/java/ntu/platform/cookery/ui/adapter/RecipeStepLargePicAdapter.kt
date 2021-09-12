package ntu.platform.cookery.ui.adapter

import android.view.ViewGroup
import ntu.platform.cookery.BR
import ntu.platform.cookery.databinding.*
import ntu.platform.cookery.data.entity.RecipeStep
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewAdapter
import ntu.platform.cookery.base.BaseRecyclerViewHolder



class RecipeStepLargePicAdapter(var clickedListener: BaseClickedListener? = null): BaseRecyclerViewAdapter<RecipeStep>() {
    companion object{
        const val ACTION_ITEM_CLICK = 1001
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        return BaseRecyclerViewHolder.from(parent, ItemStepLargeBinding::inflate).also { holder ->
            holder.itemView.setOnClickListener {
                clickedListener?.onClick(ACTION_ITEM_CLICK, holder)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        if (holder.binding is ItemStepLargeBinding){
            (holder.binding as ItemStepLargeBinding).apply {
                this.setVariable(BR.item, items?.get(position))
                this.setVariable(BR.count, position+1)
            }
        }
    }



}