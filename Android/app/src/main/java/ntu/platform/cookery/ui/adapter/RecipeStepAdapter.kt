package ntu.platform.cookery.ui.adapter

import android.content.ClipData
import android.view.LayoutInflater
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

        const val ACTION_ADD_MORE_CLICK = 1001
        const val ACTION_ITEM_CLICK = 1002
        const val ACTION_ITEM_DELETE = 1003

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
        val inflater = LayoutInflater.from(parent.context)

        return when(viewType){
            TYPE_ADD_MORE -> {
                val binding = ItemAddMoreBinding.inflate(inflater, parent, false)
                AddMoreViewHolder(binding)
            }
            else -> {
                val binding = ItemStepBinding.inflate(inflater, parent, false)
                StepItemViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        when (holder) {
            is StepItemViewHolder -> {
                val step = items?.get(position)!!
                holder.bindAs( step, clickedListener)
            }
            is AddMoreViewHolder -> holder.bindAs(clickedListener)
        }

    }


    // View Holder
    class AddMoreViewHolder(override val binding: ItemAddMoreBinding): BaseRecyclerViewHolder(binding){
        fun bindAs(clickedListener: BaseClickedListener?=null){
            itemView.setOnClickListener{
                clickedListener?.onClick(ACTION_ADD_MORE_CLICK, this)
            }
        }
    }

    class StepItemViewHolder(override val binding: ItemStepBinding): BaseRecyclerViewHolder(binding){
        fun bindAs(step: RecipeStep, clickedListener: BaseClickedListener?=null){
            itemView.setOnClickListener{
                clickedListener?.onClick(ACTION_ITEM_CLICK, this)
            }
            binding.setVariable(BR.item, step)
            binding.setVariable(BR.count, bindingAdapterPosition+1)
            binding.btnDelete.setOnClickListener{
                clickedListener?.onClick(ACTION_ITEM_DELETE, this)
            }
        }
    }





}