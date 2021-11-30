package di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.PhotoApi
import data.remote.dto.NetworkErrorMapper
import data.remote.dto.PhotoMapper
import data.repository.PhotoRepositoryImpl
import domain.repository.PhotoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import main.ApplicationClass
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTemplateRepository(app: ApplicationClass, api: PhotoApi, photoMapper: PhotoMapper, networkErrorMapper: NetworkErrorMapper): PhotoRepository =
        PhotoRepositoryImpl(app, api, networkErrorMapper, photoMapper)
}