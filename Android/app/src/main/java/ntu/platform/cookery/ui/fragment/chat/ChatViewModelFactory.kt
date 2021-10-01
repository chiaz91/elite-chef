package ntu.platform.cookery.ui.fragment.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.firebase.FBAuthRepository

class ChatViewModelFactory (val chatId:String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(chatId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}