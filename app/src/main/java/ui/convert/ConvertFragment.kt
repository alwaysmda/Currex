package ui.convert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.currex.R
import com.android.currex.databinding.FragmentConvertBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ui.base.BaseFragment
import ui.base.BaseState
import util.extension.snack
import java.util.*

@AndroidEntryPoint
class ConvertFragment : BaseFragment<FragmentConvertBinding, ConvertEvents, ConvertAction, BaseState, ConvertViewModel>(R.layout.fragment_convert) {
    private var timer: Timer = Timer()
    private var isBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: ConvertViewModel by viewModels()
        initialize(vm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        init()
        observeEvents()
        setBackHandler()
        viewModel.action.onStart()
        return binding.root
    }

    private fun init() {
        binding.apply {
        }
    }

    private fun setBackHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isBackPressed) {
                    baseActivity.finishAffinity()
                } else {
                    isBackPressed = true
                    snack(binding.root, baseActivity.getString(R.string.tapAgainToExit))
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            isBackPressed = false
                        }
                    }, 2500)
                }
            }
        })
    }

    private fun observeEvents() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.event.collectLatest {
            when (it) {
                is ConvertEvents.Rebind -> {
                    binding.vm = viewModel
                }
                is ConvertEvents.Snack  -> {
                    snack(binding.root, it.message)
                }
            }
        }
    }
}