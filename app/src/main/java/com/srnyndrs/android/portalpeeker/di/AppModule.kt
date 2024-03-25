package com.srnyndrs.android.portalpeeker.di

import android.content.Context
import androidx.room.Room
import com.srnyndrs.android.portalpeeker.data.local.CharacterDatabase
import com.srnyndrs.android.portalpeeker.data.remote.CharacterApi
import com.srnyndrs.android.portalpeeker.data.repository.CharacterRepositoryImpl
import com.srnyndrs.android.portalpeeker.domain.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCharacterDatabase(@ApplicationContext context: Context): CharacterDatabase {
        return Room.databaseBuilder(
            context,
            CharacterDatabase::class.java,
            "characters.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCharacterApi(): CharacterApi {
        return Retrofit.Builder()
            .baseUrl(CharacterApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(
        database: CharacterDatabase,
        api: CharacterApi
    ): CharacterRepository {
        return CharacterRepositoryImpl(
            database = database,
            api = api
        )
    }
}