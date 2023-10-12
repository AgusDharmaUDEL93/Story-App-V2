package com.udeldev.storyapp.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.DetailStoryResponse
import com.udeldev.storyapp.repository.story.StoryRepository
import com.udeldev.storyapp.repository.token.TokenRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val storyRepository: StoryRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    private val _detailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val detailStory: LiveData<Result<DetailStoryResponse>>
        get() = _detailStory

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getSession() {
        _token.value = tokenRepository.getSession()
    }

    fun getDetailStory (id:String){
        viewModelScope.launch {
            _detailStory.value = Result.Loading(true)
            val temp = storyRepository.getDetailStory(tokenRepository.getSession(), id)
            _detailStory.value = Result.Loading(false)
            _detailStory.value = temp
        }
    }

}