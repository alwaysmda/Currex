package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currex.R
import com.example.currex.databinding.RowSettingOptionBinding
import domain.model.Option

class SettingOptionAdapter<T>(
    private val onItemClick: (Int, Option<T>) -> Unit
) : ListAdapter<Option<T>, SettingOptionAdapter<T>.BalanceLargeHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingOptionAdapter<T>.BalanceLargeHolder {
        return BalanceLargeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_setting_option, parent, false))
    }

    override fun onBindViewHolder(holder: SettingOptionAdapter<T>.BalanceLargeHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class BalanceLargeHolder(private val binding: RowSettingOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: Option<T>) {
            with(binding) {
                data = option
                executePendingBindings()
                rowOptionBtnOption.setOnClickListener {
                    onItemClick(bindingAdapterPosition, option)
                }
            }
        }
    }

    private class DiffCallback<T> : DiffUtil.ItemCallback<Option<T>>() {
        override fun areItemsTheSame(oldItem: Option<T>, newItem: Option<T>) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Option<T>, newItem: Option<T>) =
            oldItem == newItem
    }
}