package ntu.platform.cookery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ntu.platform.cookery.BR
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewHolder
import ntu.platform.cookery.data.entity.Post
import ntu.platform.cookery.databinding.ItemNewsfeedBinding


class FBPostsAdapter(
        options: FirebaseRecyclerOptions<Post>,
        var clickedListener: BaseClickedListener? = null
): FirebaseRecyclerAdapter<Post, FBPostsAdapter.PostViewHolder>(options) {
    companion object{
        const val ACTION_ITEM_CLICK = 1001
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsfeedBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.bindAs(model, clickedListener)
    }


    // ViewHolder
    class PostViewHolder(binding: ViewDataBinding) : BaseRecyclerViewHolder(binding){
        fun bindAs(post: Post, clickedListener: BaseClickedListener?){
            binding.setVariable(BR.item, post)
            itemView.setOnClickListener{
                clickedListener?.onClick(ACTION_ITEM_CLICK, this)
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