package ntu.platform.cookery.ui.fragment.recipe_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.data.entity.UserComment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBUserCommentAdapter
import ntu.platform.cookery.ui.adapter.RecipeIngredientAdapter
import ntu.platform.cookery.ui.adapter.RecipeStepLargePicAdapter
import java.util.*

private const val TAG = "CY.VM.RecipeDetail"
class RecipeDetailsViewModel(val recipeId: String): ViewModel() {
    var user = FBDatabaseRepository.getUser()
    var recipe = FBDatabaseRepository.getRecipe(recipeId)
    var ingredients = FBDatabaseRepository.getRecipeIngredients(recipeId)
    var steps = FBDatabaseRepository.getRecipeSteps(recipeId)
    var ingredientsAdapter = RecipeIngredientAdapter()
    var stepsAdapter = RecipeStepLargePicAdapter()



    val message = MutableLiveData<String?>()
    val commentOption = FBDatabaseRepository.getRecipeCommentOptions(recipeId)
    val commentAdapter = FBUserCommentAdapter(options = commentOption)

    init {
        // TODO: to remove
        Log.d(TAG, "recipeId = $recipeId")
    }


    fun sendMessage(){
        message.value?.isNotEmpty().let {
            val input = message.value.toString()
            val comment = UserComment(
                message = input,
                timeCreated = Date().time,
//                name = user.value!!.name,
//                graphic = user.value!!.graphic,
                uid = user.value!!.uid,
                user = user.value!!
            )
            FBDatabaseRepository.saveRecipeComment(recipeId, comment)
            message.value = ""
        }

    }





}