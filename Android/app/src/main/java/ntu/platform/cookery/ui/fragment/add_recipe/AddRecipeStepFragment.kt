package ntu.platform.cookery.ui.fragment.add_recipe

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ntu.platform.cookery.R
import ntu.platform.cookery.databinding.FragmentAddRecipeStepsBinding
import ntu.platform.cookery.ui.adapter.RecipeStepAdapter
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.entity.RecipeStep
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
        when{
            _viewModel.recipeId.value!=null -> setTitle(R.string.title_edit_recipe)
            else -> setTitle(R.string.title_create_recipe)
        }
        setDisplayHomeAsUp(true)
        binding.toolbarLayout.progress.apply {
            progress = 4
            max = 4
        }
    }

    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel

            btnPrev.setOnClickListener { findNavController().popBackStack() }
            btnSave.setOnClickListener { doSave() }
            rvSteps.apply {
                this.adapter = RecipeStepAdapter{action, viewHolder ->
                    Log.d(TAG, "action: $action")


                    when (action){
                        RecipeStepAdapter.ACTION_ADD_MORE_CLICK-> toAddStep()
                        RecipeStepAdapter.ACTION_ITEM_DELETE -> {
                            val pos = viewHolder.bindingAdapterPosition
                            val step = _viewModel.steps[pos]
                            showDeleteStepDialog(step)
                        }
                    }

                }
            }

        }

    }

    private fun toAddStep(){
        val action = AddRecipeStepFragmentDirections.actionAddRecipeStepsFragmentToAddRecipeStepInfoFragment()
        findNavController().navigate(action)
    }

    private fun showDeleteStepDialog(step: RecipeStep){
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.deletion))
            .setMessage(getString(R.string.warn_delete_step_message))
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                _viewModel.steps.remove(step)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }



    private fun doSave(){
        _viewModel.saveRecipe()


        val action = AddRecipeStepFragmentDirections.actionAddRecipeStepsFragmentToRecipeListFragment()
        findNavController().navigate(action)
    }

}