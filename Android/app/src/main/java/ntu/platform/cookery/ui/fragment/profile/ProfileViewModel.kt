package ntu.platform.cookery.ui.fragment.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.Post
import ntu.platform.cookery.data.entity.Recipe
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBPostsAdapter
import ntu.platform.cookery.ui.adapter.FBRecipeAdapter

private const val TAG = "Cy.profile.vm"
class ProfileViewModel : ViewModel() {
    val user = FBDatabaseRepository.getUser()
    val onPostClicked = SingleLiveEvent<Post>()
    val onRecipeClicked = SingleLiveEvent<Recipe>()

    val postOptions = FBDatabaseRepository.getUserPostOptions()
    val postAdapter = FBPostsAdapter(postOptions).also {
        it.clickedListener = BaseClickedListener{action, viewHolder ->
            Log.d(TAG, "$action, click on item ${viewHolder.bindingAdapterPosition}")
            val pos = viewHolder.bindingAdapterPosition
            val post = it.getItem(pos).apply {
                key = it.getRef(pos).key
            }
            onPostClicked.value = post
        }
    }

    val recipeOptions = FBDatabaseRepository.getUserRecipeOptions()
    val recipeAdapter = FBRecipeAdapter(recipeOptions).also {
        it.clickedListener = BaseClickedListener { action, viewHolder ->
            Log.d(TAG, "$action, click on item ${viewHolder.bindingAdapterPosition}")
            val pos = viewHolder.bindingAdapterPosition
            val recipe = it.getItem(pos).apply {
                key = it.getRef(pos).key
            }
            onRecipeClicked.value = recipe
        }

    }
}