package com.example.numbers.di

import android.content.Context
import androidx.room.Room
import com.example.numbers.network.NumberApi
import com.example.numbers.room.NumberDao
import com.example.numbers.room.NumberDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NumberModule {
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): NumberDatabase =
        Room.databaseBuilder(context, NumberDatabase::class.java, "numbers_table").build()

    @Provides
    @Singleton
    fun provideDao(database: NumberDatabase): NumberDao = database.numberDao()


    @Provides
    fun providesOxfordApi(client: OkHttpClient): NumberApi =
        Retrofit.Builder()
            .baseUrl("http://numbersapi.com")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(NumberApi::class.java)

    @Provides
    fun providesHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        return client
    }
}