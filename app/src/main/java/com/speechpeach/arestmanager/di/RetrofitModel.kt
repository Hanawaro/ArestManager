package com.speechpeach.arestmanager.di

import com.speechpeach.arestmanager.servicies.ArestService
import com.speechpeach.arestmanager.servicies.UserService
import com.speechpeach.arestmanager.utils.ValueConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun provideUserService(
        retrofit: Retrofit
    ) = retrofit.create<UserService>()

    @Provides
    @Singleton
    fun provideArestService(
        retrofit: Retrofit
    ) = retrofit.create<ArestService>()

    @Provides
    @Singleton
    fun provideRetrofit(
        converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(ValueConstants.Retrofit.URL)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

}