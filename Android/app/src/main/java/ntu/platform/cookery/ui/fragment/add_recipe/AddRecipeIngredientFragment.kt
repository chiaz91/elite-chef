package ntu.platform.cookery.ui.fragment.add_recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.databinding.FragmentAddRecipeIngredientBinding
import ntu.platform.cookery.ui.adapter.RecipeIngredientAdapter
import ntu.platform.cookery.base.BindingFragment
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
        setTitle("Create Recipe")
        setDisplayHomeAsUp(true)
        binding.toolbarLayout.progress.apply {
            progress = 2
            max = 3
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

                        if (action == RecipeIngredientAdapter.ACTION_ADD_MORE_CLICK){
                            val action = AddRecipeIngredientFragmentDirections.actionAddRecipeIngredientFragmentToAddRecipeIngredientInfoFragment()
                            findNavController().navigate(action)
                            Log.d(TAG, "vm.count == ${_viewModel.ingredients.value?.size}, adapter.count == ${adapter?.itemCount}")
                        }
                    }
                }
            }

        }
    }


}