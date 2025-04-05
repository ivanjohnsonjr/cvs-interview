package com.inter.rickmorty.core.repo

import androidx.core.net.toUri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import okio.IOException

internal class RickMortyPagingSource(
    private val api: RickMortyApi,
    private val query: String
) : PagingSource<Int, SearchResult>(){

    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        return try {
            val page = params.key ?: 1
            val response = api.getCharactersByName(page, query).execute()

            if (response.isSuccessful) {
                val info = response.body()!!.info
                val results = response.body()!!.results

                val nextKey = info.next?.toUri()?.getQueryParameters("page")?.first()?.toInt()
                val previousKey = info.prev?.toUri()?.getQueryParameters("page")?.first()?.toInt()

                LoadResult.Page(
                    data = results,
                    prevKey = previousKey,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Exception(response.message()))
            }
        }
        catch (ioe: IOException) {
            LoadResult.Error(ioe)
        }
        catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}