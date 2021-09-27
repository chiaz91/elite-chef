package ntu.platform.cookery.ui.fragment.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ntu.platform.cookery.databinding.FragmentNewsfeedBinding
import ntu.platform.cookery.util.setToolBar

class NewsfeedFragment : Fragment() {
    private lateinit var dashboardViewModel: NewsfeedViewModel
    private var _binding: FragmentNewsfeedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(NewsfeedViewModel::class.java)

        _binding = FragmentNewsfeedBinding.inflate(inflater, container, false)
        val root: View = binding.root



        setToolBar(binding.toolbarLayout.toolbar)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}