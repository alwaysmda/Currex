package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currex.R
import com.example.currex.databinding.RowBalanceSmallBinding
import domain.model.Rate
import main.ApplicationClass

class PhotoAdapter(
    private val app: ApplicationClass,
    private val onItemClick: (Int, Rate) -> Unit = { _, _ -> },
    private val onRetryClick: () -> Unit = {},
) : ListAdapter<Rate, RecyclerView.ViewHolder>(DiffCallback()) {
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
            PhotoHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_balance_small, parent, false))
        } else {
            PhotoHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_balance_small, parent, false))
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


    inner class PhotoHolder(val binding: RowBalanceSmallBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rate: Rate) {
            with(binding) {
                data = rate
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Rate>() {
        override fun areItemsTheSame(oldItem: Rate, newItem: Rate) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Rate, newItem: Rate) =
            oldItem == newItem
    }
}