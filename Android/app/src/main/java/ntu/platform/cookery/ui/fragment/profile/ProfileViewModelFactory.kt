package ntu.platform.cookery.ui.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ntu.platform.cookery.data.firebase.FBAuthRepository

class ProfileViewModelFactory (private val userId: String = FBAuthRepository.getUser()!!.uid) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}