package ui.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xodus.templatefive.BR
import com.xodus.templatefive.R
import com.xodus.templatefive.databinding.FragmentTemplateMviBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ui.BaseActivity
import util.extension.snack


@AndroidEntryPoint
class TemplateMVIFragment : Fragment() {
    private var _binding: FragmentTemplateMviBinding? = null
    private val binding: FragmentTemplateMviBinding get() = _binding!!
    private val viewModel: TemplateMVIViewModel by viewModels()
    private lateinit var baseActivity: BaseActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate<FragmentTemplateMviBinding>(inflater, R.layout.fragment_template_mvi, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            app = viewModel.app
            action = viewModel.action
        }
        baseActivity = requireActivity() as BaseActivity
        init()
        observeState()
        observeEvent()
        viewModel.action.onStart()
        return binding.root
    }


    private fun init() {
        with(binding) {

        }
    }

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.state.collectLatest {
            with(binding) {
                when (it) {
                    is TemplateMVIState.Idle    -> {

                    }
                    is TemplateMVIState.Loading -> {
                        templateBtnCenter.text = viewModel.app.m.loading
                    }
                    is TemplateMVIState.Update  -> {
                        templateBtnCenter.text = it.data
                    }
                }
            }
        }
    }

    private fun observeEvent() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.event.collectLatest {
            when (it) {
                is TemplateMVIEvents.Rebind -> {
                    binding.setVariable(BR.app, it.app)
                }
                is TemplateMVIEvents.Snack  -> {
                    snack(binding.root, it.message)
                }
            }
        }
    }
}