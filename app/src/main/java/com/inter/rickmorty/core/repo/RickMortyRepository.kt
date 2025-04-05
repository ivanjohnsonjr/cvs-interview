package com.inter.rickmorty.core.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.inter.rickmorty.core.CoreConstants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RickMortyRepository {
    fun getSearchPaginationData(query: String): Flow<PagingData<SearchResult>>
}


class RickMortyRepositoryImpl @Inject constructor(
    private val api: RickMortyApi
) : RickMortyRepository {

    override fun getSearchPaginationData(query: String): Flow<PagingData<SearchResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = CoreConstants.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = CoreConstants.PAGE_SIZE,
                prefetchDistance = CoreConstants.PAGE_SIZE
            ),
            pagingSourceFactory = { RickMortyPagingSource(api, query) }
        ).flow
    }

}