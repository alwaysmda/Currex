package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.currex.R
import com.android.currex.databinding.RowPhotoBinding
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
            PhotoHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_photo, parent, false))
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
            is PhotoHolder -> holder.bind(getItem(position))
            is PhotoHolder -> holder.bind(getItem(position))
        }

    }


    inner class PhotoHolder(val binding: RowPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            with(binding) {
                app = this@PhotoAdapter.app
                data = photo
                executePendingBindings()
                rowPhotoCvContent.setOnClickListener { onItemClick(bindingAdapterPosition, photo) }
                rowPhotoTvText.text = translate("1 : ${photo.id}")
                ViewCompat.setTransitionName(rowPhotoIvPhoto, "transPhoto$bindingAdapterPosition")
                ViewCompat.setTransitionName(rowPhotoTvText, "transId$bindingAdapterPosition")
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