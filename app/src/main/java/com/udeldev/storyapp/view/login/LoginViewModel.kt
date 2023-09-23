package com.udeldev.storyapp.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.LoginResponse
import com.udeldev.storyapp.repository.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val response = MutableLiveData<Result<LoginResponse>>()

    fun loginUser(email: String, password:String) {
//       val responseLogin = storyRepository.loginUser(email, password)
    }

    companion object{
        private const val TAG = "LoginViewModel"
    }
}