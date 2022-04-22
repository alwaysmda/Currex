package domain.usecase.convert

data class ConvertUseCases(
    val getExchangeRateUseCase: GetExchangeRateUseCase,
    val getBalanceListUseCase: GetBalanceListUseCase,
    val sortBalanceUseCase: SortBalanceUseCase,
    val convertRateUseCase: ConvertRateUseCase,
    val applyConvertUseCase: ApplyConvertUseCase,
)
