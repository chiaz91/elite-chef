package ntu.platform.cookery.ui.fragment.add_recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ntu.platform.cookery.databinding.FragmentAddRecipeStepsBinding
import ntu.platform.cookery.ui.adapter.RecipeStepAdapter
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar


private const val TAG = "CY.Frag.AddStep"
class AddRecipeStepFragment : BindingFragment<FragmentAddRecipeStepsBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAddRecipeStepsBinding
        get() = FragmentAddRecipeStepsBinding::inflate

    private val _viewModel: AddRecipeViewModel by  activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        initBinding()
    }

    private fun initToolbar(){
        setToolBar(binding.toolbarLayout.toolbar)
        setTitle("Create Recipe")
        setDisplayHomeAsUp(true)
        binding.toolbarLayout.progress.apply {
            progress = 3
            max = 3
        }
    }

    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel

            btnPrev.setOnClickListener { findNavController().popBackStack() }
            btnSave.setOnClickListener { doSave() }
            rvSteps.apply {
                this.adapter = RecipeStepAdapter{ action, holder ->
                    Log.d(TAG, "action: $action")


                    if (action == RecipeStepAdapter.ACTION_ADD_MORE_CLICK){
                        val action = AddRecipeStepFragmentDirections.actionAddRecipeStepsFragmentToAddRecipeStepInfoFragment()
                        findNavController().navigate(action)
                        Log.d(TAG, "vm.count == ${_viewModel.steps.value?.size}, adapter.count == ${adapter?.itemCount}")
                    }
                }

            }
        }
    }

    private fun doSave(){
        _viewModel.saveRecipe()

        val action = AddRecipeStepFragmentDirections.actionAddRecipeStepsFragmentToMainFragment()
        findNavController().navigate(action)
    }

}