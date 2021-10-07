package ntu.platform.cookery.ui.fragment.add_recipe

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ntu.platform.cookery.R
import ntu.platform.cookery.databinding.FragmentAddRecipeIngredientInfoBinding
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.entity.Ingredient
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar
import java.lang.Exception


private const val TAG = "CY.frag.ingredient_info"
class AddRecipeIngredientInfoFragment : BindingFragment<FragmentAddRecipeIngredientInfoBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAddRecipeIngredientInfoBinding
        get() = FragmentAddRecipeIngredientInfoBinding::inflate

    private val _viewModel: AddRecipeViewModel by  activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initBinding()
        setHasOptionsMenu(true)
    }

    private fun initToolbar(){
        setToolBar(binding.toolbarLayout.toolbar)
        setTitle(R.string.title_add_Ingredient)
        setDisplayHomeAsUp(true)
    }


    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_save -> {
                save()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun save(){
        try{
            // TODO: validate data
            val name = _viewModel.ingredientName.value
            val amount  = _viewModel.ingredientAmount.value?.toInt()
            val unit = _viewModel.ingredientUnit.value

            _viewModel.ingredients.add(Ingredient(name!!, amount!!, unit?:""))
            findNavController().popBackStack()
        } catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        _viewModel.clearIngredientInfo()
        super.onDestroy()
    }


}