package ntu.platform.cookery.ui.fragment.post_comments


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.entity.Post
import ntu.platform.cookery.data.entity.UserComment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBUserCommentAdapter
import java.util.*

class PostCommentsViewModel(val post: Post) : ViewModel() {
    val message = MutableLiveData<String?>()
    val user = FBDatabaseRepository.getUser()
    val onProfileClick = SingleLiveEvent<ECUser>()
    val commentOptions = FBDatabaseRepository.getPostCommentsOptions(post.key!!)
    val commentAdapter = FBUserCommentAdapter(options = commentOptions).also {
        it.clickedListener = BaseClickedListener{action, holder->
            val pos = holder.bindingAdapterPosition
            val comment = it.getItem(pos).apply {
                key = it.getRef(pos).key
                user?.uid = uid
            }

            if (action == FBUserCommentAdapter.ACTION_PROFILE_CLICK){
                onProfileClick.value = comment.user!!
            }
        }
    }

    fun sendMessage(){
        message.value?.isNotEmpty().let {
            val input = message.value.toString()
            val comment = UserComment(
                message = input,
                timeCreated = Date().time,
                uid = user.value!!.uid,
                user = user.value!!
            )
            FBDatabaseRepository.savePostComments(post.key!!, comment)
            message.value = ""
        }

    }

}