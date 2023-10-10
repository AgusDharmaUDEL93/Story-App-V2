package com.udeldev.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.entity.ListStoryItem
import com.udeldev.storyapp.repository.story.StoryRepository
import com.udeldev.storyapp.repository.token.TokenRepository
import kotlinx.coroutines.launch


class MainViewModel(
    private val storyRepository: StoryRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    private val _story = MutableLiveData<Result<LiveData<PagingData<ListStoryItem>>>>()
    val story: LiveData<Result<LiveData<PagingData<ListStoryItem>>>>
        get() = _story

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getSession() {
        _token.value = tokenRepository.getSession()
    }

    fun getAllStoryPaging(){
        _story.value = Result.Loading(true)
        val temp =  Result.Success(storyRepository.getAllStory(tokenRepository.getSession()).cachedIn(viewModelScope))
        _story.value = Result.Loading(false)
        _story.value = temp
    }

    fun logoutSession() {
        viewModelScope.launch {
            tokenRepository.logout()
        }
        _token.value = ""
    }
}