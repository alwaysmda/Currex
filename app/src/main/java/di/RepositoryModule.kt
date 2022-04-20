package di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.Api
import data.remote.dto.NetworkErrorMapper
import data.remote.dto.ResponseExchangeRateMapper
import data.repository.RepositoryImpl
import domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import main.ApplicationClass
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTemplateRepository(app: ApplicationClass, api: Api, networkErrorMapper: NetworkErrorMapper, responseExchangeRateMapper: ResponseExchangeRateMapper): Repository =
        RepositoryImpl(app, api, networkErrorMapper, responseExchangeRateMapper)
}