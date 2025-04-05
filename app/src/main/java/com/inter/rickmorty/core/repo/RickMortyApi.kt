package com.inter.rickmorty.core.repo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

/**
 *  Top level Result return from search api
 */
data class SearchResults(
    val info: Info,
    val results: List<SearchResult>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}

/**
 * Character Information
 */
data class SearchResult(
    val id: Int,
    val name: String,
    val type: String? = null,
    val status: String,
    val species: String,
    val image: String,
    val origin: Origin,
    val created: Date
) {
    data class Origin(
        val name: String
    )
}

/**
 * Interface for the backend api. This will be initialize via Retrofit
 */
interface RickMortyApi {

    /**
     * Get the character with has a name that matches the query string
     *
     * @param page The page for the result
     * @param query The query string being passed to the backend
     */
    @GET("character?")
    fun getCharactersByName(@Query("page") page: Int, @Query("name") query: String): Call<SearchResults>
}