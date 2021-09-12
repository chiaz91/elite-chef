package ntu.platform.cookery.ui.fragment.recipe_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import ntu.platform.cookery.databinding.FragmentMainBinding
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentRecipeDetailsBinding
import ntu.platform.cookery.ui.fragment.recipe_list.RecipeListFragmentDirections
import ntu.platform.cookery.ui.fragment.recipe_list.RecipeListViewModel
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar


private const val TAG = "Cy.Frag.RecipeDetail"
class RecipeDetailsFragment: BindingFragment<FragmentRecipeDetailsBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentRecipeDetailsBinding
        get() = FragmentRecipeDetailsBinding::inflate

    private lateinit var _viewModel: RecipeDetailsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar(binding.toolbar)
        setDisplayHomeAsUp(true)


        // getting id, and create retrieve viewmodel
        val args by navArgs<RecipeDetailsFragmentArgs>()
        Log.d(TAG, "arg.recipe_id = ${args.recipeId}")
        val viewModelFactory = RecipeDetailsViewModelFactory(args.recipeId)
        _viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeDetailsViewModel::class.java)


        initBinding()
        observeViewModel()
    }

    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            rvIngredient.adapter = _viewModel.ingredientsAdapter
            rvSteps.adapter = _viewModel.stepsAdapter
        }
    }

    private fun observeViewModel(){
        _viewModel.ingredients.observe(viewLifecycleOwner, {
            _viewModel.ingredientsAdapter.items = it
            _viewModel.ingredientsAdapter.notifyDataSetChanged()

            // TODO: remove below
            Log.d(TAG, "ingredients.size = ${it.size}")
            it.forEach{ingredient ->
                Log.d(TAG, ingredient.toString())
            }
        })

        _viewModel.steps.observe(viewLifecycleOwner, {
            _viewModel.stepsAdapter.items = it
            _viewModel.stepsAdapter.notifyDataSetChanged()

            // TODO: remove below
            Log.d(TAG, "steps.size = ${it.size}")
            it.forEach{ step->
                Log.d(TAG, step.toString())
            }
        })
    }

//    override fun onStart() {
//        super.onStart()
//        _viewModel.adapter.startListening()
//    }
//
//    override fun onStop() {
//        _viewModel.adapter.stopListening()
//        super.onStop()
//    }



}