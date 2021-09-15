package ntu.platform.cookery.data.firebase

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import ntu.platform.cookery.data.entity.Ingredient
import ntu.platform.cookery.data.entity.Recipe
import ntu.platform.cookery.data.entity.RecipeStep

// firebase realtime database

private const val TAG = "Cy.FB.Database"
private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
object FBRepository{
    private lateinit var db: FirebaseDatabase


    init{
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Fb.database init")
//            Firebase.database.useEmulator("10.0.2.2", 9000)
        }
        //FirebaseDatabase.getInstance("url if needed")
        db = Firebase.database

        // TODO, test offline
        db.setPersistenceEnabled(true)
    }


    // Recipes
    fun saveRecipe(recipe: Recipe): DatabaseReference{
        // create or update recipe
        val refRoot = db.reference.child(REF_RECIPES)
        val refRecipe = when{
            recipe.key!= null -> refRoot.child(recipe.key!!)
            else -> refRoot.push()
        }
        Log.d(TAG, "ref.key = ${refRecipe.key}")
        recipe.key = refRecipe.key
        refRecipe.setValue(recipe)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d(TAG, "saveRecipe success")
                } else {
                    Log.d(TAG, "saveRecipe failed with ${it.exception!!.message}")
                }
            }
        return refRecipe
    }

    fun saveRecipeIngredients(recipeId: String, ingredients: List<Ingredient>){
        db.reference.child(REF_INGREDIENTS).child(recipeId).setValue(ingredients)
    }

    fun saveRecipeSteps(recipeId: String, steps: List<RecipeStep>){
        db.reference.child(REF_STEPS).child(recipeId).setValue(steps)
    }

    fun getRecipe(recipeId: String): MutableLiveData<Recipe>{
        val result = MutableLiveData<Recipe>()
        db.reference.child(REF_RECIPES).child(recipeId).get()
            .addOnSuccessListener {
                result.value = it.getValue<Recipe>()
            }
            .addOnFailureListener{
                Log.e(TAG, "getRecipe failed, err=${it.message}")
            }
        return result
    }

    fun getRecipeIngredients(recipeId: String): MutableLiveData<List<Ingredient>>{
        val result = MutableLiveData<List<Ingredient>>()
        db.reference.child(REF_INGREDIENTS).child(recipeId).get()
            .addOnSuccessListener {
                result.value = it.getValue<List<Ingredient>>()
            }
            .addOnFailureListener{
                Log.e(TAG, "getRecipeIngredients failed, err=${it.message}")
            }
        return result
    }

    fun getRecipeSteps(recipeId: String): MutableLiveData<List<RecipeStep>>{
        val result = MutableLiveData<List<RecipeStep>>()
        db.reference.child(REF_STEPS).child(recipeId).get()
            .addOnSuccessListener {
                result.value = it.getValue<List<RecipeStep>>()
            }
            .addOnFailureListener {
                Log.e(TAG, "getRecipeSteps failed, err=${it.message}")
            }
        return result
    }

    fun deleteRecipe(recipeId: String){
        db.reference.child(REF_INGREDIENTS).child(recipeId).removeValue()
        db.reference.child(REF_STEPS).child(recipeId).removeValue()
        db.reference.child(REF_RECIPES).child(recipeId).removeValue()
    }

    fun getRecipeOptions(): FirebaseRecyclerOptions<Recipe> {
        val recipeRef = db.reference.child(REF_RECIPES)

        return FirebaseRecyclerOptions.Builder<Recipe>()
            .setQuery(recipeRef, Recipe::class.java)
            .build()
    }











}