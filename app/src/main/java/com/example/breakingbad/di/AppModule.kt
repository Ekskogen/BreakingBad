package com.example.breakingbad.di

import com.example.breakingbad.data.network.CharacterService
import com.example.breakingbad.data.repositories.CharactersRepositoryImpl
import com.example.breakingbad.domain.CharactersRepository
import com.example.breakingbad.domain.GetCharactersUseCase
import com.example.breakingbad.domain.GetCharactersUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideGetCharactersUseCase(repository: CharactersRepository): GetCharactersUseCase = GetCharactersUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideRepository(service: CharacterService): CharactersRepository = CharactersRepositoryImpl(service)
}
