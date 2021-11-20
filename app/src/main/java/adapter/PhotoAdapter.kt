package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xodus.templatefive.R
import com.xodus.templatefive.databinding.RowPhotoBinding
import com.xodus.templatefive.databinding.RowPhotoStateBinding
import domain.model.Photo
import main.ApplicationClass
import util.extension.translate

class PhotoAdapter(
    private val app: ApplicationClass,
    private val onItemClick: (Int, Photo) -> Unit = { _, _ -> },
    private val onRetryClick: () -> Unit = {},
) : ListAdapter<Photo, RecyclerView.ViewHolder>(DiffCallback()) {
    private var loading = true
    private var retry = false

    fun setLoading(loading: Boolean) {
        if (this.loading == loading) {
            return
        }
        retry = false
        this.loading = loading
        if (loading) {
            notifyItemInserted(currentList.size)
        } else {
            notifyItemRemoved(currentList.size)
        }
    }

    fun setRetry() {
        retry = true
        notifyItemChanged(currentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (loading && viewType == currentList.size) {
            LoadingHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_photo_state, parent, false))
        } else {
            PhotoHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_photo, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return currentList.size + if (loading) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoadingHolder -> holder.bind()
            is PhotoHolder   -> holder.bind(getItem(position))
        }

    }


    inner class PhotoHolder(val binding: RowPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            with(binding) {
                app = this@PhotoAdapter.app
                data = photo
                executePendingBindings()
                rowPhotoCvContent.setOnClickListener { onItemClick(bindingAdapterPosition, photo) }
                rowPhotoTvText.text = translate("${this@PhotoAdapter.app.m.id} : ${photo.id}")
                ViewCompat.setTransitionName(rowPhotoIvPhoto, "transPhoto$bindingAdapterPosition")
                ViewCompat.setTransitionName(rowPhotoTvText, "transId$bindingAdapterPosition")
            }
        }
    }

    inner class LoadingHolder(val binding: RowPhotoStateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                app = this@PhotoAdapter.app
                rowPhotoBtnRetry.isVisible = retry
                rowPhotoPbLoading.isVisible = retry.not()
                executePendingBindings()
                rowPhotoBtnRetry.setOnClickListener { onRetryClick() }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem == newItem
    }
}