package ntu.platform.cookery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import ntu.platform.cookery.BR
import ntu.platform.cookery.databinding.*
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewAdapter
import ntu.platform.cookery.base.BaseRecyclerViewHolder
import ntu.platform.cookery.data.entity.ECUser


class UserAdapter(var clickedListener: BaseClickedListener? = null): BaseRecyclerViewAdapter<ECUser>() {
    companion object{
        const val ACTION_ITEM_CLICK = 1001
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> {
                val user = items?.get(position)!!
                holder.bindAs( user, clickedListener)
            }
        }

    }


    // View Holder
    class UserViewHolder(override val binding: ItemUserBinding): BaseRecyclerViewHolder(binding){
        fun bindAs(user: ECUser, clickedListener: BaseClickedListener?=null){
            itemView.tag = user
            itemView.setOnClickListener{
                clickedListener?.onClick(ACTION_ITEM_CLICK, this)
            }
            binding.setVariable(BR.item, user)
        }
    }


}