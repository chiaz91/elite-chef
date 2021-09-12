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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import ntu.platform.cookery.databinding.FragmentAddRecipeInfoBinding
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.firebase.FBStorageRepository
import ntu.platform.cookery.util.loadWithGlide
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar
import java.io.File


private const val TAG = "CY.Frag.AddRecipeInfo"
class AddRecipeInfoFragment : BindingFragment<FragmentAddRecipeInfoBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAddRecipeInfoBinding
        get() = FragmentAddRecipeInfoBinding::inflate

    private val _viewModel: AddRecipeViewModel by  activityViewModels()

    private val imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data!!
                // TODO: change user_id after authenticate added
                FBStorageRepository.uploadRecipeGraphic("tester", fileUri).observe(viewLifecycleOwner,{
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

        // set up toolbar
        initToolbar()
        initBinding()
    }

    private fun initToolbar(){
        setToolBar(binding.toolbarLayout.toolbar)
        setTitle("Create Recipe")
        setDisplayHomeAsUp(true)
        binding.toolbarLayout.progress.apply {
            progress = 1
            max = 3
        }
    }

    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel

            btnNext.setOnClickListener {
                if (_viewModel.graphic.value == null){
                    Toast.makeText(requireContext(),  "Graphic is required!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val action = AddRecipeInfoFragmentDirections.actionAddRecipeInfoFragmentToAddRecipeIngredientFragment()
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
            .saveDir(File(requireContext().cacheDir, "ImagePicker"))
            .createIntent { intent-> imageResultLauncher.launch(intent) }
    }


    override fun onDestroy() {
        super.onDestroy()
        _viewModel.clear()
    }
}