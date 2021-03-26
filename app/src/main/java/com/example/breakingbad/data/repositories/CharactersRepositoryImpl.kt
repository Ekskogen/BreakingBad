package com.example.breakingbad.data.repositories

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.data.network.CharacterService
import com.example.breakingbad.data.network.NO_INTERNET
import com.example.breakingbad.data.network.Response
import com.example.breakingbad.domain.CharactersRepository
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(val service: CharacterService):
    CharactersRepository {

    // Cache characters
    var characters: ArrayList<Character> = arrayListOf()

    /**
     * Will get characters. The first time from Network, the next times from cache variable.
     * Implementation of DB is missing to persist values.
     */
    override suspend fun getCharacters(): Response<ArrayList<Character>> {
        if(!characters.isNullOrEmpty()) return Response.Success(characters)
        return try {
            val response = service.getCharacters()
            if(response.isSuccessful) {
                response.body()?.let {
                    characters = it
                    Response.Success(it)
                } ?: Response.Failure()
            } else {
                Response.Failure()
            }
        } catch (e: Exception) {
            if(e is UnknownHostException) Response.Failure(NO_INTERNET)
            else Response.Failure()
        }
    }

}