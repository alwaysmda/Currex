package domain.usecase.convert

data class ConvertUseCases(
    val getExchangeRateUseCase: GetExchangeRateUseCase,
    val addMissingBalanceUseCase: AddMissingBalanceUseCase,
    val sortBalanceUseCase: SortBalanceUseCase,
    val convertRateUseCase: ConvertRateUseCase,
)
