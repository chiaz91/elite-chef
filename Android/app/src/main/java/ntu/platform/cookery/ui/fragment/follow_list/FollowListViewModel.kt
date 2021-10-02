package ntu.platform.cookery.ui.fragment.follow_list


import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBUserAdapter

class FollowListViewModel(val userid: String = FBAuthRepository.getUser()!!.uid) : ViewModel() {
    val curUserId = FBAuthRepository.getUser()!!.uid
    val followingOptions = FBDatabaseRepository.getFollowingOptions(userid)
    val onUserClicked = SingleLiveEvent<ECUser>()
    val adapter = FBUserAdapter(followingOptions).also {
        it.clickedListener = BaseClickedListener{action, viewHolder ->
            val pos = viewHolder.bindingAdapterPosition
            val user = it.getItem(pos).apply {
                uid = it.getRef(pos).key
            }
            onUserClicked.value = user
        }
    }

}