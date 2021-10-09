package ntu.platform.cookery.data.entity

import com.google.firebase.database.Exclude

data class ChatMessage(
    val senderId: String? = null,
    val sender: String? = null,
    val message: String? = null,
    val graphic: String? = null,
    val timestamp: Long? = null,
    @get: Exclude var key: String? =null,
    @get: Exclude var user:ECUser? = null
)