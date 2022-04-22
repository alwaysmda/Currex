package domain.usecase.convert

import domain.model.Rate
import domain.repository.Repository
import javax.inject.Inject


class GetBalanceListUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(): ArrayList<Rate> {
        return repository.getBalanceList()
    }
}