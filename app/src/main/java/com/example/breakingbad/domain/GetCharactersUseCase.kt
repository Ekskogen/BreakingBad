package com.example.breakingbad.domain

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.data.network.Response

interface GetCharactersUseCase {
    suspend fun execute(season: Int? = null, name: String? = null, id: Int? = null): Response<ArrayList<Character>>
}