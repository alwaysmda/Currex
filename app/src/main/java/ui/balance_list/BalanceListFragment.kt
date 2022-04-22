package ui.balance_list

import adapter.BalanceLargeAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.currex.R
import com.example.currex.databinding.FragmentBalanceListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ui.base.BaseFragment
import util.extension.snack

@AndroidEntryPoint
class BalanceListFragment : BaseFragment<FragmentBalanceListBinding, BalanceListEvents, BalanceListAction, BalanceListViewModel>(R.layout.fragment_balance_list) {
    private lateinit var adapter: BalanceLargeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: BalanceListViewModel by viewModels()
        initialize(vm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        init()
        observeEvents()
        arguments?.let {
            val list = BalanceListFragmentArgs.fromBundle(it).list.toMutableList()
            binding.root.post {
                viewModel.action.onStart(ArrayList(list))
            }
        }
        return binding.root
    }

    private fun init() {
        binding.apply {
            adapter = BalanceLargeAdapter(viewModel.app)
            balanceListRvBalance.adapter = adapter
            balanceListRvBalance.setHasFixedSize(true)
            balanceListRvBalance.layoutAnimation = viewModel.app.recyclerViewAnimation
        }
    }


    private fun observeEvents() {
        viewModel.event.onEach {
            when (it) {
                is BalanceListEvents.Rebind            -> binding.vm = viewModel
                is BalanceListEvents.Snack             -> snack(binding.root, it.message)
                is BalanceListEvents.UpdateBalanceList -> adapter.submitList(it.list)
                is BalanceListEvents.NavBack           -> findNavController().popBackStack()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}