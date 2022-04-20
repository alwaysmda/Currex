package di

import com.android.currex.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.Api
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

    @Provides
    @Singleton
    fun provideTemplateClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        //.addInterceptor(GsonConverterFactory.create(gson))
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        }
        return okHttpClient
    }

    @Provides
    @Singleton
    fun provideTemplateApi(gson: Gson, templateClient: OkHttpClient.Builder): Api =
        Retrofit
            .Builder()
            .client(templateClient.build())
            .baseUrl(Constant.CON_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Api::class.java)
}
