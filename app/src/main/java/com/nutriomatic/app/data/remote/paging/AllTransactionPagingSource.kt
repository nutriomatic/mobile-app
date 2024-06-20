package com.nutriomatic.app.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nutriomatic.app.data.remote.api.response.Transaction
import com.nutriomatic.app.data.remote.api.retrofit.ApiService

class AllTransactionPagingSource(private val apiService: ApiService, private val status: String) :
    PagingSource<Int, Transaction>() {
    override fun getRefreshKey(state: PagingState<Int, Transaction>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllTransactions(1, "created_at",status, params.loadSize, position)

            LoadResult.Page(
                data = responseData.transactions,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.transactions.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}