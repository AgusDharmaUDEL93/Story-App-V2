package com.udeldev.storyapp.helper.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udeldev.storyapp.di.Injection
import com.udeldev.storyapp.repository.auth.AuthRepositoryImpl
import com.udeldev.storyapp.repository.story.StoryRepositoryImpl
import com.udeldev.storyapp.repository.token.TokenRepositoryImpl
import com.udeldev.storyapp.view.login.LoginViewModel
import com.udeldev.storyapp.view.main.MainViewModel

class ViewModelFactory private constructor(
    private val storyRepositoryImpl: StoryRepositoryImpl,
    private val tokenRepositoryImpl: TokenRepositoryImpl,
    private val authRepositoryImpl: AuthRepositoryImpl
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepositoryImpl, tokenRepositoryImpl) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepositoryImpl, tokenRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    StoryRepositoryImpl(),
                    Injection.provideTokenRepository(context),
                    AuthRepositoryImpl(),
                )
            }.also { instance = it }
    }
}