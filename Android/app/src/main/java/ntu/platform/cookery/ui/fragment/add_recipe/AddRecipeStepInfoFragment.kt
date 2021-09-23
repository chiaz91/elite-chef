package ntu.platform.cookery.ui.fragment.add_recipe

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.base.DIR_IMAGE_PICKER
import ntu.platform.cookery.databinding.FragmentAddRecipeStepsInfoBinding
import ntu.platform.cookery.data.firebase.FBStorageRepository
import ntu.platform.cookery.util.loadWithGlide
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setTitle
import ntu.platform.cookery.util.setToolBar
import java.io.File
import java.lang.Exception


private const val TAG = "CY.frag.ingredient_info"
class AddRecipeStepInfoFragment : BindingFragment<FragmentAddRecipeStepsInfoBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAddRecipeStepsInfoBinding
        get() = FragmentAddRecipeStepsInfoBinding::inflate

    private val _viewModel: AddRecipeViewModel by  activityViewModels()

    private val imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data!!
                val uid = _viewModel.user.value!!.uid
                FBStorageRepository.uploadRecipeGraphic(uid, fileUri).observe(viewLifecycleOwner,{
                    _viewModel.stepGraphic.value = it.toString()
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

        initToolbar()
        setHasOptionsMenu(true)

        initBinding()
        observeViewModel()
    }

    private fun initToolbar(){
        setToolBar(binding.toolbarLayout.toolbar)
        setTitle(R.string.title_add_step)
        setDisplayHomeAsUp(true)
    }

    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            graphic.setOnClickListener {
                launchImagePicker()
            }
        }
    }

    private fun observeViewModel(){
        _viewModel.stepGraphic.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.graphic.loadWithGlide(it)
            }
        })
    }

    private fun launchImagePicker(){
        ImagePicker.with(this)
            .cropSquare()
            .maxResultSize(1080,1080)
            .saveDir(File(requireContext().cacheDir, DIR_IMAGE_PICKER))
            .createIntent { intent-> imageResultLauncher.launch(intent) }
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
            _viewModel.addStep()
            findNavController().popBackStack()
        } catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        _viewModel.clearStepInfo()
        super.onDestroy()
    }


}