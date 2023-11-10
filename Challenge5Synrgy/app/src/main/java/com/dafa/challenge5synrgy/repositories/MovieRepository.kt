package com.dafa.challenge5synrgy.repositories

import android.os.Build
import com.dafa.challenge5synrgy.BuildConfig
import com.dafa.challenge5synrgy.api.ApiConfig
import retrofit2.http.Query

class MovieRepository {
    private val client = ApiConfig.getApiService()

    suspend fun getPopularMovie(page: Int) = client.getPopularMovie(BuildConfig.API_KEY, page)
    suspend fun searchMovie(query: String, page: Int) = client.searchMovie(BuildConfig.API_KEY, query,page)
    suspend fun getGenres() = client.getGenre(BuildConfig.API_KEY)
}