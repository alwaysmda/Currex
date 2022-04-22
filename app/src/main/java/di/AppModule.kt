package di

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.room.Room
import com.example.currex.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import data.db.BalanceDatabase
import data.db.RateDao
import data.remote.Api
import data.remote.dto.RatesMapper
import data.repository.RepositoryImpl
import domain.repository.Repository
import domain.usecase.convert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import main.ApplicationClass
import util.AppSetting
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
    fun provideRecyclerViewAnimation(app: ApplicationClass): LayoutAnimationController =
        AnimationUtils.loadLayoutAnimation(app, R.anim.anim_layout_animation)

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideConvertUseCases(app: ApplicationClass, repository: Repository): ConvertUseCases {
        return ConvertUseCases(
            GetExchangeRateUseCase(app.appSetting, repository),
            GetBalanceListUseCase(repository),
            SortBalanceUseCase(app.prefManager, repository),
            ConvertRateUseCase(app),
            ApplyConvertUseCase(app, repository),
        )
    }

    @Provides
    @Singleton
    fun provideRepository(app: ApplicationClass, api: Api, rateDao: RateDao, ratesMapper: RatesMapper): Repository =
        RepositoryImpl(app, api, rateDao, ratesMapper)


    @Singleton
    @Provides
    fun provideBalanceDatabase(app: ApplicationClass) =
        Room.databaseBuilder(
            app,
            BalanceDatabase::class.java,
            "db_balance"
        ).build()

    @Provides
    @Singleton
    fun provideRateDao(db: BalanceDatabase): RateDao =
        db.getRateDao()

    @Provides
    @Singleton
    fun provideAppSetting(prefManager: PrefManager): AppSetting =
        AppSetting(prefManager)


}
