package ui.balance_list

import adapter.BalanceLargeAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import com.example.currex.R
import kotlinx.coroutines.flow.onEach
import main.ApplicationClass
import javax.inject.Inject

class BalanceListController(args: Bundle) : Controller() {

    companion object {
        const val ARG_BALANCE_LIST = "ARG_BALANCE_LIST"
    }

    @Inject
    private lateinit var app: ApplicationClass

    private lateinit var adapter: BalanceLargeAdapter

    //View
    private lateinit var v: View
    private lateinit var balanceListRvBalance: RecyclerView

    private lateinit var viewModel: BalanceListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        v = inflater.inflate(R.layout.fragment_balance_list, container, false)
        viewModel = BalanceListViewModel(app)
        init(v)
        observeEvents()
        v.post {
//            args.getParcelableArray(ARG_BALANCE_LIST) as List<Rate>
//            (args.getParcelableArray(ARG_BALANCE_LIST)? as List<Rate>)?.let {
//
//                viewModel.onStart(ArrayList(it.toMutableList()))
//            }
        }
        return v
    }

    private fun init(view: View) {
        balanceListRvBalance = view.findViewById(R.id.balanceList_rvBalance)
        adapter = BalanceLargeAdapter(viewModel.app)
        balanceListRvBalance.adapter = adapter
        balanceListRvBalance.setHasFixedSize(true)
        balanceListRvBalance.layoutAnimation = viewModel.app.recyclerViewAnimation
    }

    private fun observeEvents() {
        viewModel.event.onEach {
            when (it) {
                is BalanceListEvents.UpdateBalanceList -> adapter.submitList(it.list)
                is BalanceListEvents.NavBack           -> router.popCurrentController()
            }
        }//.launchIn(viewLifecycleOwner.lifecycleScope) todo scope
    }

}