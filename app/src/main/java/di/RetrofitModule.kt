package di

import billing.Market
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xodus.templatefive.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.PhotoApi
import data.remote.PhotoHeaderInterceptor
import lang.LanguageManager
import main.ApplicationClass
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.Constant
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson =
        GsonBuilder().create()

    @Singleton
    @Provides
    fun provideHeaderInterceptor(appClass: ApplicationClass, market: Market, languageManager: LanguageManager): PhotoHeaderInterceptor =
        PhotoHeaderInterceptor(appClass, market, languageManager)


    @Provides
    @Singleton
    fun provideTemplateClient(gson: Gson, photoHeaderInterceptor: PhotoHeaderInterceptor): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(photoHeaderInterceptor)
        //.addInterceptor(GsonConverterFactory.create(gson))
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        }
        return okHttpClient
    }


    @Provides
    @Singleton
    fun provideTemplateApi(templateClient: OkHttpClient.Builder): PhotoApi =
        Retrofit
            .Builder()
            .client(templateClient.build())
            .baseUrl(Constant.CON_BASE_TEMPLATE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoApi::class.java)

}
