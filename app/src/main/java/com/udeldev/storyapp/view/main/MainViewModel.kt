package com.udeldev.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.repository.story.StoryRepository
import com.udeldev.storyapp.repository.story.StoryRepositoryImpl
import com.udeldev.storyapp.repository.token.TokenRepository
import com.udeldev.storyapp.repository.token.TokenRepositoryImpl
import kotlinx.coroutines.launch


class MainViewModel(
    private val storyRepository: StoryRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    private val _story = MutableLiveData<Result<AllStoryResponse>>()
    val story: LiveData<Result<AllStoryResponse>>
        get() = _story

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getSession() {
        _token.value = tokenRepository.getSession()
    }

    fun logoutSession() {
        viewModelScope.launch {
            tokenRepository.logout()
        }
        _token.value = ""
    }

    fun getAllStory() {
        viewModelScope.launch {
            _story.value = Result.Loading(true)
            val temp = storyRepository.getAllStory(tokenRepository.getSession())
            _story.value = Result.Loading(false)
            _story.value = temp
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}