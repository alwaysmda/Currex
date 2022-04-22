package ui.currency_list

import adapter.CurrencyAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.currex.R
import com.example.currex.databinding.FragmentCurrencyListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ui.base.BaseFragment
import util.Constant
import util.extension.setBackStackData
import util.extension.snack

@AndroidEntryPoint
class CurrencyListFragment : BaseFragment<FragmentCurrencyListBinding, CurrencyListEvents, CurrencyListAction, CurrencyListViewModel>(R.layout.fragment_currency_list) {
    private lateinit var adapter: CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: CurrencyListViewModel by viewModels()
        initialize(vm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        init()
        observeEvents()
        arguments?.let {
            CurrencyListFragmentArgs.fromBundle(it).apply {
                binding.root.post {
                    viewModel.action.onStart(ArrayList(list.toMutableList()), sellRate, receiveRate)
                }
            }
        }
        return binding.root
    }

    private fun init() {
        binding.apply {
            adapter = CurrencyAdapter { index, rate -> viewModel.action.onCurrencyClick(index, rate) }
            currencyListRvCurrency.adapter = adapter
            currencyListRvCurrency.setHasFixedSize(true)
            currencyListRvCurrency.layoutAnimation = viewModel.app.recyclerViewAnimation
        }
    }


    private fun observeEvents() {
        viewModel.event.onEach {
            when (it) {
                is CurrencyListEvents.Rebind             -> binding.vm = viewModel
                is CurrencyListEvents.Snack              -> snack(binding.root, it.message)
                is CurrencyListEvents.UpdateCurrencyList -> adapter.submitList(it.list)
                is CurrencyListEvents.NavBack            -> findNavController().popBackStack()
                is CurrencyListEvents.ReturnCurrency     -> setBackStackData(Constant.ARG_RATE, it.rate)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}