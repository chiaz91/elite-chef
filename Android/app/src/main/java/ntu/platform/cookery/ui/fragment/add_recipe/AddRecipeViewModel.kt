package ntu.platform.cookery.ui.fragment.add_recipe


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.MutableListLiveData
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.Ingredient
import ntu.platform.cookery.data.entity.Recipe
import ntu.platform.cookery.data.entity.RecipeStep
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import java.util.*

private const val TAG = "CY.VM.AddRecipe"
class AddRecipeViewModel: ViewModel() {
    val toastMsg = SingleLiveEvent<String>()
    val onSaveComplete = SingleLiveEvent<Any>()

    val user = FBDatabaseRepository.getUser()
    val recipeId = MutableLiveData<String?>()
    val timeCreated = MutableLiveData<Long?>()

    // recipe info
    val name = MutableLiveData<String?>()
    val desc = MutableLiveData<String?>()
    val graphic = MutableLiveData<String?>()
    val ingredients = MutableListLiveData<Ingredient>()
    val steps = MutableListLiveData<RecipeStep>()

    val timePrepare = MutableLiveData<String?>("0")
    val timeBake = MutableLiveData<String?>("0")
    val timeRest = MutableLiveData<String?>("0")


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
        timePrepare.value = recipe.timePrepareMin.toString()
        timeBake.value = recipe.timeBakingMin.toString()
        timeRest.value = recipe.timeRestMin.toString()
        timeCreated.value = recipe.timeCreated
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
        if (ingredients.isEmpty()){
            toastMsg.value = "ingredients cannot be empty"
            return
        }

        if (steps.isEmpty()){
            toastMsg.value = "Steps cannot be empty"

            return
        }

        Log.d(TAG, "save recipe")
        val recipe = Recipe(
            name = name.value!!,
            description = desc.value!!,
            graphic = graphic.value?.toString(),
            timePrepareMin = Integer.parseInt(timePrepare.value?.toString() ?: "0"),
            timeBakingMin = Integer.parseInt(timeBake.value?.toString() ?: "0"),
            timeRestMin = Integer.parseInt(timeRest.value?.toString() ?: "0"),
            timeCreated = timeCreated.value?: Date().time,
            authorId = user.value!!.uid,
            author = user.value!!,
            key = recipeId.value
        )
        // TODO: send out event?
        val refRecipe = FBDatabaseRepository.saveRecipe(recipe)
        FBDatabaseRepository.saveRecipeIngredients(refRecipe.key!!, ingredients.value!!)
        FBDatabaseRepository.saveRecipeSteps(refRecipe.key!!, steps.value!!)
        onSaveComplete.call()
    }

    fun clearIngredientInfo(){
        ingredientName.value = null
        ingredientAmount.value = null
        ingredientUnit.value = null
    }

    fun clearStepInfo(){
        stepGraphic.value = null
        instruction.value = null
    }


    fun clear(){
        recipeId.value = null
        name.value = null
        desc.value = null
        graphic.value = null
        timePrepare.value = "0"
        timeBake.value = "0"
        timeRest.value = "0"
        ingredients.clear()
        steps.clear()
    }


}