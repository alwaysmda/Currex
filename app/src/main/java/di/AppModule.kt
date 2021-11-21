package di

import android.content.Context
import billing.Market
import com.xodus.templatefive.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import domain.repository.PhotoRepository
import domain.usecase.photo.GetPhoto
import domain.usecase.photo.GetPhotoList
import domain.usecase.photo.PhotoUseCases
import lang.LanguageManager
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

    @Singleton
    @Provides
    fun provideLanguageManager(app: ApplicationClass, prefManager: PrefManager): LanguageManager =
        LanguageManager(app, prefManager)

    @Singleton
    @Provides
    fun provideMarket(): Market = when {
        BuildConfig.FLAVOR.contains("bazaar")     -> Market.init(Market.MarketType.BAZAAR)
        BuildConfig.FLAVOR.contains("myket")      -> Market.init(Market.MarketType.MYKET)
        BuildConfig.FLAVOR.contains("iranapps")   -> Market.init(Market.MarketType.IRANAPPS)
        BuildConfig.FLAVOR.contains("googleplay") -> Market.init(Market.MarketType.GOOGLEPLAY)
        else                                      -> Market.init(Market.MarketType.BAZAAR)
    }

    @Singleton
    @Provides
    fun providePhotoUseCases(photoRepository: PhotoRepository): PhotoUseCases {
        return PhotoUseCases(
            GetPhoto(photoRepository),
            GetPhotoList(photoRepository)
        )
    }
}
