package ntu.platform.cookery.ui.fragment.newsfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsfeedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is newsfeed Fragment"
    }
    val text: LiveData<String> = _text
}