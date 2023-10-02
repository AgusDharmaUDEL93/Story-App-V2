package com.udeldev.storyapp.view.login

import androidx.lifecycle.*
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.LoginResponse
import com.udeldev.storyapp.repository.auth.AuthRepositoryImpl
import com.udeldev.storyapp.repository.story.StoryRepositoryImpl
import com.udeldev.storyapp.repository.token.TokenRepositoryImpl
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepositoryImpl,
    private val tokenRepository: TokenRepositoryImpl,
) :
    ViewModel() {

    private val _user = MutableLiveData<Result<LoginResponse>>()
    val user: LiveData<Result<LoginResponse>>
        get() = _user

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    init {
        _user.value = Result.Loading(false)
    }

    fun loginUser(email: String, password: String) {
        _user.value = Result.Loading(true)
        viewModelScope.launch {
            _user.value = authRepository.loginUser(email, password)
            _user.value = Result.Loading(false)
        }
    }

    fun saveSession(token: String) {
        _token.value = token
        viewModelScope.launch {
            tokenRepository.saveSession(token)
        }
    }


    companion object {
        private const val TAG = "LoginViewModel"
    }
}