package com.udeldev.storyapp.helper.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udeldev.storyapp.di.Injection
import com.udeldev.storyapp.repository.auth.AuthRepository
import com.udeldev.storyapp.repository.auth.AuthRepositoryImpl
import com.udeldev.storyapp.repository.story.StoryRepository
import com.udeldev.storyapp.repository.story.StoryRepositoryImpl
import com.udeldev.storyapp.repository.token.TokenRepository
import com.udeldev.storyapp.repository.token.TokenRepositoryImpl
import com.udeldev.storyapp.view.add.AddViewModel
import com.udeldev.storyapp.view.detail.DetailViewModel
import com.udeldev.storyapp.view.login.LoginViewModel
import com.udeldev.storyapp.view.main.MainViewModel
import com.udeldev.storyapp.view.maps.MapsViewModel
import com.udeldev.storyapp.view.register.RegisterViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository, tokenRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository, tokenRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(storyRepository, tokenRepository) as T
        } else if (modelClass.isAssignableFrom(AddViewModel::class.java)){
            return AddViewModel(storyRepository, tokenRepository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)){
            return MapsViewModel(storyRepository, tokenRepository) as T
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