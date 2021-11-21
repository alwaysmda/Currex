package ui.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xodus.templatefive.R
import com.xodus.templatefive.databinding.FragmentTemplateBindingBinding
import dagger.hilt.android.AndroidEntryPoint
import ui.BaseActivity


@AndroidEntryPoint
class TemplateBindingFragment : Fragment() {
    private var _binding: FragmentTemplateBindingBinding? = null
    private val binding: FragmentTemplateBindingBinding get() = _binding!!
    private val viewModel: TemplateBindingViewModel by viewModels()
    private lateinit var baseActivity: BaseActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate<FragmentTemplateBindingBinding>(inflater, R.layout.fragment_template_binding, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        baseActivity = requireActivity() as BaseActivity
        init()
        viewModel.action.onStart()
        return binding.root
    }


    private fun init() {
        with(binding) {

        }
    }
}