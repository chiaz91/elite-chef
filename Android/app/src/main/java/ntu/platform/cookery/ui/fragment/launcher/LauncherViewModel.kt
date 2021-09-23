package ntu.platform.cookery.ui.fragment.launcher

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseUser
import ntu.platform.cookery.data.firebase.FBAuthRepository

private const val TAG = "Cy.Launcher.VM"
class LauncherViewModel : ViewModel() {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val user = FBAuthRepository.userLiveData

    val authenticationState = user.map { user ->
        if (user != null) {
            logUser(user)

            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    private fun logUser(user: FirebaseUser){
        Log.d(TAG, "user info")
        Log.d(TAG, "id="+user.uid)
        Log.d(TAG, "name="+user.displayName)
        Log.d(TAG, "metadata="+user.metadata.toString())
        Log.d(TAG, "isAnonymous="+user.isAnonymous)
        val provider = user.providerData
        provider.forEach{
            Log.d(TAG, "provider="+it)
        }
    }
}