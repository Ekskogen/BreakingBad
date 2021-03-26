package com.example.breakingbad.usecases

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.data.network.Response
import com.example.breakingbad.domain.CharactersRepository
import com.example.breakingbad.domain.GetCharactersUseCase
import com.example.breakingbad.domain.GetCharactersUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest {

    lateinit var usecase: GetCharactersUseCase
    var repository: CharactersRepository = mockk()

    companion object {
        val mockedList = arrayListOf<Character>(
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
        usecase = GetCharactersUseCaseImpl(repository)
    }

    @Test
    fun execute_FailureFromNetwork_WillReturnFailure() {
        coEvery { repository.getCharacters() }  returns Response.Failure()

        runBlocking {
            val response = usecase.execute()
            coVerify(exactly = 1) { repository.getCharacters() }
            assertTrue(response is Response.Failure)
        }
    }

    @Test
    fun execute_SuccessAndNoFilters_WillReturnFullList() {
        coEvery { repository.getCharacters() }  returns Response.Success(mockedList)

        runBlocking {
            val response = usecase.execute()
            coVerify(exactly = 1) { repository.getCharacters() }
            assertTrue(response is Response.Success && response.result == mockedList)
        }
    }

    @Test
    fun execute_SuccessFilterSeason_WillReturnFilteredList() {
        coEvery { repository.getCharacters() }  returns Response.Success(mockedList)

        runBlocking {
            val response = usecase.execute(season = 5)
            assertTrue(response is Response.Success && response.result.size == 7)

            val response2 = usecase.execute(season = 1)
            assertTrue(response2 is Response.Success && response2.result.size == 6)

            val response3 = usecase.execute(season = 0)
            assertTrue(response3 is Response.Success && response3.result.size == 0)
        }
    }

    @Test
    fun execute_SuccessFilterName_WillReturnFilteredListNoSensitiveCase() {
        coEvery { repository.getCharacters() }  returns Response.Success(mockedList)

        runBlocking {
            val response = usecase.execute(name = "WaltEr")
            assertTrue(response is Response.Success && response.result.size == 2)

            val response2 = usecase.execute(name = "he")
            assertTrue(response2 is Response.Success && response2.result.size == 2)

            val response3 = usecase.execute(name = "rogelio")
            assertTrue(response3 is Response.Success && response3.result.size == 0)
        }
    }

    @Test
    fun execute_GetByIdRealId_WillReturnSuccessCharacter() {
        coEvery { repository.getCharacters() }  returns Response.Success(mockedList)

        runBlocking {
            val response = usecase.execute(id=8)
            assertTrue(response is Response.Success && response.result[0].name == "here")

            val response2 = usecase.execute(id=0)
            assertTrue(response2 is Response.Success && response2.result[0].name == "walter")
        }
    }

    @Test
    fun execute_GetByIdFakeId_WillReturnFailure() {
        coEvery { repository.getCharacters() }  returns Response.Success(mockedList)

        runBlocking {
            val response = usecase.execute(id=123)
            assertTrue(response is Response.Failure)
        }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}