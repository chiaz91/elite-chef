package ntu.platform.cookery.ui.fragment.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentNewsfeedBinding
import ntu.platform.cookery.util.setToolBar

class NewsfeedFragment : BindingFragment<FragmentNewsfeedBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentNewsfeedBinding
        get() = FragmentNewsfeedBinding::inflate

    private  val _viewModel: NewsfeedViewModel  by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar(binding.toolbarLayout.toolbar)
        initBinding()
    }

    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel

            fabAdd.setOnClickListener{
                val action = NewsfeedFragmentDirections.actionNewsfeedListToNewPostFragment()
                findNavController().navigate(action)
            }

            rvPosts.adapter = _viewModel.adapter
        }

    }

    override fun onStart() {
        super.onStart()
        _viewModel.adapter.startListening()
    }

    override fun onStop() {
        _viewModel.adapter.stopListening()
        super.onStop()
    }


}