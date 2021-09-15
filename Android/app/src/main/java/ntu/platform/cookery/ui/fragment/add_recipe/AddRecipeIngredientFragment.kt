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
import ntu.platform.cookery.databinding.FragmentAddRecipeIngredientBinding
import ntu.platform.cookery.ui.adapter.RecipeIngredientAdapter
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.entity.Ingredient
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar

private const val TAG = "CY.Frag.AddIngredient"
class AddRecipeIngredientFragment : BindingFragment<FragmentAddRecipeIngredientBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAddRecipeIngredientBinding
        get() = FragmentAddRecipeIngredientBinding::inflate

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
            progress = 3
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
                val action = AddRecipeIngredientFragmentDirections.actionAddRecipeIngredientFragmentToAddRecipeStepsFragment()
                findNavController().navigate(action)
            }

            rvIngredient.apply {
                this.adapter = RecipeIngredientAdapter().also {
                    it.allowEdit = true
                    it.clickedListener = BaseClickedListener { action, viewHolder ->
                        Log.d(TAG, "action: $action")

                        when (action){
                            RecipeIngredientAdapter.ACTION_ADD_MORE_CLICK -> toAddIngredient()
                            RecipeIngredientAdapter.ACTION_ITEM_DELETE -> {
                                val pos = viewHolder.bindingAdapterPosition
                                val ingredient = _viewModel.ingredients[pos]
                                showDeleteIngredientDialog(ingredient)
                            }
                        }
                    }
                }
            }

        }
    }

    private fun toAddIngredient(){
        val action = AddRecipeIngredientFragmentDirections.actionAddRecipeIngredientFragmentToAddRecipeIngredientInfoFragment()
        findNavController().navigate(action)
    }

    private fun showDeleteIngredientDialog(ingredient: Ingredient){
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.deletion))
            .setMessage(getString(R.string.warn_delete_ingredient_message))
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                _viewModel.ingredients.remove(ingredient)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }


}