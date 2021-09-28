package ntu.platform.cookery.data.entity

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    var name:String? = null,
    var description: String? = null,
    var graphic: String? = null,
    var timePrepareMin: Int = 0,
    var timeBakingMin: Int = 0,
    var timeRestMin: Int = 0,
    var timeCreated: Long?=null,
    var authorId: String? = null,
    var author: ECUser? = null,
    @get:Exclude var key: String? = null,
): Parcelable{
    fun totalTime() = timePrepareMin+timeBakingMin+timeRestMin
}