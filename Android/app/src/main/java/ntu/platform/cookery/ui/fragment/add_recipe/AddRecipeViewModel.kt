package ntu.platform.cookery.ui.fragment.add_recipe


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ntu.platform.cookery.base.MutableListLiveData
import ntu.platform.cookery.data.entity.Ingredient
import ntu.platform.cookery.data.entity.Recipe
import ntu.platform.cookery.data.entity.RecipeStep
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBRepository

private const val TAG = "CY.VM.AddRecipe"
class AddRecipeViewModel: ViewModel() {
    val user = FBAuthRepository.userLiveData
    val recipeId = MutableLiveData<String?>()

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

        Log.d(TAG, "save recipe")
        val recipe = Recipe(
            name = name.value!!,
            description = desc.value!!,
            graphic = graphic.value?.toString(),
            timePrepareMin = Integer.parseInt(timePrepare.value?.toString() ?: "0"),
            timeBakingMin = Integer.parseInt(timeBake.value?.toString() ?: "0"),
            timeRestMin = Integer.parseInt(timeRest.value?.toString() ?: "0"),
            author = user.value!!.uid,
            key = recipeId.value
        )
        // TODO: send out event?
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