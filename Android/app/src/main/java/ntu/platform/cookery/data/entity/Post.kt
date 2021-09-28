package ntu.platform.cookery.data.entity

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
class Post (
    var message: String? =null,
    var graphic: String? = null,
    var timeCreated: Long?=null,

    var userId: String? = null,
    var user: ECUser? = null,
    @get:Exclude var key:String? = null
): Parcelable