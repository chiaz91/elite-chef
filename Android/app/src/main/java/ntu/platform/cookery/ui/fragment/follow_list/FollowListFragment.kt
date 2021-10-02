package ntu.platform.cookery.ui.fragment.follow_list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentFollowListBinding
import ntu.platform.cookery.ui.fragment.profile.ProfileOtherFragmentArgs
import ntu.platform.cookery.util.setToolBar

private const val TAG = "Cy.follow"
class FollowListFragment:  BindingFragment<FragmentFollowListBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentFollowListBinding
    get() = FragmentFollowListBinding::inflate

    private lateinit var _viewModel: FollowListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val args by navArgs<ProfileOtherFragmentArgs>()
        val vmFactory = FollowListViewModelFactory(args.userId)
        _viewModel = ViewModelProvider(this, vmFactory).get(FollowListViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initBinding()
        observeViewModel()

        setHasOptionsMenu(true)
    }


    private fun initBinding() {
        with(binding){
            setToolBar(binding.toolbarLayout.toolbar)
            rvUsers.adapter = _viewModel.adapter
        }
    }

    private fun observeViewModel(){
        with(_viewModel){
//            users.observe(viewLifecycleOwner, {
//                _viewModel.adapter.items = it
//                _viewModel.adapter.notifyDataSetChanged()
//            })

            onUserClicked.observe(viewLifecycleOwner, {
                Log.d(TAG, "key=${it.uid}, name=${it.name}")

                val action = FollowListFragmentDirections.actionFollowListFragmentToProfileOtherFragment(it.uid!!)
                findNavController().navigate(action)
            })


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