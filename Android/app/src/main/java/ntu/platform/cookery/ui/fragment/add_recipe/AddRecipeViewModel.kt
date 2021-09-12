package ntu.platform.cookery.ui.fragment.add_recipe


import android.net.Uri
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
        val recipe = Recipe(name.value!!, desc.value!!, graphic.value?.toString() )
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

        name.value = null
        desc.value = null
        graphic.value = null
        ingredients.clear()
        steps.clear()
    }

    init{
    // test data
        name.value = "cold noodle"
        desc.value = "home made cold noodle"
        ingredients.apply {
            add(Ingredient("Cucumber", 1, "unit"))
            add(Ingredient("Carrot", 1, "unit"))
            add(Ingredient("Noodle", 1, "package"))
        }
        steps.apply {
            add(RecipeStep("Cut cucumber"))
            add(RecipeStep( "Cut Carrot"))
            add(RecipeStep( "Mix together"))
            add(RecipeStep( "place in refrigerator"))
        }

    }



}