package ntu.platform.cookery.ui.fragment.follow_list


import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.UserAdapter

class FollowListViewModel(val userid: String = FBAuthRepository.getUser()!!.uid) : ViewModel() {
    val curUserId = FBAuthRepository.getUser()!!.uid
    val users = FBDatabaseRepository.getFollowingUsers(userid)
    val onUserClicked = SingleLiveEvent<ECUser>()
    val adapter = UserAdapter().also {
        it.clickedListener = BaseClickedListener{action, viewHolder ->
            val pos = viewHolder.bindingAdapterPosition
            val user = it.items!![pos]
            onUserClicked.value = user
        }
    }

}