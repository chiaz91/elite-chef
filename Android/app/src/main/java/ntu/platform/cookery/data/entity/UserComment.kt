package ntu.platform.cookery.data.entity


import com.google.firebase.database.Exclude
import java.util.*

data class UserComment(
    var message: String? =null,
    var timeCreated: Long?=null,

    // user info
//    var name: String? = null,
//    var graphic: String? = null,
    var uid:String? = null,
    var user: ECUser? = null,
    @get:Exclude var key:String? = null
)