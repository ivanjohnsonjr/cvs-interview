package com.inter.rickmorty.core

import com.google.gson.GsonBuilder
import com.inter.rickmorty.core.repo.RickMortyApi
import com.inter.rickmorty.core.repo.RickMortyRepository
import com.inter.rickmorty.core.repo.RickMortyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object CoreDi {
    private val BASE_URL = "https://rickandmortyapi.com/api/"
    private val retrofitBuilder: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder().apply {
                this.addInterceptor(interceptor)
                    // time out setting
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .writeTimeout(25,TimeUnit.SECONDS)

            }.build()

            val gsonBuilder = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build()
        }

    @Provides
    fun provideRickMortyApi(): RickMortyApi {
        return retrofitBuilder.create(RickMortyApi::class.java)
    }

    @Provides
    fun provideRepository(
        api: RickMortyApi
    ): RickMortyRepository {
        return RickMortyRepositoryImpl(api)
    }

}