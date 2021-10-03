package ntu.platform.cookery.ui.fragment.profile

import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.databinding.FragmentProfileBinding
import ntu.platform.cookery.util.setToolBar

private const val TAG = "Cy.profile"
class ProfileOtherFragment:  BindingFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentProfileBinding
    get() = FragmentProfileBinding::inflate

    private lateinit var _viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val args by navArgs<ProfileOtherFragmentArgs>()
        val vmFactory = ProfileViewModelFactory(args.userId)
        _viewModel = ViewModelProvider(this, vmFactory).get(ProfileViewModel::class.java)
        if (_viewModel.isCurrentUser){
            val action = ProfileOtherFragmentDirections.actionProfileOtherFragmentToProfile()
            findNavController().navigate(action)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        observeViewModel()
        setHasOptionsMenu(true)
    }



    private fun initBinding() {
        with(binding){

            setToolBar(toolbar)
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel

            following.setOnClickListener{
                val action = ProfileOtherFragmentDirections.actionProfileOtherFragmentToFollowListFragment(_viewModel.userId)
                findNavController().navigate(action)
            }
            follower.setOnClickListener{
                val action = ProfileOtherFragmentDirections.actionProfileOtherFragmentToFollowListFragment(_viewModel.userId)
                findNavController().navigate(action)
            }
            rvPosts.adapter = _viewModel.postAdapter
            rvRecipes.adapter = _viewModel.recipeAdapter


            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    Log.d(TAG, "onTabSelected::id=${tab?.id}, content=${tab?.text}")
                    when (tab?.text) {
                        "Posts" -> {
                            rvPosts.visibility = View.VISIBLE
                            rvRecipes.visibility = View.GONE
                        }
                        "Recipes" -> {
                            rvPosts.visibility = View.GONE
                            rvRecipes.visibility = View.VISIBLE
                        }
                        else -> Log.d(TAG, "Something wrong, text=${tab?.text}")
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Handle tab unselect
                }
            })


        }
    }

    private fun observeViewModel(){
        with(_viewModel){
            onPostClicked.observe(viewLifecycleOwner, {
                val action = ProfileOtherFragmentDirections.actionProfileOtherFragmentToPostCommentsFragment(it)
                findNavController().navigate(action)
            })

            onRecipeClicked.observe(viewLifecycleOwner, {
                val action = ProfileOtherFragmentDirections.actionProfileOtherFragmentToRecipeDetailsFragment(it.key!!)
                findNavController().navigate(action)
            })
        }
    }



    // Life Cycle
    override fun onStart() {
        super.onStart()
        with(_viewModel){
            postAdapter.startListening()
            recipeAdapter.startListening()
        }
    }

    override fun onStop() {
        with(_viewModel){
            postAdapter.stopListening()
            recipeAdapter.stopListening()
        }
        super.onStop()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        when {
            _viewModel.isCurrentUser -> inflater.inflate(R.menu.profile, menu)
            else -> inflater.inflate(R.menu.profile_other, menu)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_chat -> {
//                Toast.makeText(requireContext(), "WIP: chat with this user", Toast.LENGTH_SHORT).show()
                toChat()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    fun toChat(){
        // putting friend's id
        val otherUser = _viewModel.user.value!!
        val chatId = FBDatabaseRepository.getChatIdByUserIds(otherUser.uid!!, FBAuthRepository.getUser()!!.uid)
        val action = ProfileOtherFragmentDirections.actionProfileOtherFragmentToChatFragment(chatId)
        findNavController().navigate(action)
    }
}