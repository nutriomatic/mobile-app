package com.nutriomatic.app.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nutriomatic.app.data.remote.api.response.NutritionScan
import com.nutriomatic.app.data.remote.api.retrofit.ApiService

class NutritionScanPagingSource(private val apiService: ApiService) :
    PagingSource<Int, NutritionScan>() {
    override fun getRefreshKey(state: PagingState<Int, NutritionScan>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NutritionScan> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                apiService.getNutritionScanByUserId(1, "created_at", params.loadSize, position)

            LoadResult.Page(
                data = responseData.nutritionScans,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.nutritionScans.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}