package ntu.platform.cookery.ui.fragment.add_recipe

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.ui.adapter.RecipeIngredientAdapter
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.entity.Ingredient
import ntu.platform.cookery.databinding.FragmentAddRecipeTimingBinding
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar

private const val TAG = "CY.Frag.Timing"
class AddRecipeTimingFragment : BindingFragment<FragmentAddRecipeTimingBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAddRecipeTimingBinding
        get() = FragmentAddRecipeTimingBinding::inflate

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
            progress = 2
            max = 4
        }
    }


    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel

            btnPrev.setOnClickListener {
                findNavController().popBackStack()
            }
            btnNext.setOnClickListener {
                val action = AddRecipeTimingFragmentDirections.actionAddRecipeTimingFragmentToAddRecipeIngredientFragment()
                findNavController().navigate(action)
            }

        }
    }



}