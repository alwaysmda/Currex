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
import domain.model.Rate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ui.base.BaseFragment
import ui.dialog.CustomDialog
import util.Constant
import util.extension.fadeInOut
import util.extension.getBackStackData
import util.extension.snack
import java.util.*

@AndroidEntryPoint
class ConvertFragment : BaseFragment<FragmentConvertBinding, ConvertEvents, ConvertAction, ConvertViewModel>(R.layout.fragment_convert) {
    private var timer: Timer = Timer()
    private var isBackPressed = false
    private lateinit var adapter: BalanceSmallAdapter
    private var sellTextWatcher: TextWatcher? = null
    private var receiveTextWatcher: TextWatcher? = null
    private var initialized = false

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
        binding.root.post {
            viewModel.action.onStart()
        }
        getBackStackData<Rate>(Constant.ARG_RATE) {
            viewModel.action.onCurrencyChanged(it)
        }
        return binding.root
    }

    private fun init() {
        binding.apply {
            adapter = BalanceSmallAdapter {
                viewModel.action.onBalanceMoreClick()
            }
            convertRvBalance.adapter = adapter
            convertRvBalance.setHasFixedSize(true)
            convertRvBalance.layoutAnimation = viewModel.app.recyclerViewAnimation
            sellTextWatcher = convertEtSell.addTextChangedListener(afterTextChanged = {
                if (initialized) {
                    viewModel.action.onSellTextChanged(it.toString())
                }
            })
            receiveTextWatcher = convertEtReceive.addTextChangedListener(afterTextChanged = {
                if (initialized) {
                    viewModel.action.onReceiveTextChanged(it.toString())
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        initialized = true
    }

    private fun setBackHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isBackPressed) {
                    baseActivity.finishAffinity()
                } else {
                    isBackPressed = true
                    snack(binding.root, getString(R.string.tapAgainToExit))
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

    override fun onDestroyView() {
        initialized = false
        super.onDestroyView()
    }

    private fun observeEvents() {
        viewModel.event.onEach {
            when (it) {
                is ConvertEvents.UpdateBalanceList         -> {
                    binding.convertRvBalance.recycledViewPool.setMaxRecycledViews(0, 0)
                    adapter.submitList(it.list) {
                        if (it.list.size > 20) {
                            binding.convertRvBalance.scrollToPosition(0)
                        } else {
                            binding.convertRvBalance.smoothScrollToPosition(0)
                        }
                    }
                }
                is ConvertEvents.NavBalanceList            -> findNavController().navigate(ConvertFragmentDirections.actionConvertFragmentToBalanceListFragment(it.list.toTypedArray()))
                is ConvertEvents.NavCurrencyList           -> findNavController().navigate(ConvertFragmentDirections.actionConvertFragmentToCurrencyListFragment(it.list.toTypedArray(), it.sellRate, it.receiveRate))
                is ConvertEvents.NavSetting                -> findNavController().navigate(ConvertFragmentDirections.actionConvertFragmentToSettingFragment())
                is ConvertEvents.ShowConvertCompleteDialog -> CustomDialog(requireContext())
                    .setTitle(R.string.convert_success_title)
                    .setContent(it.content.asString(requireContext()) + "\n" + it.fee.asString(requireContext()))
                    .setPositiveText(R.string.confirm)
                    .show(childFragmentManager)
                is ConvertEvents.UpdateSellText            -> {
                    binding.convertEtSell.apply {
                        if (text.toString() != it.text) {
                            removeTextChangedListener(sellTextWatcher)
                            setText(it.text)
                            sellTextWatcher = addTextChangedListener(afterTextChanged = { text ->
                                viewModel.action.onSellTextChanged(text.toString())
                            })
                        }
                    }
                }
                is ConvertEvents.UpdateReceiveText         -> {
                    binding.convertEtReceive.apply {
                        if (text.toString() != it.text) {
                            removeTextChangedListener(receiveTextWatcher)
                            setText(it.text)
                            receiveTextWatcher = addTextChangedListener(afterTextChanged = { text ->
                                viewModel.action.onReceiveTextChanged(text.toString())
                            })
                        }
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.validationErrorTextVisibility.onEach {
            if (it) {
                binding.convertTvValidationError.fadeInOut()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}