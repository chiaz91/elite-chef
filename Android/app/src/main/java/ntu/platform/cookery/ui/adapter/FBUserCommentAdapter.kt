package ntu.platform.cookery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ntu.platform.cookery.BR
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewHolder
import ntu.platform.cookery.data.entity.UserComment
import ntu.platform.cookery.databinding.ItemUserCommentBinding


class FBUserCommentAdapter(
    options: FirebaseRecyclerOptions<UserComment>,
    var clickedListener: BaseClickedListener? = null
): FirebaseRecyclerAdapter<UserComment, FBUserCommentAdapter.CommentViewHolder>(options) {
    companion object{
        const val ACTION_ITEM_CLICK = 1001
        const val ACTION_PROFILE_CLICK = 1002
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserCommentBinding.inflate(inflater, parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, model: UserComment) {
        holder.bindAs(model, clickedListener)
    }


    // ViewHolder
    class CommentViewHolder(binding: ViewDataBinding) : BaseRecyclerViewHolder(binding){
        fun bindAs(recipe: UserComment, clickedListener: BaseClickedListener?){
            binding.setVariable(BR.item, recipe)
            itemView.tag = recipe
            itemView.setOnClickListener{
                clickedListener?.onClick(ACTION_ITEM_CLICK, this)
            }
            if (binding is ItemUserCommentBinding){
                binding.profilePic.setOnClickListener{
                    clickedListener?.onClick(ACTION_PROFILE_CLICK, this)
                }
            }
        }
    }


    // TODO: without notifyDataSetChanged will lead to IndexOutOfBoundsException, debug later
    override fun startListening() {
        super.startListening()
        notifyDataSetChanged()
    }

    override fun stopListening() {
        super.stopListening()
        notifyDataSetChanged()
    }
}