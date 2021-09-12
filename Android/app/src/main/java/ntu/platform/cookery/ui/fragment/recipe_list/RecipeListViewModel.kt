package ntu.platform.cookery.ui.fragment.recipe_list

import android.util.Log
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.firebase.FBRepository
import ntu.platform.cookery.ui.adapter.RecipeAdapter2

private const val TAG = "Cy.VM.RecipeList"
class RecipeListViewModel: ViewModel() {

    val options = FBRepository.getRecipeOptions()
    val recipeClicked = SingleLiveEvent<String>()


    val adapter =  RecipeAdapter2(options).also {
        it.clickedListener = BaseClickedListener { action, viewHolder ->
            Log.d(TAG, "$action, click on item ${viewHolder.bindingAdapterPosition}")
            val pos = viewHolder.bindingAdapterPosition

            Log.d(TAG, "item id: ${it.getItemId(pos)}")
            Log.d(TAG, "item key: ${it.getRef(pos).key}")

            recipeClicked.value = it.getRef(pos).key

        }
    }



}