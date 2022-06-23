package ui.convert

import adapter.BalanceSmallAdapter
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.example.currex.R
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import domain.usecase.convert.ConvertUseCases
import kotlinx.coroutines.flow.onEach
import main.ApplicationClass
import ui.balance_list.BalanceListController
import util.extension.fadeInOut
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ConvertController : Controller() {
    @Inject
    private lateinit var app: ApplicationClass

    @Inject
    private lateinit var usecases: ConvertUseCases

    private var timer: Timer = Timer()
    private var isBackPressed = false
    private lateinit var adapter: BalanceSmallAdapter
    private var sellTextWatcher: TextWatcher? = null
    private var receiveTextWatcher: TextWatcher? = null
    private var initialized = false

    //Views
    private lateinit var v: View
    private lateinit var convertRvBalance: RecyclerView
    private lateinit var convertEtSell: EditText
    private lateinit var convertEtReceive: EditText
    private lateinit var convertTvValidationError: MaterialTextView

    private lateinit var viewModel: ConvertViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        v = inflater.inflate(R.layout.fragment_convert, container, false)
        viewModel = ConvertViewModel(app, usecases)
        init(v)
        observeEvents()
        setBackHandler()
        v.post {
            viewModel.action.onStart()
        }
        //todo
//        getBackStackData<Rate>(Constant.ARG_RATE) {
//            viewModel.action.onCurrencyChanged(it)
//        }
        return v
    }

    private fun init(view: View) {
        convertRvBalance = view.findViewById(R.id.convert_rvBalance)
        convertEtSell = view.findViewById(R.id.convert_etSell)
        convertEtReceive = view.findViewById(R.id.convert_etReceive)
        convertTvValidationError = view.findViewById(R.id.convert_tvValidationError)
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


    private fun setBackHandler() {
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (isBackPressed) {
//                    baseActivity.finishAffinity()
//                } else {
//                    isBackPressed = true
//                    snack(binding.root, getString(R.string.tapAgainToExit))
//                    timer.cancel()
//                    timer = Timer()
//                    timer.schedule(object : TimerTask() {
//                        override fun run() {
//                            isBackPressed = false
//                        }
//                    }, 2500)
//                }
//            }
//        })
    }

//    override fun onDestroyView() {
//        initialized = false
//        super.onDestroyView()
//    }

    private fun observeEvents() {
        viewModel.event.onEach {
            when (it) {
                is ConvertEvents.UpdateBalanceList         -> {
                    convertRvBalance.recycledViewPool.setMaxRecycledViews(0, 0)
                    adapter.submitList(it.list) {
                        if (it.list.size > 20) {
                            convertRvBalance.scrollToPosition(0)
                        } else {
                            convertRvBalance.smoothScrollToPosition(0)
                        }
                    }
                }
                is ConvertEvents.NavBalanceList            -> {
                    val bundle = Bundle()
                    bundle.putParcelableArray(BalanceListController.ARG_BALANCE_LIST, it.list.toTypedArray())
                    router.pushController(RouterTransaction.with(BalanceListController(bundle)))
                }
                is ConvertEvents.NavCurrencyList           -> Unit //findNavController().navigate(ConvertFragmentDirections.actionConvertFragmentToCurrencyListFragment(it.list.toTypedArray(), it.sellRate, it.receiveRate))
                is ConvertEvents.NavSetting                -> Unit //findNavController().navigate(ConvertFragmentDirections.actionConvertFragmentToSettingFragment())
                is ConvertEvents.ShowConvertCompleteDialog -> Unit
//                    CustomDialog(v.context)
//                    .setTitle(R.string.convert_success_title)
//                    .setContent(it.content.asString(v.context) + "\n" + it.fee.asString(v.context))
//                    .setPositiveText(R.string.confirm)
//                    .show(childFragmentManager)
                is ConvertEvents.UpdateSellText            -> {
                    convertEtSell.apply {
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
                    convertEtReceive.apply {
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
        }//.launchIn( viewLifecycleOwner.lifecycleScope)
        viewModel.validationErrorTextVisibility.onEach {
            if (it) {
                convertTvValidationError.fadeInOut()
            }

        }//.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}