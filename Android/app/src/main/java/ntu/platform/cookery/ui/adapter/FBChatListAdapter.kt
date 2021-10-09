package ntu.platform.cookery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ntu.platform.cookery.BR
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewHolder
import ntu.platform.cookery.data.entity.Chat
import ntu.platform.cookery.databinding.ItemChatRoomBinding


class FBChatListAdapter(
    options: FirebaseRecyclerOptions<Chat>,

    var clickedListener: BaseClickedListener? = null
): FirebaseRecyclerAdapter<Chat, FBChatListAdapter.ChatroomViewHolder>(options) {
    companion object{
        const val ACTION_ITEM_CLICK = 1001
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatRoomBinding.inflate(inflater, parent, false)
        return ChatroomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatroomViewHolder, position: Int, model: Chat) {
        holder.bindAs(model, clickedListener)
    }


    // ViewHolder
    class ChatroomViewHolder(binding: ViewDataBinding) : BaseRecyclerViewHolder(binding){
        fun bindAs(chatroom: Chat, clickedListener: BaseClickedListener?){
            binding.setVariable(BR.item, chatroom)
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