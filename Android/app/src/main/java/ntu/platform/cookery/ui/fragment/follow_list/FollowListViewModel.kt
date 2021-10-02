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

    val onUserClicked = SingleLiveEvent<ECUser>()
    val followingOptions = FBDatabaseRepository.getFollowingOptions(userid)
    val followingAdapter = FBUserAdapter(followingOptions).also {
        it.clickedListener = BaseClickedListener{action, viewHolder ->
            val pos = viewHolder.bindingAdapterPosition
            val user = it.getItem(pos).apply {
                uid = it.getRef(pos).key
            }
            onUserClicked.value = user
        }
    }
    val followerOptions = FBDatabaseRepository.getFollowByOptions(userid)
    val followerAdapter = FBUserAdapter(followerOptions).also {
        it.clickedListener = BaseClickedListener{action, viewHolder ->
            val pos = viewHolder.bindingAdapterPosition
            val user = it.getItem(pos).apply {
                uid = it.getRef(pos).key
            }
            onUserClicked.value = user
        }
    }

}