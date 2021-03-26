package com.example.breakingbad.domain

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.data.network.Response

interface CharactersRepository {
    suspend fun getCharacters(): Response<ArrayList<Character>>
}