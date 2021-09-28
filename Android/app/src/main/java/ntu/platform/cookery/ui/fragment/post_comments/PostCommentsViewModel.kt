package ntu.platform.cookery.ui.fragment.post_comments


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.data.entity.Post
import ntu.platform.cookery.data.entity.UserComment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBUserCommentAdapter
import java.util.*

class PostCommentsViewModel(val post: Post) : ViewModel() {
    val message = MutableLiveData<String?>()
    val user = FBDatabaseRepository.getUser()
    val commentOptions = FBDatabaseRepository.getPostCommentsOptions(post.key!!)
    val commentAdapter = FBUserCommentAdapter(options = commentOptions)

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is profile Fragment"
//    }
//    val text: LiveData<String> = _text

    fun sendMessage(){
        message.value?.isNotEmpty().let {
            val input = message.value.toString()
            val comment = UserComment(
                message = input,
                timeCreated = Date().time,
//                name = user.value!!.name,
//                graphic = user.value!!.graphic,
                uid = user.value!!.uid,
                user = user.value!!
            )
            FBDatabaseRepository.savePostComments(post.key!!, comment)
            message.value = ""
        }

    }

}