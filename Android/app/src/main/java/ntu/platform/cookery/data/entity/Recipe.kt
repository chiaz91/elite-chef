package ntu.platform.cookery.data.entity

import com.google.firebase.database.Exclude

data class Recipe(
    var name:String? = null,
    var description: String? = null,
    var graphic: String? = null,
    var timePrepareMin: Int = 0,
    var timeBakingMin: Int = 0,
    var timeRestMin: Int = 0,
    var author: String? = null,
    @get:Exclude var key: String? = null,
){

    fun totalTime() = timePrepareMin+timeBakingMin+timeRestMin
}