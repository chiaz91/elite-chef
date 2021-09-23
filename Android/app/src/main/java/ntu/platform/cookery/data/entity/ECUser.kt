package ntu.platform.cookery.data.entity

import android.net.Uri
import com.google.firebase.database.Exclude

data class ECUser(
    var name: String? = null,
    var bio: String? = null,
    var graphic: String? = null,
    @get:Exclude var uid:String? = null

)