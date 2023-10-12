package com.udeldev.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.repository.story.StoryRepository
import com.udeldev.storyapp.repository.token.TokenRepository
import kotlinx.coroutines.launch

class MapsViewModel (
    private val storyRepository: StoryRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _storyLocation = MutableLiveData<Result<AllStoryResponse>>()
    val storyLocation : LiveData<Result<AllStoryResponse>>
        get() = _storyLocation

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getSession() {
        _token.value = tokenRepository.getSession()
    }

    fun getAllStoryLocation (){
        viewModelScope.launch {
            _storyLocation.value = Result.Loading(true)
            val temp = storyRepository.getAllStoryLocation(tokenRepository.getSession())
            _storyLocation.value = Result.Loading(false)
            _storyLocation.value = temp
        }
    }

}