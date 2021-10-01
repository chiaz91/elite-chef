package ntu.platform.cookery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ntu.platform.cookery.BR
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewHolder
import ntu.platform.cookery.data.entity.ChatMessage
import ntu.platform.cookery.data.entity.UserComment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.databinding.ItemMsgReceiveBinding
import ntu.platform.cookery.databinding.ItemMsgSendBinding
import ntu.platform.cookery.databinding.ItemUserCommentBinding
import ntu.platform.cookery.util.isSameDate


class FBChatMessagesAdapter(
    options: FirebaseRecyclerOptions<ChatMessage>,
    val userId: String = FBAuthRepository.getUser()!!.uid,
    var clickedListener: BaseClickedListener? = null
): FirebaseRecyclerAdapter<ChatMessage, FBChatMessagesAdapter.MessageViewHolder>(options) {
    companion object{
        private const val TYPE_MSG_SEND = 1
        private const val TYPE_MSG_RECEIVE = 1

        const val ACTION_ITEM_CLICK = 1001
        const val ACTION_PROFILE_CLICK = 1002
    }



    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.senderId) {
            userId -> TYPE_MSG_SEND
            else -> TYPE_MSG_RECEIVE
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when(viewType){
            TYPE_MSG_SEND -> ItemMsgSendBinding.inflate(inflater, parent, false)
            else -> ItemMsgReceiveBinding.inflate(inflater, parent, false)
        }
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: ChatMessage) {
        var showDate = when{
            position == 0 -> true
            !isSameDate(model.timestamp!!, getItem(position-1).timestamp!!) -> true
            else -> false
        }
        holder.bindAs(model, showDate, clickedListener)
    }


    // ViewHolder
    class MessageViewHolder(binding: ViewDataBinding) : BaseRecyclerViewHolder(binding){
        fun bindAs(chatMessage: ChatMessage, showDate:Boolean, clickedListener: BaseClickedListener?){
            binding.setVariable(BR.item, chatMessage)
            binding.setVariable(BR.showDate, showDate)
            itemView.tag = chatMessage
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