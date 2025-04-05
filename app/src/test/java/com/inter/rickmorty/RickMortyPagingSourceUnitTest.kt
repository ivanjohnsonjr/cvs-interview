package com.inter.rickmorty

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import com.inter.rickmorty.core.repo.RickMortyApi
import com.inter.rickmorty.core.repo.RickMortyPagingSource
import com.inter.rickmorty.core.repo.SearchResult
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*

class RickMortyPagingSourceUnitTest {

    @Test
    fun `test loading empty string should return empty list`() = runTest {
        val api = mockk<RickMortyApi>()
        val params = LoadParams.Prepend (
            key = 1,
            loadSize = 20,
            placeholdersEnabled = false
        )

        val pagingSource = RickMortyPagingSource(api, "")

        val result = pagingSource.load(params) as PagingSource.LoadResult.Page<Int, SearchResult>

        assertTrue("The result should be empty", result.data.isEmpty(), )
        verify(exactly = 0) { api.getCharactersByName(any(), any()) }
    }
}