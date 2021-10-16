package ntu.platform.cookery.ui.fragment.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.Post
import ntu.platform.cookery.data.entity.Recipe
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBPostsAdapter
import ntu.platform.cookery.ui.adapter.FBRecipeAdapter

private const val TAG = "Cy.profile.vm"
class ProfileViewModel(val userId:String=FBAuthRepository.getUser()!!.uid) : ViewModel() {
    val isCurrentUser = FBAuthRepository.getUser()!!.uid == userId
    val user = FBDatabaseRepository.getUser(userId)
    val hasFollowUser = FBDatabaseRepository.hasFollowUser(userId)
    val following = FBDatabaseRepository.getUserFollowingIds(userId)
    val followBy  = FBDatabaseRepository.getUserFollowByIds(userId)

    val onPostClicked = SingleLiveEvent<Post>()
    val onRecipeClicked = SingleLiveEvent<Recipe>()

    val postOptions = FBDatabaseRepository.getUserPostOptions(userId)
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

    val recipeOptions = FBDatabaseRepository.getUserRecipeOptions(userId)
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

    val recipeLikedOptions = FBDatabaseRepository.getUserLikedRecipe(userId)
    val recipeLikedAdapter = FBRecipeAdapter(recipeLikedOptions).also {
        it.clickedListener = BaseClickedListener { action, viewHolder ->
            Log.d(TAG, "$action, click on item ${viewHolder.bindingAdapterPosition}")
            val pos = viewHolder.bindingAdapterPosition
            val recipe = it.getItem(pos).apply {
                key = it.getRef(pos).key
            }
            onRecipeClicked.value = recipe
        }
    }

    fun onFollowClicked(){
        when(hasFollowUser.value!!){
            true -> FBDatabaseRepository.unfollowUser(user.value!!)
            else -> FBDatabaseRepository.followUser(user.value!!)
        }
    }


}