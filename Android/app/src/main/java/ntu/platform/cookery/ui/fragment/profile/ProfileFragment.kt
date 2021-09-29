package ntu.platform.cookery.ui.fragment.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.databinding.FragmentProfileBinding
import ntu.platform.cookery.ui.fragment.recipe_details.RecipeDetailsFragmentDirections
import ntu.platform.cookery.util.setToolBar

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

            setToolBar(toolbarLayout.toolbar)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = _viewModel
        }
    }

    private fun observeViewModel(){
//        _viewModel.text.observe(viewLifecycleOwner, {
//            binding.text.text = it
//        })
    }
}