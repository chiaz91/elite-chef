package ntu.platform.cookery.ui.fragment.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.databinding.FragmentSplashBinding
import ntu.platform.cookery.ui.MainActivity

private const val TAG = "Cy.Launcher"
private const val DELAY_MS = 1000L
class LauncherFragment : BindingFragment<FragmentSplashBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    private val _viewModel: LauncherViewModel by viewModels()

    private val authResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        val response = IdpResponse.fromResultIntent(data)
        if (resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            Log.i(TAG, "isNewUser= ${response?.isNewUser}!")
            if (response?.isNewUser == true){
                val user = FBAuthRepository.getUser()!!
                val newUser = ECUser(
                    name = user.displayName,
                    bio ="",
                    graphic =user.photoUrl?.toString(),
                    uid =user.uid
                )
                FBDatabaseRepository.saveUser(newUser)
            }

        } else {
            Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner


        // subscribe changes in firebase, decide direct to login or main activity
        observeAuthenticationState()
    }


    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val intentSignIn = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_launcher_foreground) // Set logo drawable
            .setTheme(R.style.Theme_EC) // Set theme
            .setAlwaysShowSignInMethodScreen(true)
            .setIsSmartLockEnabled(false)
            .build()
        authResultLauncher.launch(intentSignIn)
    }

    private fun observeAuthenticationState() {
        _viewModel.authenticationState.observe(viewLifecycleOwner, { authenticationState ->
            when (authenticationState) {
                LauncherViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.root.postDelayed({
                        toMainActivity()
                    }, DELAY_MS)
                }
                else -> {Log.i(TAG, "user not authenticated.")
                    binding.root.postDelayed({
                        launchSignInFlow()
                    }, DELAY_MS)
                }
            }
        })
    }



    private fun toMainActivity(){
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }


}