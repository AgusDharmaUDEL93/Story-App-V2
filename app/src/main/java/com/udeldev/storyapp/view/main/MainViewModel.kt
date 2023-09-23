package com.udeldev.storyapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch


class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _story = MutableLiveData<Result<AllStoryResponse>>()
    val story: LiveData<Result<AllStoryResponse>>
        get() = _story

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getSession() {
        _token.value = storyRepository.getSession()
    }

    fun logoutSession() {
        viewModelScope.launch {
            storyRepository.logout()
        }
        _token.value = ""
    }

    fun getAllStory() {
        storyRepository.getAllStory().observeForever {
            _story.value = it
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}