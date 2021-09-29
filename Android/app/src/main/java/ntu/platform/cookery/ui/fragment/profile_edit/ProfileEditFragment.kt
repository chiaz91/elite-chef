package ntu.platform.cookery.ui.fragment.profile_edit

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.base.DIR_IMAGE_PICKER
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.data.firebase.FBStorageRepository
import ntu.platform.cookery.databinding.FragmentProfileEditBinding
import ntu.platform.cookery.util.setToolBar
import java.io.File

private const val TAG = "Cy.profile.edit"
class ProfileEditFragment:  BindingFragment<FragmentProfileEditBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentProfileEditBinding
    get() = FragmentProfileEditBinding::inflate

    private lateinit var _viewModel: ProfileEditViewModel

    private val imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data!!
                val uid = _viewModel.user.uid!!
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args by navArgs<ProfileEditFragmentArgs>()
        Log.d(TAG, "arg.user = ${args.user.uid}")
        val vmFactory = ProfileEditViewModelFactory(args.user)
        _viewModel = ViewModelProvider(this, vmFactory).get(ProfileEditViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initBinding()
        observeViewModel()

        setHasOptionsMenu(true)
    }



    private fun initBinding() {
        with(binding){

            setToolBar(toolbarLayout.toolbar)
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            profilePic.setOnClickListener{
                ImagePicker.with(this@ProfileEditFragment)
                    .cropSquare()
                    .maxResultSize(1080,1080)
                    .saveDir(File(requireContext().cacheDir, DIR_IMAGE_PICKER))
                    .createIntent { intent -> imageResultLauncher.launch(intent) }
            }
        }
    }

    private fun observeViewModel(){
//        _viewModel.text.observe(viewLifecycleOwner, {
//            binding.text.text = it
//        })

        with (_viewModel){
            onSaved.observe(viewLifecycleOwner, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            })
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_logout -> {
                FBAuthRepository.logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}