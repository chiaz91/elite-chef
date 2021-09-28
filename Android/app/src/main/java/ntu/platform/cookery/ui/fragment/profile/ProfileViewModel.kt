package ntu.platform.cookery.ui.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "profile Fragment WIP"
    }
    val text: LiveData<String> = _text
}