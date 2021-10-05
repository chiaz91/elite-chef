package ntu.platform.cookery.ui.fragment.follow_list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentFollowListBinding
import ntu.platform.cookery.ui.fragment.profile.ProfileOtherFragmentArgs
import ntu.platform.cookery.util.setTitle
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
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            setToolBar(toolbar)
            rvUsers.adapter = _viewModel.followingAdapter

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.text){
                        getString(R.string.following) -> {
                            setTitle( tab.text.toString() )
                            rvUsers.adapter = _viewModel.followingAdapter
                        }
                        getString(R.string.follower) -> {
                            setTitle( tab.text.toString() )
                            rvUsers.adapter = _viewModel.followerAdapter
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }

    private fun observeViewModel(){
        with(_viewModel){
            onUserClicked.observe(viewLifecycleOwner, {
                Log.d(TAG, "key=${it.uid}, name=${it.name}")

                val action = FollowListFragmentDirections.actionFollowListFragmentToProfileOtherFragment(it.uid!!)
                findNavController().navigate(action)
            })
        }
    }



    override fun onStart() {
        super.onStart()
        _viewModel.followingAdapter.startListening()
        _viewModel.followerAdapter.startListening()
    }

    override fun onStop() {
        _viewModel.followingAdapter.stopListening()
        _viewModel.followerAdapter.stopListening()
        super.onStop()
    }
}