package com.udeldev.storyapp.di

import android.content.Context
import com.udeldev.storyapp.data.story.StoryDatabase
import com.udeldev.storyapp.helper.preference.TokenPreference
import com.udeldev.storyapp.helper.preference.dataStore
import com.udeldev.storyapp.repository.story.StoryRepositoryImpl
import com.udeldev.storyapp.repository.token.TokenRepositoryImpl

object Injection {
    fun provideTokenRepository(context: Context) : TokenRepositoryImpl {
        val pref = TokenPreference.getInstance(context.dataStore)
        return TokenRepositoryImpl.getInstance(pref)
    }

    fun  provideStoryRepository (context: Context) : StoryRepositoryImpl {
        val database = StoryDatabase.getDatabase(context)
        return StoryRepositoryImpl(database)
    }
}