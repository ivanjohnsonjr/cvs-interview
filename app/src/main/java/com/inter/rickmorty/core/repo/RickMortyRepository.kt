package com.inter.rickmorty.core.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.inter.rickmorty.core.CoreConstants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The interface for the Repository
 */
interface RickMortyRepository {
    fun getSearchPaginationData(query: String): Flow<PagingData<SearchResult>>
}


/**
 * Repository implementation that will fetch the paging information
 *
 * @param api Service api that will be call when fetch character information
 */
class RickMortyRepositoryImpl @Inject constructor(
    private val api: RickMortyApi
) : RickMortyRepository {

    /**
     * Fetching Pagination Information as a flow
     *
     * @param query The query string
     */
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