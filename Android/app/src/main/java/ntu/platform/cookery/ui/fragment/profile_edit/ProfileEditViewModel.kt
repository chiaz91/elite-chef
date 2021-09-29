package ntu.platform.cookery.ui.fragment.profile_edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.firebase.FBDatabaseRepository

private const val TAG = "Cy.profile.edit.vm"
class ProfileEditViewModel(val user: ECUser) : ViewModel() {
    val graphic = MutableLiveData<String?>()
    val name = MutableLiveData<String?>()
    val bio  = MutableLiveData<String?>()
    val onSaved = SingleLiveEvent<String>()
    private val _text = MutableLiveData<String>().apply {
        value = "profile Fragment WIP"
    }
    val text: LiveData<String> = _text

    init {
        Log.d(TAG, "init livedata with user")
        Log.d(TAG, "user.uid ${user.uid}")
        graphic.value = user.graphic
        name.value = user.name
        bio.value = user.bio
    }

    fun onSave(){
        name.value?.isNotBlank().let {
            user.name = name.value
        }
        bio.value?.isNotBlank().let {
            user.bio = bio.value
        }
        graphic.value?.isNotBlank().let {
            user.graphic = graphic.value
        }

        FBDatabaseRepository.saveUser(user)
        onSaved.value = "user particular updated"
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "VM onCleared")
    }
}