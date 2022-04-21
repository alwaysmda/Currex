package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currex.R
import com.example.currex.databinding.RowBalanceSmallBinding
import com.example.currex.databinding.RowBalanceSmallMoreBinding
import domain.model.Rate

class BalanceSmallAdapter(private val onMoreClick: () -> Unit) : ListAdapter<Rate, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == currentList.size) {
            BalanceMoreHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_balance_small_more, parent, false))
        } else {
            BalanceSmallHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_balance_small, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BalanceSmallHolder -> holder.bind(getItem(position))
            is BalanceMoreHolder  -> holder.bind()
        }

    }


    inner class BalanceSmallHolder(val binding: RowBalanceSmallBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rate: Rate) {
            with(binding) {
                data = rate
                executePendingBindings()
            }
        }
    }

    inner class BalanceMoreHolder(private val binding: RowBalanceSmallMoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                rowBalanceCvMore.setOnClickListener {
                    onMoreClick()
                }
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