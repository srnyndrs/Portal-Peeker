package com.srnyndrs.android.portalpeeker.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String
    ): ResponseDto
}