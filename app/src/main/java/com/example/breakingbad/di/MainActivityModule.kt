package com.example.breakingbad.di

import android.content.Context
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.breakingbad.domain.GetCharactersUseCase
import com.example.breakingbad.ui.main.MainContract
import com.example.breakingbad.ui.main.MainPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @ActivityScoped
    @Provides
    fun provideMainPresenter(getCharactersUseCase: GetCharactersUseCase): MainContract.Presenter
            = MainPresenter(getCharactersUseCase)
}