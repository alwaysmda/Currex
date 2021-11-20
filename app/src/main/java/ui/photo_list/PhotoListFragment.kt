package ui.photo_list

import adapter.PhotoAdapter
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.xodus.templatefive.BR
import com.xodus.templatefive.R
import com.xodus.templatefive.databinding.FragmentPhotoListBinding
import dagger.hilt.android.AndroidEntryPoint
import domain.model.Photo
import kotlinx.coroutines.flow.collectLatest
import ui.BaseActivity
import util.Constant

@AndroidEntryPoint
class PhotoListFragment : Fragment() {
    private var _binding: FragmentPhotoListBinding? = null
    private val binding: FragmentPhotoListBinding get() = _binding!!
    private val viewModel: PhotoListViewModel by viewModels()
    private lateinit var baseActivity: BaseActivity

    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.image_shared_element_transition)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.image_shared_element_transition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate<FragmentPhotoListBinding>(inflater, R.layout.fragment_photo_list, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            app = viewModel.app
            action = viewModel.action
        }
        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }
        baseActivity = requireActivity() as BaseActivity
        init()
        observeState()
        observeEvent()
        viewModel.action.onStart()
        return binding.root
    }


    private fun init() {
        with(binding) {
            adapter = PhotoAdapter(viewModel.app, ::onItemClick, ::onRetry)
            photoListRvPhoto.setHasFixedSize(true)
            photoListRvPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (
                        recyclerView.computeVerticalScrollOffset() +
                        recyclerView.computeVerticalScrollExtent() >
                        recyclerView.computeVerticalScrollRange() - 100
                    ) {
                        viewModel.action.onPaginate()
                    }
                }
            })
        }
    }

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.state.collectLatest {
            with(binding) {
                when (it) {
                    is PhotoListState.Idle            -> {
                        baseActivity.setLoading(false)
                        photoListTvMessage.isVisible = false
                        photoListRvPhoto.isVisible = false
                        photoListCvEmpty.isVisible = false
                        adapter.setLoading(false)
                    }
                    is PhotoListState.Loading         -> {
                        baseActivity.setLoading(true)
                        photoListTvMessage.isVisible = false
                        photoListRvPhoto.isVisible = false
                        photoListCvEmpty.isVisible = false
                    }
                    is PhotoListState.EmptyView       -> {
                        baseActivity.setLoading(false)
                        photoListTvMessage.isVisible = false
                        photoListRvPhoto.isVisible = false
                        photoListCvEmpty.isVisible = true
                    }
                    is PhotoListState.UpdatePhotoList -> {
                        baseActivity.setLoading(false)
                        photoListTvMessage.isVisible = false
                        photoListRvPhoto.isVisible = true
                        photoListCvEmpty.isVisible = false
                        if (binding.photoListRvPhoto.adapter == null) {
                            binding.photoListRvPhoto.adapter = adapter
                        }
                        adapter.submitList(it.list)
                        adapter.setLoading(it.loading)
                        if (it.retry) {
                            adapter.setRetry()
                        }
                    }
                }
            }
        }
    }

    private fun observeEvent() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.event.collectLatest {
            when (it) {
                is PhotoListEvents.Rebind   -> {
                    binding.setVariable(BR.app, it.app)
                    (binding.photoListRvPhoto.layoutManager as LinearLayoutManager).apply {
                        val start = findFirstVisibleItemPosition()
                        val end = findLastVisibleItemPosition()
                        binding.photoListRvPhoto.adapter?.notifyItemRangeChanged(start, end)
                    }
                }
                is PhotoListEvents.NavPhoto -> {
                    (binding.photoListRvPhoto.findViewHolderForLayoutPosition(it.position) as PhotoAdapter.PhotoHolder?)?.binding?.let { row ->
                        val extra = FragmentNavigatorExtras(
                            row.rowPhotoIvPhoto to "${Constant.CON_TRANS_PHOTO}${it.position}",
                            row.rowPhotoTvText to "${Constant.CON_TRANS_ID}${it.position}",
                        )
                        findNavController().navigate(PhotoListFragmentDirections.actionNavigationOneToPhotoDetailFragment(it.photo, it.position), extra)
                    }
                }
                is PhotoListEvents.Snack    -> {
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onItemClick(position: Int, photo: Photo) {
        viewModel.action.onPhotoClick(position, photo)
    }

    private fun onRetry() {
        viewModel.onRetry()
    }


}