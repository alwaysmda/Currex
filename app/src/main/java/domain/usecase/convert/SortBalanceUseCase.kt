package domain.usecase.convert

import domain.model.Rate
import domain.repository.Repository
import util.Constant
import util.PrefManager
import javax.inject.Inject

class SortBalanceUseCase @Inject constructor(private val prefManager: PrefManager, private val repository: Repository) {
    suspend operator fun invoke(list: ArrayList<Rate>, sell: Rate, receive: Rate): ArrayList<Rate> {
        val result = arrayListOf<Rate>()
        list.first { it.name == sell.name }.also { item ->
            result.add(item.copy(selected = true))
            list.remove(item)
        }
        list.first { it.name == receive.name }.also { item ->
            result.add(item.copy(selected = true))
            list.remove(item)
        }

        result.addAll(list.onEach { it.selected = false })
        repository.updateBalanceList(result)
        prefManager.setPref(Constant.PREF_SELL, sell.name)
        prefManager.setPref(Constant.PREF_RECEIVE, receive.name)
        return result
    }
}