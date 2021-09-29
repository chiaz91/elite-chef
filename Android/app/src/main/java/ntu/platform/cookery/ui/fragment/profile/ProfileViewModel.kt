package ntu.platform.cookery.ui.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.data.firebase.FBDatabaseRepository

class ProfileViewModel : ViewModel() {
    val user = FBDatabaseRepository.getUser()

    private val _text = MutableLiveData<String>().apply {
        value = "profile Fragment WIP"
    }
    val text: LiveData<String> = _text
}