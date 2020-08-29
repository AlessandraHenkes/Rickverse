package com.example.rickverse.search.result

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.rickverse.character.testData.CharactersData
import com.example.rickverse.model.CharacterPreview
import com.example.rickverse.model.CharactersResponse
import com.example.rickverse.model.Status
import com.example.rickverse.service.CharactersService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.mock.Calls
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SearchResultsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observerShowMessageHasReachedEnd = Observer<Boolean> {}
    private val observerShowMessageLoading = Observer<Boolean> {}
    private val observerShowMessageError = Observer<Boolean> {}
    private val observerCharacters = Observer<List<CharacterPreview>> {}

    private val responseBody = mockk<ResponseBody>(relaxed = true)

    private val charactersService = mockk<CharactersService>(relaxed = true)

    private lateinit var searchResultsViewModel: SearchResultsViewModel

    @Before
    fun setUp() {
        searchResultsViewModel = SearchResultsViewModel(charactersService).apply {
            showMessageHasReachedEnd.observeForever(observerShowMessageHasReachedEnd)
            showMessageLoading.observeForever(observerShowMessageLoading)
            showMessageError.observeForever(observerShowMessageError)
            characters.observeForever(observerCharacters)
        }
    }

    @Test
    fun `verify if search call search at service`() {
        // Act
        searchResultsViewModel.search()

        // Assert
        verify(exactly = 1) { charactersService.search(any(), any(), any(), any()) }
        confirmVerified(charactersService)
    }

    @Test
    fun `verify if search call search at service with name filter`() {
        // Arrange
        val name = "Rick"
        searchResultsViewModel.loadFilters(name, null, null)

        // Act
        searchResultsViewModel.search()

        // Assert
        verify(exactly = 1) { charactersService.search(name, null, null, any()) }
        confirmVerified(charactersService)
    }

    @Test
    fun `verify if search call search at service with species filter`() {
        // Arrange
        val species = "human"
        searchResultsViewModel.loadFilters(null, species, null)

        // Act
        searchResultsViewModel.search()

        // Assert
        verify(exactly = 1) { charactersService.search(null, species, null, any()) }
        confirmVerified(charactersService)
    }

    @Test
    fun `verify if search call search at service with status filter`() {
        // Arrange
        val status = Status.ALIVE.value
        searchResultsViewModel.loadFilters(null, null, status)

        // Act
        searchResultsViewModel.search()

        // Assert
        verify(exactly = 1) { charactersService.search(null, null, status, any()) }
        confirmVerified(charactersService)
    }

    @Test
    fun `verify if showMessageHasReachedEnd is false when response CHARACTERS_RESPONSE_NEXT_PAGE`() {
        // Arrange
        everySuccessfulData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(false, searchResultsViewModel.showMessageHasReachedEnd.value)
    }

    @Test
    fun `verify if showMessageHasReachedEnd is true when response CHARACTERS_RESPONSE_WITHOUT_NEXT_PAGE`() {
        // Arrange
        everySuccessfulData(result = CharactersData.CHARACTERS_RESPONSE_WITHOUT_NEXT_PAGE)

        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(true, searchResultsViewModel.showMessageHasReachedEnd.value)
    }

    @Test
    fun `verify if showMessageLoading is true when response is loading`() {
        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(true, searchResultsViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response fail`() {
        // Arrange
        everyFailData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(false, searchResultsViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(false, searchResultsViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is successful`() {
        // Arrange
        everySuccessfulData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(false, searchResultsViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify if showMessageError is true when response fail`() {
        // Arrange
        everyFailData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(true, searchResultsViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageError is true when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(true, searchResultsViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageError is false when response is successful`() {
        // Arrange
        everySuccessfulData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertEquals(false, searchResultsViewModel.showMessageError.value)
    }

    @Test
    fun `verify if characters is null when response fail`() {
        // Arrange
        everyFailData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertNull(searchResultsViewModel.characters.value)
    }

    @Test
    fun `verify if characters is null when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertNull(searchResultsViewModel.characters.value)
    }

    @Test
    fun `verify if characters is not null when response is successful and check value`() {
        // Arrange
        everySuccessfulData()

        // Act
        searchResultsViewModel.search()

        // Assert
        assertNotNull(searchResultsViewModel.characters.value)
        assertEquals(
            CharactersData.CHARACTERS_RESPONSE_NEXT_PAGE.characters,
            searchResultsViewModel.characters.value
        )
    }

    private fun everyFailData() {
        every { charactersService.search(any(), any(), any(), any()) } returns Calls.failure(
            Throwable()
        )
    }

    private fun everyNotSuccessfulData() {
        every { charactersService.search(any(), any(), any(), any()) } returns Calls.response(
            Response.error(
                404,
                responseBody
            )
        )
    }

    private fun everySuccessfulData(
        result: CharactersResponse = CharactersData.CHARACTERS_RESPONSE_NEXT_PAGE
    ) {
        every { charactersService.search(any(), any(), any(), any()) } returns Calls.response(
            result
        )
    }

    @After
    fun tearDown() {
        searchResultsViewModel.run {
            showMessageHasReachedEnd.removeObserver(observerShowMessageHasReachedEnd)
            showMessageLoading.removeObserver(observerShowMessageLoading)
            showMessageError.removeObserver(observerShowMessageError)
            characters.removeObserver(observerCharacters)
        }
    }

}
