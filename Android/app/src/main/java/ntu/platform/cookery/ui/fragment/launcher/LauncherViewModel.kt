package ntu.platform.cookery.ui.fragment.launcher

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ntu.platform.cookery.data.firebase.FBAuthRepository

private const val TAG = "Cy.Launcher"
class LauncherViewModel : ViewModel() {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val user = FBAuthRepository.userLiveData

    val authenticationState = user.map { user ->
        if (user != null) {
            Log.d(TAG, "user is "+user.displayName)
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}