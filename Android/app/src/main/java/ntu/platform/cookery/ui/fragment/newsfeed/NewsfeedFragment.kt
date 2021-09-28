package ntu.platform.cookery.ui.fragment.newsfeed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.entity.Post
import ntu.platform.cookery.databinding.FragmentNewsfeedBinding
import ntu.platform.cookery.util.setToolBar

private const val TAG = "Cy.newsfeed"
class NewsfeedFragment : BindingFragment<FragmentNewsfeedBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentNewsfeedBinding
        get() = FragmentNewsfeedBinding::inflate

    private  val _viewModel: NewsfeedViewModel  by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar(binding.toolbarLayout.toolbar)
        initBinding()
        observeViewModel()
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

    private fun observeViewModel(){
        with(_viewModel){

            itemClick.observe(viewLifecycleOwner, {
                toPostComments(it)
            })
        }
    }

    private fun toPostComments(post: Post){
        val action = NewsfeedFragmentDirections.actionNewsfeedListToPostCommentsFragment(post)
        findNavController().navigate(action)
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