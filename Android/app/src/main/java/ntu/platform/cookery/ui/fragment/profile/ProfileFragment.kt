package ntu.platform.cookery.ui.fragment.profile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.databinding.FragmentProfileBinding
import ntu.platform.cookery.util.hashMD5
import ntu.platform.cookery.util.setToolBar

private const val TAG = "Cy.profile"
class ProfileFragment:  BindingFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentProfileBinding
    get() = FragmentProfileBinding::inflate

    private lateinit var _viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vmFactory = ProfileViewModelFactory()
        _viewModel = ViewModelProvider(this, vmFactory).get(ProfileViewModel::class.java)

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
                val action = ProfileFragmentDirections.actionProfileToFollowListFragment(_viewModel.userId)
                findNavController().navigate(action)
            }
            follower.setOnClickListener{
                val action = ProfileFragmentDirections.actionProfileToFollowListFragment(_viewModel.userId)
                findNavController().navigate(action)
            }

            rvPosts.adapter = _viewModel.postAdapter
            rvRecipes.adapter = _viewModel.recipeAdapter


            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    Log.d(TAG, "onTabSelected::id=${tab?.id}, content=${tab?.text}")
                    when (tab?.text) {
                        getString(R.string.posts) -> {
                            rvPosts.visibility = View.VISIBLE
                            rvRecipes.visibility = View.GONE
                        }
                        getString(R.string.recipes) -> {
                            rvPosts.visibility = View.GONE
                            rvRecipes.visibility = View.VISIBLE
                            rvRecipes.adapter = _viewModel.recipeAdapter
                        }
                        getString(R.string.liked_recipe) -> {
                            rvPosts.visibility = View.GONE
                            rvRecipes.visibility = View.VISIBLE
                            rvRecipes.adapter = _viewModel.recipeLikedAdapter
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
                val action = ProfileFragmentDirections.actionProfileToPostCommentsFragment(it)
                findNavController().navigate(action)
            })

            onRecipeClicked.observe(viewLifecycleOwner, {
                val action = ProfileFragmentDirections.actionProfileToRecipeDetailsFragment(it.key!!)
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
            recipeLikedAdapter.startListening()
        }
    }

    override fun onStop() {
        with(_viewModel){
            postAdapter.stopListening()
            recipeAdapter.stopListening()
            recipeLikedAdapter.stopListening()
        }
        super.onStop()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.profile_other, menu)
        when {
            _viewModel.isCurrentUser -> inflater.inflate(R.menu.profile, menu)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_edit -> {
                val action = ProfileFragmentDirections.actionProfileToProfileEditFragment(_viewModel.user.value!!)
                findNavController().navigate(action)
                true
            }

            R.id.action_theme -> {
                chooseThemeDialog()
                true
            }

            R.id.action_logout -> {
                FBAuthRepository.logout()
                true
            }

            R.id.action_chat -> {
                toChatList()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun toChatList(){
        val action = ProfileFragmentDirections.actionProfileToChatListFragment()
        findNavController().navigate(action)
    }

    private fun chooseThemeDialog() {
        val styles = resources.getStringArray(R.array.theme_color)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.choose_theme))
            .setSingleChoiceItems(styles, 0) { dialog, which ->
                when (which) {
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

}