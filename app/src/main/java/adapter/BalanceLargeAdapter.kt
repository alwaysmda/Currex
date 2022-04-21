package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currex.R
import com.example.currex.databinding.RowBalanceLargeBinding
import domain.model.Rate
import main.ApplicationClass

class BalanceLargeAdapter(
    private val app: ApplicationClass,
) : ListAdapter<Rate, BalanceLargeAdapter.BalanceLargeHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceLargeAdapter.BalanceLargeHolder {
        return BalanceLargeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_balance_large, parent, false))
    }

    override fun onBindViewHolder(holder: BalanceLargeAdapter.BalanceLargeHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class BalanceLargeHolder(private val binding: RowBalanceLargeBinding) : RecyclerView.ViewHolder(binding.root) {
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