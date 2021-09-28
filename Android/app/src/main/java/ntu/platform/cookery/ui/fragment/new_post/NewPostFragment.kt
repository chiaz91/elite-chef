package ntu.platform.cookery.ui.fragment.new_post

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.base.DIR_IMAGE_PICKER
import ntu.platform.cookery.data.firebase.FBStorageRepository
import ntu.platform.cookery.databinding.FragmentNewPostBinding
import ntu.platform.cookery.util.setToolBar
import java.io.File

private const val TAG = "Cy.posts.new";
class NewPostFragment : BindingFragment<FragmentNewPostBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentNewPostBinding
        get() = FragmentNewPostBinding::inflate

    private val _viewModel: NewPostViewModel by viewModels()

    private val imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data!!
                val uid = _viewModel.user.value!!.uid!!
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

        setToolBar(binding.toolbarLayout.toolbar)

        initBinding()
        observeViewModel()

        setHasOptionsMenu(true)
    }

    private fun initBinding(){
        with (binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            btnCross.setOnClickListener{
                _viewModel.graphic.value = null
            }

            btnCamera.setOnClickListener{
                ImagePicker.with(this@NewPostFragment)
                        .cropSquare()
                        .cameraOnly()
                        .maxResultSize(1080,1080)
                        .saveDir(File(requireContext().cacheDir, DIR_IMAGE_PICKER))
                        .createIntent { intent -> imageResultLauncher.launch(intent) }
            }

            btnGallery.setOnClickListener{
                ImagePicker.with(this@NewPostFragment)
                        .cropSquare()
                        .galleryOnly()
                        .maxResultSize(1080,1080)
                        .saveDir(File(requireContext().cacheDir, DIR_IMAGE_PICKER))
                        .createIntent { intent -> imageResultLauncher.launch(intent) }
            }
        }

    }

    private fun observeViewModel(){
        _viewModel.user.observe(viewLifecycleOwner, {
            binding.greeting.text = "Hello ${it.name},"
        })
    }


    // options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.post, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_post -> {
                _viewModel.save()
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}