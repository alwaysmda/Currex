package ui.setting

import adapter.SettingOptionAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.currex.R
import com.example.currex.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ui.base.BaseFragment
import util.extension.snack

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, SettingEvents, SettingAction, SettingViewModel>(R.layout.fragment_setting) {
    private lateinit var retryIntervalAdapter: SettingOptionAdapter<Int>
    private lateinit var freeConvertCountAdapter: SettingOptionAdapter<Int>
    private lateinit var freeConvertEveryXAdapter: SettingOptionAdapter<Int>
    private lateinit var freeConvertBelowXEurAdapter: SettingOptionAdapter<Double>
    private lateinit var refreshRegularlyAdapter: SettingOptionAdapter<Boolean>
    private lateinit var refreshIntervalAdapter: SettingOptionAdapter<Int>
    private lateinit var homeBalanceCountAdapter: SettingOptionAdapter<Int>
    private lateinit var initialBalanceValueAdapter: SettingOptionAdapter<Double>
    private lateinit var initialBalanceNameAdapter: SettingOptionAdapter<String>
    private lateinit var conversionFeeAdapter: SettingOptionAdapter<Double>
    private lateinit var reduceFeeFromSourceAdapter: SettingOptionAdapter<Boolean>
    private lateinit var reduceFeeFromTargetAdapter: SettingOptionAdapter<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: SettingViewModel by viewModels()
        initialize(vm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        init()
        observeEvents()
        binding.root.post {
            viewModel.action.onStart()
        }
        return binding.root
    }

    private fun init() {
        binding.apply {
            retryIntervalAdapter = SettingOptionAdapter { index, option -> viewModel.action.onRetryIntervalClick(index, option) }
            freeConvertCountAdapter = SettingOptionAdapter { index, option -> viewModel.action.onFreeConvertCountClick(index, option) }
            freeConvertEveryXAdapter = SettingOptionAdapter { index, option -> viewModel.action.onFreeConvertEveryXClick(index, option) }
            freeConvertBelowXEurAdapter = SettingOptionAdapter { index, option -> viewModel.action.onFreeConvertBelowXEurClick(index, option) }
            refreshRegularlyAdapter = SettingOptionAdapter { index, option -> viewModel.action.onRefreshRegularlyClick(index, option) }
            refreshIntervalAdapter = SettingOptionAdapter { index, option -> viewModel.action.onRefreshIntervalClick(index, option) }
            homeBalanceCountAdapter = SettingOptionAdapter { index, option -> viewModel.action.onHomeBalanceCountClick(index, option) }
            initialBalanceValueAdapter = SettingOptionAdapter { index, option -> viewModel.action.onInitialBalanceValueClick(index, option) }
            initialBalanceNameAdapter = SettingOptionAdapter { index, option -> viewModel.action.onInitialBalanceNameClick(index, option) }
            conversionFeeAdapter = SettingOptionAdapter { index, option -> viewModel.action.onConversionFeeClick(index, option) }
            reduceFeeFromSourceAdapter = SettingOptionAdapter { index, option -> viewModel.action.onReduceFeeFromSourceClick(index, option) }
            reduceFeeFromTargetAdapter = SettingOptionAdapter { index, option -> viewModel.action.onReduceFeeFromTargetClick(index, option) }

            settingRvRetryInterval.adapter = retryIntervalAdapter
            settingRvFreeConvertCount.adapter = freeConvertCountAdapter
            settingRvFreeConvertEveryX.adapter = freeConvertEveryXAdapter
            settingRvFreeConvertBelowXEur.adapter = freeConvertBelowXEurAdapter
            settingRvRefreshRegularly.adapter = refreshRegularlyAdapter
            settingRvRefreshInterval.adapter = refreshIntervalAdapter
            settingRvHomeBalanceCount.adapter = homeBalanceCountAdapter
            settingRvInitialBalanceValue.adapter = initialBalanceValueAdapter
            settingRvInitialBalanceName.adapter = initialBalanceNameAdapter
            settingRvConversionFee.adapter = conversionFeeAdapter
            settingRvReduceFeeFromSource.adapter = reduceFeeFromSourceAdapter
            settingRvReduceFeeFromTarget.adapter = reduceFeeFromTargetAdapter

            settingRvRetryInterval.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvFreeConvertCount.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvFreeConvertEveryX.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvFreeConvertBelowXEur.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvRefreshRegularly.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvRefreshInterval.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvHomeBalanceCount.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvInitialBalanceValue.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvInitialBalanceName.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvConversionFee.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvReduceFeeFromSource.layoutAnimation = viewModel.app.recyclerViewAnimation
            settingRvReduceFeeFromTarget.layoutAnimation = viewModel.app.recyclerViewAnimation
        }
    }


    @SuppressWarnings("unchecked")
    private fun observeEvents() {
        viewModel.event.onEach {
            when (it) {
                is SettingEvents.Rebind                         -> binding.vm = viewModel
                is SettingEvents.Snack                          -> snack(binding.root, it.message)
                is SettingEvents.NavBack                        -> findNavController().popBackStack()
                is SettingEvents.UpdateRetryIntervalList        -> retryIntervalAdapter.submitList(it.list)
                is SettingEvents.UpdateFreeConvertCountList     -> freeConvertCountAdapter.submitList(it.list)
                is SettingEvents.UpdateFreeConvertEveryXList    -> freeConvertEveryXAdapter.submitList(it.list)
                is SettingEvents.UpdateFreeConvertBelowXEurList -> freeConvertBelowXEurAdapter.submitList(it.list)
                is SettingEvents.UpdateRefreshRegularlyList     -> refreshRegularlyAdapter.submitList(it.list)
                is SettingEvents.UpdateRefreshIntervalList      -> refreshIntervalAdapter.submitList(it.list)
                is SettingEvents.UpdateHomeBalanceCountList     -> homeBalanceCountAdapter.submitList(it.list)
                is SettingEvents.UpdateInitialBalanceValueList  -> initialBalanceValueAdapter.submitList(it.list)
                is SettingEvents.UpdateInitialBalanceNameList   -> initialBalanceNameAdapter.submitList(it.list)
                is SettingEvents.UpdateConversionFeeList        -> conversionFeeAdapter.submitList(it.list)
                is SettingEvents.UpdateReduceFeeFromSourceList  -> reduceFeeFromSourceAdapter.submitList(it.list)
                is SettingEvents.UpdateReduceFeeFromTargetList  -> reduceFeeFromTargetAdapter.submitList(it.list)
                is SettingEvents.SendMail                       -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(it.email))
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    }
                    startActivity(intent)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}