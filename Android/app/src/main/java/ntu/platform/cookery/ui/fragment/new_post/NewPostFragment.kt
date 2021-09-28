package ntu.platform.cookery.ui.fragment.new_post

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
import ntu.platform.cookery.ui.fragment.newsfeed.NewsfeedViewModel
import ntu.platform.cookery.util.setToolBar

class NewPostFragment : BindingFragment<FragmentNewPostBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentNewPostBinding
        get() = FragmentNewPostBinding::inflate

    private val _viewModel: NewsfeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar(binding.toolbarLayout.toolbar)
        initBinding()
    }

    private fun initBinding(){
        binding.btnCross.setOnClickListener{
            Toast.makeText(requireContext(),"cross", Toast.LENGTH_SHORT).show()
        }

        binding.btnCamera.setOnClickListener{
            Toast.makeText(requireContext(),"camera", Toast.LENGTH_SHORT).show()
        }

        binding.btnGallery.setOnClickListener{
            Toast.makeText(requireContext(),"gallery", Toast.LENGTH_SHORT).show()
        }
    }

}