package com.journiapp.stampapplication.di

import android.content.Context
import com.google.gson.Gson
import com.journiapp.stampapplication.common.SharedPrefHelper
import com.journiapp.stampapplication.data.datasource.remote.StampRemoteDataSource
import com.journiapp.stampapplication.data.network.NetworkUtil
import com.journiapp.stampapplication.data.repository.StampRepository
import com.journiapp.stampapplication.model.API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return NetworkUtil.providesOkHttp(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(NetworkUtil.BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson())).build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        api: API,
        sharedPrefHelper: SharedPrefHelper
    ): StampRemoteDataSource {
        return StampRemoteDataSource(api, sharedPrefHelper)
    }

    @Provides
    @Singleton
    fun provideStampRepository(remoteDataSource: StampRemoteDataSource): StampRepository {
        return StampRepository(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideSharedPrefHelper(@ApplicationContext context: Context): SharedPrefHelper {
        return SharedPrefHelper(context)
    }

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}