package com.example.breakingbad.domain

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.data.network.Response
import javax.inject.Inject

class GetCharactersUseCaseImpl @Inject constructor(val repository: CharactersRepository): GetCharactersUseCase {

    override suspend fun execute(season: Int?, name: String?, id: Int?): Response<ArrayList<Character>> {
        val response = repository.getCharacters()
        if(response is Response.Failure<*>) return response

        val characters = (response as Response.Success).result

        // Filter by season
        return if (season != null) {
            val c = characters.filter { it.appearance?.contains(season) ?: false } as ArrayList
            Response.Success(c)
        // Filter by name
        } else if(!name.isNullOrBlank()) {
            val c = characters.filter {
                it.name?.toLowerCase()?.contains(name.toLowerCase()) ?: false
            } as ArrayList
            Response.Success(c)
        } else if(id != null) {
            characters.firstOrNull { it.charId == id }?.let {
                Response.Success(arrayListOf(it))
            } ?: Response.Failure()
        } else {
            response
        }
    }

}