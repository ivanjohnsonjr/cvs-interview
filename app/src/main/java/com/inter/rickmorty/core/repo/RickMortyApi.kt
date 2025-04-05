package com.inter.rickmorty.core.repo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

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

interface RickMortyApi {

    @GET("character?")
    fun getCharactersByName(@Query("page") page: Int, @Query("name") query: String): Call<SearchResults>
}