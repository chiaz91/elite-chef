package ntu.platform.cookery.ui.fragment.recipe_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.entity.UserComment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBUserCommentAdapter
import ntu.platform.cookery.ui.adapter.RecipeIngredientAdapter
import ntu.platform.cookery.ui.adapter.RecipeStepLargePicAdapter
import java.util.*

private const val TAG = "CY.VM.RecipeDetail"
class RecipeDetailsViewModel(val recipeId: String): ViewModel() {
    val liked = FBDatabaseRepository.hasLikeRecipe(recipeId)
    var user = FBDatabaseRepository.getUser()
    var recipe = FBDatabaseRepository.getRecipe(recipeId)
    var ingredients = FBDatabaseRepository.getRecipeIngredients(recipeId)
    var steps = FBDatabaseRepository.getRecipeSteps(recipeId)
    var ingredientsAdapter = RecipeIngredientAdapter()
    var stepsAdapter = RecipeStepLargePicAdapter()

    val onProfileClick = SingleLiveEvent<ECUser>()


    val message = MutableLiveData<String?>()
    val commentOption = FBDatabaseRepository.getRecipeCommentOptions(recipeId)
    val commentAdapter = FBUserCommentAdapter(options = commentOption).also {
        it.clickedListener = BaseClickedListener{action, holder->
            val pos = holder.bindingAdapterPosition
            val comment = it.getItem(pos).apply {
                key = it.getRef(pos).key
                user?.uid = uid
            }

            if (action == FBUserCommentAdapter.ACTION_PROFILE_CLICK){
                onProfileClick.value = comment.user!!
            }
        }
    }

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


    fun onLikeClicked(){
        when(liked.value!!){
            true -> FBDatabaseRepository.unlikeRecipe(recipeId)
            else -> FBDatabaseRepository.likeRecipe(recipeId)
        }
    }






}