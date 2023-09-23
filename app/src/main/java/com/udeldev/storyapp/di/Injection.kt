package com.udeldev.storyapp.di

import android.content.Context
import com.udeldev.storyapp.helper.preference.TokenPreference
import com.udeldev.storyapp.helper.preference.dataStore
import com.udeldev.storyapp.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = TokenPreference.getInstance(context.dataStore)
        return StoryRepository.getInstance(pref)
    }
}