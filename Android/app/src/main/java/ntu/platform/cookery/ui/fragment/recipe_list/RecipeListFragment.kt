package ntu.platform.cookery.ui.fragment.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ntu.platform.cookery.databinding.FragmentMainBinding
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar


class RecipeListFragment: BindingFragment<FragmentMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    private val _viewModel: RecipeListViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar(binding.toolbarLayout.toolbar)
        setTitle("Recipes")

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = _viewModel
        binding.rvRecipes.adapter = _viewModel.adapter

        binding.fabAddRecipe.setOnClickListener{
            val action = RecipeListFragmentDirections.actionMainFragmentToAddRecipeInfoFragment(null)
            findNavController().navigate(action)
        }

        _viewModel.recipeClicked.observe(viewLifecycleOwner, { recipeID ->
            val action = RecipeListFragmentDirections.actionMainFragmentToRecipeDetailsFragment(recipeID)
            findNavController().navigate(action)
        })
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