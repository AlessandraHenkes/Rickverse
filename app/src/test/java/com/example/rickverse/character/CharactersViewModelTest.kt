package com.example.rickverse.character

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.rickverse.character.testData.CharactersData.CHARACTERS_RESPONSE_NEXT_PAGE
import com.example.rickverse.character.testData.CharactersData.CHARACTERS_RESPONSE_WITHOUT_NEXT_PAGE
import com.example.rickverse.model.CharacterPreview
import com.example.rickverse.model.CharactersResponse
import com.example.rickverse.service.CharactersService
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.mock.Calls
import kotlin.test.assertEquals

class CharactersViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observerShowMessageHasReachedEnd = Observer<Boolean> {}
    private val observerShowMessageLoading = Observer<Boolean> {}
    private val observerShowMessageError = Observer<Boolean> {}
    private val observerCharacters = Observer<List<CharacterPreview>> {}

    private val responseBody = mockk<ResponseBody>(relaxed = true)

    private val charactersService = mockk<CharactersService>(relaxed = true)

    private lateinit var charactersViewModel: CharactersViewModel

    @Before
    fun setUp() {
        charactersViewModel = CharactersViewModel(charactersService).apply {
            showMessageHasReachedEnd.observeForever(observerShowMessageHasReachedEnd)
            showMessageLoading.observeForever(observerShowMessageLoading)
            showMessageError.observeForever(observerShowMessageError)
            characters.observeForever(observerCharacters)
        }
    }

    @Test
    fun `verify if showMessageHasReachedEnd is false when response CHARACTERS_RESPONSE_NEXT_PAGE`() {
        // Arrange
        everySuccessfulData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(false, charactersViewModel.showMessageHasReachedEnd.value)
    }

    @Test
    fun `verify if showMessageHasReachedEnd is true when response CHARACTERS_RESPONSE_WITHOUT_NEXT_PAGE`() {
        // Arrange
        everySuccessfulData(result = CHARACTERS_RESPONSE_WITHOUT_NEXT_PAGE)

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(true, charactersViewModel.showMessageHasReachedEnd.value)
    }

    @Test
    fun `verify if showMessageLoading is true when response is loading`() {
        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(true, charactersViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response fail`() {
        // Arrange
        every { charactersService.getAll(any()) } returns Calls.failure(Throwable())

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(false, charactersViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is successful`() {
        // Arrange
        everySuccessfulData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(false, charactersViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is not successful`() {
        // Arrange
        every { charactersService.getAll(any()) } returns Calls.response(
            Response.error(
                404,
                responseBody
            )
        )

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(false, charactersViewModel.showMessageLoading.value)
    }

    private fun everySuccessfulData(result: CharactersResponse = CHARACTERS_RESPONSE_NEXT_PAGE) {
        every { charactersService.getAll(any()) } returns Calls.response(
            result
        )
    }

    @After
    fun tearDown() {
        charactersViewModel.run {
            showMessageHasReachedEnd.removeObserver(observerShowMessageHasReachedEnd)
            showMessageLoading.removeObserver(observerShowMessageLoading)
            showMessageError.removeObserver(observerShowMessageError)
            characters.removeObserver(observerCharacters)
        }
    }

}
