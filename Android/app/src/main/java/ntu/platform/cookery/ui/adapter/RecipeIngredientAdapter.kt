package ntu.platform.cookery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import ntu.platform.cookery.BR
import ntu.platform.cookery.databinding.ItemAddMoreBinding
import ntu.platform.cookery.databinding.ItemIngredientBinding
import ntu.platform.cookery.databinding.ItemIngredientBindingImpl
import ntu.platform.cookery.data.entity.Ingredient
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewAdapter
import ntu.platform.cookery.base.BaseRecyclerViewHolder



class RecipeIngredientAdapter: BaseRecyclerViewAdapter<Ingredient>() {
    companion object{
        private const val TYPE_ADD_MORE = 0
        private const val TYPE_ITEM = 1
        const val ACTION_ADD_MORE_CLICK = 1001
        const val ACTION_ITEM_CLICK = 1002
        const val ACTION_DELETE_ITEM = 1003
    }

    var allowEdit = false
    var clickedListener: BaseClickedListener? = null

    override fun getItemCount() = when{
        allowEdit -> super.getItemCount() +1
        else -> super.getItemCount()
    }


    override fun getItemViewType(position: Int): Int {
        return when{
            allowEdit && (position+1 == itemCount) -> TYPE_ADD_MORE
            else -> TYPE_ITEM
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType){
            TYPE_ADD_MORE -> {
                val binding = ItemAddMoreBinding.inflate(inflater, parent, false)
                AddMoreViewHolder(binding)
            }
            else -> {
                val binding = ItemIngredientBinding.inflate(inflater, parent, false)
                IngredientItemViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        when (getItemViewType(position)){
            TYPE_ADD_MORE -> (holder as AddMoreViewHolder).bindAs(clickedListener)
            else -> {
                val ingredient = items?.get(position)!!
                (holder as IngredientItemViewHolder).bindAs(ingredient, allowEdit, clickedListener)
            }
        }
    }


    // view holder
    class AddMoreViewHolder(override val binding: ItemAddMoreBinding): BaseRecyclerViewHolder(binding){
        fun bindAs(clickedListener: BaseClickedListener?=null){
            itemView.setOnClickListener{
                clickedListener?.onClick(ACTION_ADD_MORE_CLICK, this)
            }
        }
    }

    class IngredientItemViewHolder(override val binding: ItemIngredientBinding): BaseRecyclerViewHolder(binding){
        fun bindAs(ingredient: Ingredient, enableEdit: Boolean, clickedListener: BaseClickedListener?=null){
            itemView.setOnClickListener{
                clickedListener?.onClick(ACTION_ITEM_CLICK, this)
            }
            binding.setVariable(BR.item, ingredient)
            binding.btnDelete.visibility = when {
                enableEdit -> View.VISIBLE
                else -> View.GONE
            }
            binding.btnDelete.setOnClickListener{
                clickedListener?.onClick(ACTION_DELETE_ITEM, this)
            }
        }
    }

}