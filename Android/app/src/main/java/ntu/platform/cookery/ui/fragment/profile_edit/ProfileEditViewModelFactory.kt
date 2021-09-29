package ntu.platform.cookery.ui.fragment.profile_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ntu.platform.cookery.data.entity.ECUser

class ProfileEditViewModelFactory (private val user: ECUser) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileEditViewModel::class.java)) {
            return ProfileEditViewModel(user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}