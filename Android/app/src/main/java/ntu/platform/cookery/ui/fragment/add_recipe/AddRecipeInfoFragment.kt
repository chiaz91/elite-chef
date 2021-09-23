package ntu.platform.cookery.ui.fragment.add_recipe

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import ntu.platform.cookery.R
import ntu.platform.cookery.databinding.FragmentAddRecipeInfoBinding
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.base.DIR_IMAGE_PICKER
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.data.firebase.FBStorageRepository
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar
import java.io.File


private const val TAG = "CY.Frag.AddRecipeInfo"
class AddRecipeInfoFragment : BindingFragment<FragmentAddRecipeInfoBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAddRecipeInfoBinding
        get() = FragmentAddRecipeInfoBinding::inflate


    private val args by navArgs<AddRecipeInfoFragmentArgs>()
    private val _viewModel: AddRecipeViewModel by  activityViewModels()

    private val imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data!!
                // TODO: change user_id after authenticate added
                val uid = _viewModel.user.value!!.uid
                FBStorageRepository.uploadRecipeGraphic(uid, fileUri).observe(viewLifecycleOwner,{
                    _viewModel.graphic.value = it.toString()
                })
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // reading arguments
        args.recipeId?.let{ recipeId ->
            Log.d(TAG, "arg.recipe_id = $recipeId")
            if (_viewModel.recipeId.value != recipeId){
                _viewModel.recipeId.value = recipeId

                loadRecipeById(recipeId)
            }
        }

        // set up toolbar
        initToolbar()
        initBinding()
    }

    private fun loadRecipeById(recipeId:String){
        with(FBDatabaseRepository){
            getRecipe(recipeId).observe(viewLifecycleOwner, {
                _viewModel.setUpRecipe(it)
            })
            getRecipeIngredients(recipeId).observe(viewLifecycleOwner, {
                _viewModel.setUpIngredients(it)
            })
            getRecipeSteps(recipeId).observe(viewLifecycleOwner, {
                _viewModel.setUpSteps(it)
            })
        }
    }

    private fun initToolbar(){
        setToolBar(binding.toolbarLayout.toolbar)
        when{
            args.recipeId!=null -> setTitle(R.string.title_edit_recipe)
            else -> setTitle(R.string.title_create_recipe)
        }

        setDisplayHomeAsUp(true)
        binding.toolbarLayout.progress.apply {
            progress = 1
            max = 4
        }
    }

    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel

            btnNext.setOnClickListener {
                if (_viewModel.graphic.value == null){
                    Toast.makeText(requireContext(),  getString(R.string.message_graphic_is_required), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val action = AddRecipeInfoFragmentDirections.actionAddRecipeInfoFragmentToAddRecipeTimingFragment()
                findNavController().navigate(action)
            }
            graphic.setOnClickListener {
                launchImagePicker()
            }
        }
    }


    private fun launchImagePicker(){
        ImagePicker.with(this)
            .cropSquare()
            .maxResultSize(1080,1080)
            .saveDir(File(requireContext().cacheDir, DIR_IMAGE_PICKER))
            .createIntent { intent-> imageResultLauncher.launch(intent) }
    }


    override fun onDestroy() {
        super.onDestroy()
        _viewModel.clear()
    }
}