package com.example.breakingbad.data.network

import com.example.breakingbad.data.models.Character
import retrofit2.Response
import retrofit2.http.GET

interface CharacterService {

    @GET("api/characters")
    suspend fun getCharacters(): Response<ArrayList<Character>>

}