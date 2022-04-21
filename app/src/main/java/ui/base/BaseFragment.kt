package ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment

open class BaseFragment<VB : ViewDataBinding, E : BaseEvent, A : BaseAction, VM : BaseViewModel<E, A>>(private val layoutId: Int) : Fragment() {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!
    lateinit var viewModel: VM
    lateinit var baseActivity: BaseActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate<VB>(inflater, layoutId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.vm, viewModel)
        }
        baseActivity = requireActivity() as BaseActivity
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun initialize(viewmodel: VM) {
        viewModel = viewmodel
    }
}