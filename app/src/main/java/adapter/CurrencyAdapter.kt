package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currex.R
import com.example.currex.databinding.RowCurrencyBinding
import domain.model.Rate

class CurrencyAdapter(
    private val onItemClick: (Int, Rate) -> Unit
) : ListAdapter<Rate, CurrencyAdapter.BalanceLargeHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyAdapter.BalanceLargeHolder {
        return BalanceLargeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_currency, parent, false))
    }

    override fun onBindViewHolder(holder: CurrencyAdapter.BalanceLargeHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class BalanceLargeHolder(private val binding: RowCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rate: Rate) {
            with(binding) {
                data = rate
                executePendingBindings()
                rowCurrencyIvSelected.setImageResource(if (rate.isSell) R.drawable.ic_sell else R.drawable.ic_receive)
                rowCurrencyIvSelected.setColorFilter(ContextCompat.getColor(root.context, if (rate.isSell) R.color.main_red else R.color.main_green))
                rowCurrencyClParent.setOnClickListener {
                    onItemClick(bindingAdapterPosition, rate)
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