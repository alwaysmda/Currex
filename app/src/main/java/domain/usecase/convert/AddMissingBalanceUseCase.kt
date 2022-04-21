package domain.usecase.convert

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import domain.model.Rate
import main.ApplicationClass
import util.Constant
import java.lang.reflect.Type
import javax.inject.Inject


class AddMissingBalanceUseCase @Inject constructor(private val app: ApplicationClass) {
    operator fun invoke(list: ArrayList<Rate>): ArrayList<Rate> {
        val listType: Type = object : TypeToken<ArrayList<Rate>>() {}.type
        val balanceList = Gson().fromJson<ArrayList<Rate>>(app.prefManager.getStringPref(Constant.PREF_BALANCE) ?: "[]", listType)
        if (balanceList.isEmpty()) {
            balanceList.add(Rate("EUR", 1000.0))
        }
        balanceList.addAll(list.filter { it.name !in balanceList.map { item -> item.name }.toSet() }.map { Rate(it.name, 0.0) })
        app.prefManager.setPref(Constant.PREF_BALANCE, Gson().toJson(balanceList))
        return balanceList
    }
}