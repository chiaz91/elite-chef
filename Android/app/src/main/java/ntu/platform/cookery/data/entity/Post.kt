package ntu.platform.cookery.data.entity

import com.google.firebase.database.Exclude

class Post (
    var message: String? =null,
    var graphic: String? = null,
    var timeCreated: Long?=null,

    var user: ECUser? = null,
    @get:Exclude var key:String? = null
)