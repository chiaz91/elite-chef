package ntu.platform.cookery.data.firebase

import android.content.Context
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

// firebase realtime database

private const val TAG = "Cy.FB.Database"
private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
object FBAuthRepository{
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userLiveData = FirebaseUserLiveData()

    fun getUser() = auth.currentUser

    fun logout() {
        auth.signOut()
    }

    fun logout(context: Context, callback: (()->Unit)? = null) {
        AuthUI.getInstance().signOut(context)
            .addOnCompleteListener {
                callback?.invoke()
                Log.d(TAG, "logout")
            }
    }

}