package tech.hiregus.trendingprojects.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tech.hiregus.trendingprojects.data.response.SearchResponse

interface ReposService {

    @GET("search/repositories")
    suspend fun search(
        @Query("sort") sort: String?,
        @Query("order") order: String?,
        @Query("q") query: List<String>?,
        @Query("per_page") perPage: Int?,
        @Query("page") page: Int?
    ): Response<SearchResponse>

    @GET("repos/{owner}/{repo}/languages")
    suspend fun getRepoLanguages(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Map<String, Long>>

}