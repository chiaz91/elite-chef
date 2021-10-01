package ntu.platform.cookery.ui.fragment.chat_list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ntu.platform.cookery.base.BindingFragment
import ntu.platform.cookery.databinding.FragmentChatListBinding
import ntu.platform.cookery.util.setToolBar

private const val TAG = "Cy.chatlist"
class ChatListFragment:  BindingFragment<FragmentChatListBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentChatListBinding
    get() = FragmentChatListBinding::inflate

    private val _viewModel: ChatListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initBinding()
        observeViewModel()
    }


    private fun initBinding() {
        with(binding){
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            setToolBar(toolbarLayout.toolbar)

            rvChatList.adapter = _viewModel.chatListAdapter
        }
    }

    private fun observeViewModel(){
        with(_viewModel){
            onChatClicked.observe(viewLifecycleOwner,{
                toChat(it.key!!)
            })
        }
    }

    fun toChat(chatId:String){
        val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(chatId)
        findNavController().navigate(action)
    }



    override fun onStart() {
        super.onStart()
        _viewModel.chatListAdapter.startListening()
    }

    override fun onStop() {
        _viewModel.chatListAdapter.stopListening()
        super.onStop()
    }
}