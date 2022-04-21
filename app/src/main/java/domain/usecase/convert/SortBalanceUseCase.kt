package domain.usecase.convert

import com.google.gson.Gson
import domain.model.Rate
import main.ApplicationClass
import util.Constant
import javax.inject.Inject

class SortBalanceUseCase @Inject constructor(private val app: ApplicationClass) {
    operator fun invoke(list: ArrayList<Rate>, sell: Rate, receive: Rate): ArrayList<Rate> {
        val result = arrayListOf<Rate>()
        list.first { it.name == sell.name }.also { item ->
            result.add(item.copy(selected = true))
            list.remove(item)
        }
        list.first { it.name == receive.name }.also { item ->
            result.add(item.copy(selected = true))
            list.remove(item)
        }

        result.addAll(list)
        app.prefManager.setPref(Constant.PREF_BALANCE, Gson().toJson(result))
        app.prefManager.setPref(Constant.PREF_SELL, sell.name)
        app.prefManager.setPref(Constant.PREF_RECEIVE, receive.name)
        return result
    }
}