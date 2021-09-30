package ntu.platform.cookery.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.databinding.FragmentProfileBinding
import ntu.platform.cookery.util.setToolBar

private const val TAG = "Cy.profile"
class ProfileFragment:  BindingFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentProfileBinding
    get() = FragmentProfileBinding::inflate

    private val _viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initBinding()
        observeViewModel()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_edit -> {
                val action = ProfileFragmentDirections.actionProfileToProfileEditFragment(_viewModel.user.value!!)
                findNavController().navigate(action)
                true
            }

            R.id.action_logout -> {
                FBAuthRepository.logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initBinding() {
        with(binding){

            setToolBar(toolbar)
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
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
                val action = ProfileFragmentDirections.actionProfileToPostCommentsFragment(it)
                findNavController().navigate(action)
            })

            onRecipeClicked.observe(viewLifecycleOwner, {
                val action = ProfileFragmentDirections.actionProfileToRecipeDetailsFragment(it.key!!)
                findNavController().navigate(action)
            })
        }
    }


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
}