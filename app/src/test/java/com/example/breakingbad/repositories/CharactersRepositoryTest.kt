package com.example.breakingbad.repositories

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.data.network.CharacterService
import com.example.breakingbad.data.network.Response
import com.example.breakingbad.data.repositories.CharactersRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException


class CharactersRepositoryTest {

    lateinit var repository: CharactersRepositoryImpl
    var service: CharacterService = mockk()

    companion object {
        val mockedList = arrayListOf(
            Character(charId = 0, name = "walter", appearance = listOf(1,2,3,4,5)),
            Character(charId = 1, name = "walter jr", appearance = listOf(1,2,3,4,5)),
            Character(charId = 2, name = "hey", appearance = listOf(1,2)),
            Character(charId = 3, name = "this", appearance = listOf(4,5)),
            Character(charId = 4, name = "are", appearance = listOf(1,5)),
            Character(charId = 5, name = "testing", appearance = listOf(4,5)),
            Character(charId = 6, name = "names", appearance = listOf(1)),
            Character(charId = 7, name = "in", appearance = listOf(4,5)),
            Character(charId = 8, name = "here", appearance = listOf(3,4,5)),
            Character(charId = 9, name = "ok", appearance = listOf(1)),
        )
    }

    @Before
    fun setUp() {
        repository = CharactersRepositoryImpl(service)
    }

    @Test
    fun getCharacters_NotCachedNetworkSuccessful_WillReturnSuccessWithList() {
        repository.characters = arrayListOf()
        coEvery { service.getCharacters() }  returns retrofit2.Response.success(mockedList)

        runBlocking {
            val response = repository.getCharacters()
            coVerify(exactly = 1) { service.getCharacters() }
            assertTrue(response is Response.Success && response.result == mockedList)
        }
    }

    @Test
    fun getCharacters_NotCachedNetworkSuccessfulNull_WillReturnFailure() {
        repository.characters = arrayListOf()
        coEvery { service.getCharacters() }  returns retrofit2.Response.success(null)

        runBlocking {
            val response = repository.getCharacters()
            coVerify(exactly = 1) { service.getCharacters() }
            assertTrue(response is Response.Failure)
        }
    }

    @Test
    fun getCharacters_NotCachedNetworkNotSuccessful_WillReturnFailure() {
        repository.characters = arrayListOf()
        coEvery { service.getCharacters() }  returns retrofit2.Response.error(404, ResponseBody.create(null,""))

        runBlocking {
            val response = repository.getCharacters()
            coVerify(exactly = 1) { service.getCharacters() }
            assertTrue(response is Response.Failure)
        }
    }

    @Test
    fun getCharacters_NotCachedNetworkNoInternet_WillReturnFailure() {
        repository.characters = arrayListOf()
        coEvery { service.getCharacters() }  throws SocketTimeoutException()

        runBlocking {
            val response = repository.getCharacters()
            coVerify(exactly = 1) { service.getCharacters() }
            assertTrue(response is Response.Failure)
        }
    }

    @Test
    fun getCharacters_CachedList_WillReturnCachedList() {
        repository.characters = mockedList

        runBlocking {
            val response = repository.getCharacters()
            coVerify(exactly = 0) { service.getCharacters() }
            assertTrue(response is Response.Success && response.result == mockedList)
        }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}