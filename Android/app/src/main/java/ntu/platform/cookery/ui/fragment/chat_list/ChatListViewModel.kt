package ntu.platform.cookery.ui.fragment.chat_list


import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.Chat
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBChatListAdapter

class ChatListViewModel : ViewModel() {
    val user = FBDatabaseRepository.getUser()
    val chatListOptions = FBDatabaseRepository.getChatList()
    val chatListAdapter = FBChatListAdapter(chatListOptions).also {
        it.clickedListener = BaseClickedListener{action, holder ->
            val pos = holder.bindingAdapterPosition
            val chat = it.getItem(pos).apply {
                key = it.getRef(pos).key
            }
            onChatClicked.value = chat
        }
    }

    val onChatClicked = SingleLiveEvent<Chat>()

    val test = FBDatabaseRepository.getChatList()

}