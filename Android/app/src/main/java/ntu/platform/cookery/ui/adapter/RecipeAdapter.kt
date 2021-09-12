package ntu.platform.cookery.ui.adapter

import android.view.ViewGroup
import ntu.platform.cookery.BR
import ntu.platform.cookery.databinding.ItemRecipeBinding
import ntu.platform.cookery.data.entity.Recipe
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewAdapter
import ntu.platform.cookery.base.BaseRecyclerViewHolder



class RecipeAdapter(var clickedListener: BaseClickedListener? = null): BaseRecyclerViewAdapter<Recipe>() {
    companion object{
        const val ACTION_ITEM_CLICK = 1001
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        return BaseRecyclerViewHolder.from(parent, ItemRecipeBinding::inflate)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            clickedListener?.onClick(ACTION_ITEM_CLICK, holder)
        }
        (holder.binding as ItemRecipeBinding).apply {
            this.setVariable(BR.item, items?.get(position))
            // setVar, move others to bindingAdapter??

        }
    }


}