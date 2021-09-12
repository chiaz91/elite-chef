package ntu.platform.cookery.ui.fragment.recipe_details

import android.util.Log
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.data.firebase.FBRepository
import ntu.platform.cookery.ui.adapter.RecipeIngredientAdapter
import ntu.platform.cookery.ui.adapter.RecipeStepLargePicAdapter

private const val TAG = "CY.VM.RecipeDetail"
class RecipeDetailsViewModel(private val recipeId: String): ViewModel() {
    var recipe = FBRepository.getRecipe(recipeId)
    var ingredients = FBRepository.getRecipeIngredients(recipeId)
    var steps = FBRepository.getRecipeSteps(recipeId)
    var ingredientsAdapter = RecipeIngredientAdapter()
    var stepsAdapter = RecipeStepLargePicAdapter()


    init {
        Log.d(TAG, "recipeId = $recipeId")
    }





}