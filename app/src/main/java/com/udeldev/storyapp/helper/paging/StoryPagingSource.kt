package com.udeldev.storyapp.helper.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.udeldev.storyapp.model.entity.ListStoryItem
import com.udeldev.storyapp.provider.config.ApiConfig

class StoryPagingSource(private val token : String) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                ApiConfig.getApiService().getAllStory("Bearer $token", position, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory as List<ListStoryItem>,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}