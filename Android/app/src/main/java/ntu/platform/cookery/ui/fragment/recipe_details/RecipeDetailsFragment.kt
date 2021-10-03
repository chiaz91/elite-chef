package ntu.platform.cookery.ui.fragment.recipe_details

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.data.entity.ECUser
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.databinding.FragmentRecipeDetailsBinding
import ntu.platform.cookery.util.log
import ntu.platform.cookery.util.setDisplayHomeAsUp
import ntu.platform.cookery.util.setToolBar


private const val TAG = "Cy.Frag.RecipeDetail"
class RecipeDetailsFragment: BindingFragment<FragmentRecipeDetailsBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentRecipeDetailsBinding
        get() = FragmentRecipeDetailsBinding::inflate

    private lateinit var _viewModel: RecipeDetailsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar(binding.toolbar)
        setDisplayHomeAsUp(true)


        // getting id, and create retrieve viewmodel
        val args by navArgs<RecipeDetailsFragmentArgs>()
        Log.d(TAG, "arg.recipe_id = ${args.recipeId}")
        val viewModelFactory = RecipeDetailsViewModelFactory(args.recipeId)
        _viewModel = ViewModelProvider(this, viewModelFactory).get(RecipeDetailsViewModel::class.java)


        initBinding()
        observeViewModel()
    }

    private fun initBinding(){
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            rvIngredient.adapter = _viewModel.ingredientsAdapter
            rvSteps.adapter = _viewModel.stepsAdapter
            rvComments.adapter = _viewModel.commentAdapter

            profilePic.setOnClickListener{
                viewProfile(_viewModel.recipe.value?.authorId!!)
            }
        }
    }

    private fun observeViewModel(){
        with(_viewModel){
            onProfileClick.observe(viewLifecycleOwner, {
                Log.d(TAG, "profileClicked::${it.uid}=${it.name}")
                viewProfile(it.uid!!)
            })

            recipe.observe(viewLifecycleOwner, {
                if (it.authorId == _viewModel.user.value!!.uid!!){
                    setHasOptionsMenu(true)
                }
            })

            ingredients.observe(viewLifecycleOwner, {
                _viewModel.ingredientsAdapter.items = it
                _viewModel.ingredientsAdapter.notifyDataSetChanged()
                it?.log()
            })

            steps.observe(viewLifecycleOwner, {
                _viewModel.stepsAdapter.items = it
                _viewModel.stepsAdapter.notifyDataSetChanged()
                it?.log()
            })
        }
    }

    private fun viewProfile(userId: String){
        val action = RecipeDetailsFragmentDirections.actionRecipeDetailsFragmentToProfileOtherFragment(userId)
        findNavController().navigate(action)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_edit -> {
                val recipeID = _viewModel.recipeId
                val action = RecipeDetailsFragmentDirections.actionRecipeDetailsFragmentToAddRecipeInfoFragment(recipeID)
                findNavController().navigate(action)
                true
            }
            R.id.action_delete -> {
                showDeleteDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // dialog
    private fun showDeleteDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.deletion))
            .setMessage(getString(R.string.warn_deleting_recipe_message))
            .setPositiveButton(getString(R.string.confirm)) { _, i ->
                FBDatabaseRepository.deleteRecipe(_viewModel.recipeId)
                findNavController().popBackStack()
            }
            .setNegativeButton(getString(R.string.cancel),null)
            .show()
    }

    override fun onStart() {
        super.onStart()
        _viewModel.commentAdapter.startListening()
    }

    override fun onStop() {
        _viewModel.commentAdapter.stopListening()
        super.onStop()
    }


}