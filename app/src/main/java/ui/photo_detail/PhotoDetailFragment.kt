package ui.photo_detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.xodus.templatefive.BR
import com.xodus.templatefive.R
import com.xodus.templatefive.databinding.FragmentPhotoDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import util.Constant
import util.extension.darkenColor
import util.extension.getColorFromAttributes
import util.extension.setStatusbarColor
import util.extension.translate

@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {
    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding: FragmentPhotoDetailBinding get() = _binding!!
    private val viewModel: PhotoDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.image_shared_element_transition)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.image_shared_element_transition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate<FragmentPhotoDetailBinding>(inflater, R.layout.fragment_photo_detail, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            app = viewModel.app
            action = viewModel.action
        }
        init()
        observeState()
        observeEvent()
        PhotoDetailFragmentArgs.fromBundle(requireArguments()).apply {
            viewModel.action.onStart(photo)
            ViewCompat.setTransitionName(binding.photoDetailIvPhoto, "${Constant.CON_TRANS_PHOTO}${position}")
            ViewCompat.setTransitionName(binding.photoDetailTvId, "${Constant.CON_TRANS_ID}${position}")
            Picasso.get().load(photo.url.thumb).into(binding.photoDetailIvPhoto)
        }
        return binding.root
    }


    private fun init() {
        with(binding) {

        }
    }

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.state.collectLatest {
            with(binding) {
                when (it) {
                    is PhotoDetailState.Idle -> {

                    }
                    is PhotoDetailState.Bind -> {
                        Picasso.get().load(it.photo.url.original).into(object : Target {
                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                photoDetailIvPhoto.setImageBitmap(bitmap)
                            }

                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                            }

                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                            }
                        })
                        photoDetailTvAlbumId.text = translate("${viewModel.app.m.albumId} : ${it.photo.albumId}")
                        photoDetailTvId.text = translate("${viewModel.app.m.id} : ${it.photo.id}")
                        photoDetailTvTitle.text = translate("${viewModel.app.m.title} : ${it.photo.title}")
                        try {
                            val color = Color.parseColor("#${it.photo.url.original.split("/").last()}")
                            photoDetailToolbar.toolbar.setBackgroundColor(darkenColor(color, 0.8F))
                            setStatusbarColor(requireActivity(), darkenColor(color, 0.4F))
                            photoDetailBtnOpenUrl.setBackgroundColor(darkenColor(color, 0.8F))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun observeEvent() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.event.collect {
            when (it) {
                is PhotoDetailEvents.Rebind      -> {
                    binding.setVariable(BR.app, it.app)
                }
                is PhotoDetailEvents.NavBack     -> {
                    setStatusbarColor(requireActivity(), getColorFromAttributes(requireActivity(), R.attr.colorPrimaryDark))
                    findNavController().popBackStack()
                }
                is PhotoDetailEvents.Snack       -> {
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                }
                is PhotoDetailEvents.Browse      -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
                }
                is PhotoDetailEvents.UpdateTheme -> {
                    viewModel.app.themeManager.changeTheme(requireActivity(), it.theme)
                }
            }
        }
    }
}