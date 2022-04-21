package di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.Api
import data.remote.dto.RatesMapper
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
    fun provideRepository(app: ApplicationClass, api: Api, ratesMapper: RatesMapper): Repository =
        RepositoryImpl(app, api, ratesMapper)
}