package ntu.platform.cookery.ui.fragment.post_comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ntu.platform.cookery.data.entity.Post

class PostCommentsViewModelFactory (private val post: Post) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostCommentsViewModel::class.java)) {
            return PostCommentsViewModel(post) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}