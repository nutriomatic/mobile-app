package com.nutriomatic.app.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.data.remote.api.retrofit.ApiService

class AdvertisedProductPagingSource(private val apiService: ApiService) :
    PagingSource<Int, ProductsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ProductsItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getProductsAdvertise(params.loadSize, position)

            LoadResult.Page(
                data = responseData.products,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.products.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}