package ntu.platform.cookery.ui.fragment.post_comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentPostCommentsBinding
import ntu.platform.cookery.util.setToolBar

private const val TAG = "Cy.post.cmt"
class PostCommentsFragment:  BindingFragment<FragmentPostCommentsBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentPostCommentsBinding
    get() = FragmentPostCommentsBinding::inflate

    private lateinit var _viewModel: PostCommentsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args by navArgs<PostCommentsFragmentArgs>()
        Log.d(TAG, "arg.post = ${args.post.key}:: ${args.post.message}")
        val viewModelFactory = PostCommentsViewModelFactory(args.post)
        _viewModel = ViewModelProvider(this, viewModelFactory).get(PostCommentsViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        observeViewModel()
    }

    private fun initBinding() {

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            setToolBar(toolbarLayout.toolbar)
            cardPost.item = _viewModel.post
            rvComments.adapter = _viewModel.commentAdapter

        }
    }

    private fun observeViewModel(){
//        _viewModel.text.observe(viewLifecycleOwner, {
//
//        })
    }


    override fun onStart() {
        super.onStart()
        _viewModel.commentAdapter.startListening()
    }

    override fun onStop() {
        _viewModel.commentAdapter.stopListening()
        super.onStop()
    }

}