package com.alma.mlcconvertrer.di

import com.alma.mlcconvertrer.BuildConfig
import com.alma.mlcconvertrer.data.remote.CoinApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{

        return Retrofit.Builder().baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCoinApi(retrofit: Retrofit): CoinApi{
        return retrofit.create(CoinApi::class.java)
    }

}