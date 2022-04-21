package di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import domain.repository.Repository
import domain.usecase.convert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import main.ApplicationClass
import util.PrefManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplicationClass(@ApplicationContext app: Context): ApplicationClass =
        app as ApplicationClass

    @Singleton
    @Provides
    fun providePrefManager(app: ApplicationClass): PrefManager =
        PrefManager(app)

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideConvertUseCases(app: ApplicationClass, repository: Repository): ConvertUseCases {
        return ConvertUseCases(
            GetExchangeRateUseCase(repository),
            AddMissingBalanceUseCase(app),
            SortBalanceUseCase(app),
            ConvertRateUseCase(app),
        )
    }

}
