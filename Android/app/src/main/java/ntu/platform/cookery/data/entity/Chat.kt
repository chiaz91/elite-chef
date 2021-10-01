package ntu.platform.cookery.data.entity

import com.google.firebase.database.Exclude


data class Chat(
    var title: String? = null, // temp: nameA and nameB
    var senderId: String? = null,
    var sender:String? = null,
    var lastMessage:String? = null,
    var lastUpdate: Long? = null,
    @get:Exclude var key: String? =null,
    @get:Exclude var members: MutableList<ECUser>? =null,
){

    fun updateAs(chatMessage: ChatMessage){
        senderId = chatMessage.senderId
        sender = chatMessage.sender
        lastMessage = chatMessage.message
        lastUpdate = chatMessage.timestamp
    }
}

