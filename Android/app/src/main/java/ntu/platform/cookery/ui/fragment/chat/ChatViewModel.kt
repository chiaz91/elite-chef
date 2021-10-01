package ntu.platform.cookery.ui.fragment.chat

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.data.entity.ChatMessage
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBChatMessagesAdapter
import java.util.*

private const val TAG="Cy.chat.vm"
class ChatViewModel(val chatId: String ) : ViewModel() {
    // ui
    val message = MutableLiveData<String?>()
    // remote data
    val user = FBDatabaseRepository.getUser()
    val chat = FBDatabaseRepository.getChat(chatId)
    // TODO load chat members
    val chatMessagesOptions = FBDatabaseRepository.getChatMessagesOptions(chatId)
    val messageAdapter = FBChatMessagesAdapter(chatMessagesOptions)




    fun sendMessage(){
        Log.d(TAG, "message=${message.value}")
        if (!TextUtils.isEmpty(message.value)){
            val chatMessage = ChatMessage(
                senderId = user.value!!.uid,
                sender = user.value!!.name,
                message = message.value,
                timestamp = Date().time
            )
            FBDatabaseRepository.saveChatMessage(chat.value!!, chatMessage)
            message.value = ""
        }

    }
}