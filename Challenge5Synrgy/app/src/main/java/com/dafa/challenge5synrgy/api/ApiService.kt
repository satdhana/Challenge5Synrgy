package com.dafa.challenge5synrgy.api

import com.dafa.challenge5synrgy.models.GenreResponse
import com.dafa.challenge5synrgy.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") key : String?,
        @Query("page") page : Int?
    ): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") key : String?,
        @Query("query") query: String,
        @Query("page") page : Int?
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getGenre(
        @Query("api_key") key : String?,
    ): GenreResponse

}