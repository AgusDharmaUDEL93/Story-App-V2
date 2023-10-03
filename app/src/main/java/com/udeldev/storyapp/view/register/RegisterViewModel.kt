package com.udeldev.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.BasicResponse
import com.udeldev.storyapp.model.response.LoginResponse
import com.udeldev.storyapp.repository.auth.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _response = MutableLiveData<Result<BasicResponse>>()
    val response: LiveData<Result<BasicResponse>>
        get() = _response

    init {
        _response.value = Result.Loading(false)
    }

    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            _response.value = Result.Loading(true)
            _response.value = authRepository.registerUser(name, email, password)
        }
    }


}