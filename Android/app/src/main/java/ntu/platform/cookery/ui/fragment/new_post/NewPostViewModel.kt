package ntu.platform.cookery.ui.fragment.new_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.data.entity.Post
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import java.util.*

class NewPostViewModel : ViewModel() {
    var user = FBDatabaseRepository.getUser()
    val message = MutableLiveData<String>()
    val graphic = MutableLiveData<String?>()


    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text


    fun save(){
        val post = Post(
            message = message.value,
            graphic = graphic.value,
            timeCreated = Date().time,
            user = user.value,
            userId = user.value!!.uid
        )

        FBDatabaseRepository.savePost(post)
    }



    fun clear(){
        message.value = ""
        graphic.value = null
    }

}