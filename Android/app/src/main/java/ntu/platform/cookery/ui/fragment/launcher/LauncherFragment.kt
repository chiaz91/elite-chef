package ntu.platform.cookery.ui.fragment.launcher

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentSplashBinding
import ntu.platform.cookery.ui.MainActivity

class LauncherFragment : BindingFragment<FragmentSplashBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    private val _viewModel: LauncherViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner


        // subscribe changes in firebase, decide direct to login or main activity
    }

    override fun onStart() {
        super.onStart()

        // direct to main activity
        binding.root.postDelayed({
            toMainActivity();
        }, 1000)
    }



    private fun toMainActivity(){
        val intent = Intent(requireContext(), MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


}