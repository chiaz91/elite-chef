package ntu.platform.cookery.data.entity

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class ECUser(
    var name: String? = null,
    var bio: String? = null,
    var graphic: String? = null,
    @get:Exclude var uid:String? = null

): Parcelable