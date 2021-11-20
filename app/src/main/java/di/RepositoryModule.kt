package di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.PhotoApi
import data.repository.PhotoRepositoryImpl
import domain.repository.PhotoRepository
import main.ApplicationClass
import util.PhotoMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTemplateRepository(app: ApplicationClass, api: PhotoApi, networkMapper: PhotoMapper): PhotoRepository =
        PhotoRepositoryImpl(app, api, networkMapper)
}