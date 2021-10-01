package ntu.platform.cookery.ui.fragment.chat

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentChatBinding
import ntu.platform.cookery.util.setToolBar


private const val TAG = "Cy.chat"
class ChatFragment:  BindingFragment<FragmentChatBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentChatBinding
    get() = FragmentChatBinding::inflate

    private lateinit var _viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args by navArgs<ChatFragmentArgs>()
        Log.d(TAG, "arg.chatid=${args.chatId}")
        val vmFactory = ChatViewModelFactory(args.chatId)
        _viewModel = ViewModelProvider(this, vmFactory).get(ChatViewModel::class.java)
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


            rvMessages.adapter = _viewModel.messageAdapter
            (rvMessages.layoutManager as LinearLayoutManager).stackFromEnd = true
        }
    }

    private fun observeViewModel(){
        with(_viewModel){

        }
    }


    override fun onStart() {
        super.onStart()
        _viewModel.messageAdapter.startListening()
    }

    override fun onStop() {
        _viewModel.messageAdapter.stopListening()
        super.onStop()
    }

}