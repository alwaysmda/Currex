package ui.convert

import adapter.BalanceSmallAdapter
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.currex.R
import com.example.currex.databinding.FragmentConvertBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ui.base.BaseFragment
import util.extension.snack
import java.util.*

@AndroidEntryPoint
class ConvertFragment : BaseFragment<FragmentConvertBinding, ConvertEvents, ConvertAction, ConvertViewModel>(R.layout.fragment_convert) {
    private var timer: Timer = Timer()
    private var isBackPressed = false
    private lateinit var adapter: BalanceSmallAdapter
    private var sellTextWatcher: TextWatcher? = null
    private var receiveTextWatcher: TextWatcher? = null

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
            adapter = BalanceSmallAdapter {
                viewModel.action.onBalanceMoreClick()
            }
            convertRvBalance.adapter = adapter
            convertRvBalance.setHasFixedSize(true)
            sellTextWatcher = convertEtSell.addTextChangedListener(afterTextChanged = {
                viewModel.action.onSellTextChanged(it.toString())
            })
            receiveTextWatcher = convertEtReceive.addTextChangedListener(afterTextChanged = {
                viewModel.action.onReceiveTextChanged(it.toString())
            })
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

    private fun observeEvents() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.event.collectLatest {
            when (it) {
                is ConvertEvents.Rebind            -> binding.vm = viewModel
                is ConvertEvents.Snack             -> snack(binding.root, it.message)
                is ConvertEvents.UpdateBalanceList -> adapter.submitList(it.list)
                is ConvertEvents.NavBalanceList    -> findNavController().navigate(ConvertFragmentDirections.actionConvertFragmentToBalanceListFragment(it.list.toTypedArray()))
                is ConvertEvents.NavCurrencyList   -> Unit
                is ConvertEvents.ShowDialog        -> it.dialog.show(childFragmentManager)
                is ConvertEvents.UpdateSellText    -> {
                    binding.convertEtSell.apply {
                        removeTextChangedListener(sellTextWatcher)
                        setText(it.text)
                        sellTextWatcher = addTextChangedListener(afterTextChanged = { text ->
                            viewModel.action.onSellTextChanged(text.toString())
                        })
                    }
                }
                is ConvertEvents.UpdateReceiveText -> {
                    binding.convertEtReceive.apply {
                        removeTextChangedListener(receiveTextWatcher)
                        setText(it.text)
                        receiveTextWatcher = addTextChangedListener(afterTextChanged = { text ->
                            viewModel.action.onReceiveTextChanged(text.toString())
                        })
                    }
                }
            }
        }
    }
}