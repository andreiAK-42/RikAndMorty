package com.example.rickandmorty

import com.example.rickandmorty.ViewModel.MainViewModel
import com.example.rickandmorty.models.CharactersModelAPI
import com.example.rickandmorty.models.EpisodesModelAPI
import com.example.rickandmorty.repository.MainRepository
import org.junit.Assert.*
import org.junit.Assert.assertEquals
import org.junit.Test
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    @MockK
    private lateinit var mockedRepository: MainRepository
    private lateinit var viewModel: MainViewModel

    val testCharacterInfo = CharactersModelAPI.Info(count = 2, pages = 1, next = "", prev = "")
    val testCharacterOrigin = CharactersModelAPI.Origin(name = "Earth", url = "http://example.com")
    val testCharacterLocation = CharactersModelAPI.Location(name = "Citadel of Ricks", url = "http://example.com")
    val testCharacterResult = listOf(
        CharactersModelAPI.Result(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = testCharacterOrigin,
            location = testCharacterLocation,
            image = "http://example.com/image.jpg",
            episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            url = "http://example.com/rick",
            created = "2021-01-01T00:00:00Z"
        ),
        CharactersModelAPI.Result(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = testCharacterOrigin,
            location = testCharacterLocation,
            image = "http://example.com/morty.jpg",
            episode = listOf("https://rickandmortyapi.com/api/episode/2"),
            url = "http://example.com/morty",
            created = "2021-01-01T00:00:00Z"
        )
    )

    val testEpisodeInfo = EpisodesModelAPI.Info(count = 2, pages = 1, next = "", prev = "")
    val testEpisodeResult = mutableListOf(
        EpisodesModelAPI.Episode(
            id = 1,
            name = "Pilot",
            air_date = "December 2, 2013",
            episode = "S01E01",
            characters = emptyList(),
            url = "http://example.com/pilot",
            created = "2021-01-01T00:00:00Z"
        ),
        EpisodesModelAPI.Episode(
            id = 2,
            name = "Lawnmower Dog",
            air_date = "December 9, 2013",
            episode = "S01E02",
            characters = emptyList(),
            url = "http://example.com/lawnmower-dog",
            created = "2021-01-01T00:00:00Z"
        )
    )
    val testEpisodesModel = EpisodesModelAPI(info = testEpisodeInfo, results = testEpisodeResult)

    @Test
    fun `Test loading characters`() = runBlocking {
        val testCharactersModel = CharactersModelAPI(
            info = CharactersModelAPI.Info(count = 2, pages = 1, next = "2", prev = null),
            results = testCharacterResult
        )

        val mockCall = mockk<Call<CharactersModelAPI>> {
            coEvery { execute() } returns Response.success(testCharactersModel)
        }

        val mockRepository = mockk<MainRepository> {
            coEvery { getCharactersByName("Rick") } returns mockCall
        }

        val viewModel = MainViewModel(mockRepository)
        viewModel.loadCharacters("Rick")

        delay(100)

        assertTrue(viewModel.CharacterList.value?.count()!! > 1)
    }

    @Test
    fun `Test setting search name`() {
        val viewModel = MainViewModel(mockk())

        viewModel.setSearchName("Rick")
        assertEquals("Rick", viewModel.SearchName.value)
    }


    @Test
    fun `Test character list change`() {
        val viewModel = MainViewModel(mockk())

        viewModel._characterList.value = testCharacterResult

        assertEquals(testCharacterResult, viewModel.CharacterList.value)
    }


    @Test
    fun `Test search name update`() {
        val viewModel = MainViewModel(mockk())

        viewModel._searchName.value = "Rick"

        assertEquals("Rick", viewModel.SearchName.value)
    }
}