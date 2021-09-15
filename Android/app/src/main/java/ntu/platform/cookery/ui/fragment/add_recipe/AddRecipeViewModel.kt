package ntu.platform.cookery.ui.fragment.add_recipe


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.MutableListLiveData
import ntu.platform.cookery.data.entity.Ingredient
import ntu.platform.cookery.data.entity.Recipe
import ntu.platform.cookery.data.entity.RecipeStep
import ntu.platform.cookery.data.firebase.FBRepository

private const val TAG = "CY.VM.AddRecipe"
class AddRecipeViewModel: ViewModel() {
    val recipeId = MutableLiveData<String?>()

    // recipe info
    val name = MutableLiveData<String?>()
    val desc = MutableLiveData<String?>()
    val graphic = MutableLiveData<String?>()
    val ingredients = MutableListLiveData<Ingredient>()
    val steps = MutableListLiveData<RecipeStep>()

    // ingredient info
    val ingredientName = MutableLiveData<String?>()
    val ingredientAmount = MutableLiveData<String?>()
    val ingredientUnit = MutableLiveData<String?>()

    // step info
    val instruction = MutableLiveData<String?>()
    val stepGraphic = MutableLiveData<String?>()


    fun setUpRecipe(recipe: Recipe){
        name.value = recipe.name
        desc.value = recipe.description
        graphic.value = recipe.graphic
    }
    fun setUpIngredients(ingredients: List<Ingredient>){
        this.ingredients.clear()
        this.ingredients.addAll(ingredients)
    }

    fun setUpSteps(steps: List<RecipeStep>){
        this.steps.clear()
        this.steps.addAll(steps)
    }

    fun addStep(){
        // TODO: validate data
        val step  = RecipeStep(instruction.value!!,stepGraphic.value?.toString() )
        steps.add( step )
    }



    fun saveRecipe(){
        // TODO: validate data
        // TODO: save data
        // TODO: navigate and clear viewModel

        Log.d(TAG, "save recipe")
        val recipe = Recipe(
            name.value!!,
            desc.value!!,
            graphic.value?.toString(),
            key = recipeId.value
        )
        // TODO: send out event
        val refRecipe = FBRepository.saveRecipe(recipe)
        FBRepository.saveRecipeIngredients(refRecipe.key!!, ingredients.value!!)
        FBRepository.saveRecipeSteps(refRecipe.key!!, steps.value!!)
    }

    fun clearIngredientInfo(){
        ingredientName.value = null
        ingredientAmount.value = null
        ingredientUnit.value = null
    }

    fun clearStepInfo(){
        instruction.value = null
    }


    fun clear(){
        // TODO: implement clear
        recipeId.value = null
        name.value = null
        desc.value = null
        graphic.value = null
        ingredients.clear()
        steps.clear()
    }


}