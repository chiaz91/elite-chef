package ntu.platform.cookery.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentNewPostBinding
import ntu.platform.cookery.databinding.FragmentProfileBinding
import ntu.platform.cookery.ui.fragment.newsfeed.NewsfeedViewModel
import ntu.platform.cookery.util.setToolBar

class ProfileFragment:  BindingFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentProfileBinding
    get() = FragmentProfileBinding::inflate

    private val _viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initBinding()
        observeViewModel()
    }

    private fun initBinding() {
        setToolBar(binding.toolbarLayout.toolbar)
    }

    private fun observeViewModel(){
        _viewModel.text.observe(viewLifecycleOwner, {
            binding.text.text = it
        })
    }
}