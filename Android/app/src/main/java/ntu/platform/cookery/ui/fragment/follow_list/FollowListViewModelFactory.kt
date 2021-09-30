package ntu.platform.cookery.ui.fragment.follow_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ntu.platform.cookery.data.firebase.FBAuthRepository

class FollowListViewModelFactory (private val userId: String = FBAuthRepository.getUser()!!.uid) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowListViewModel::class.java)) {
            return FollowListViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}