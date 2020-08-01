package com.example.rickverse.character

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.rickverse.character.testData.CharactersData.CHARACTERS_RESPONSE_NEXT_PAGE
import com.example.rickverse.character.testData.CharactersData.CHARACTERS_RESPONSE_WITHOUT_NEXT_PAGE
import com.example.rickverse.model.CharacterPreview
import com.example.rickverse.model.CharactersResponse
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
    fun `verify if loadCharacters call getAll at service`() {
        // Act
        charactersViewModel.loadCharacters()

        // Assert
        verify(exactly = 1) { charactersService.getAll(any()) }
        confirmVerified(charactersService)
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
        everyFailData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(false, charactersViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

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
    fun `verify if showMessageError is true when response fail`() {
        // Arrange
        everyFailData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(true, charactersViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageError is true when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(true, charactersViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageError is false when response is successful`() {
        // Arrange
        everySuccessfulData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertEquals(false, charactersViewModel.showMessageError.value)
    }

    @Test
    fun `verify if characters is null when response fail`() {
        // Arrange
        everyFailData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertNull(charactersViewModel.characters.value)
    }

    @Test
    fun `verify if characters is null when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertNull(charactersViewModel.characters.value)
    }

    @Test
    fun `verify if characters is not null when response is successful and check value`() {
        // Arrange
        everySuccessfulData()

        // Act
        charactersViewModel.loadCharacters()

        // Assert
        assertNotNull(charactersViewModel.characters.value)
        assertEquals(CHARACTERS_RESPONSE_NEXT_PAGE.characters, charactersViewModel.characters.value)
    }

    private fun everyFailData() {
        every { charactersService.getAll(any()) } returns Calls.failure(Throwable())
    }

    private fun everyNotSuccessfulData() {
        every { charactersService.getAll(any()) } returns Calls.response(
            Response.error(
                404,
                responseBody
            )
        )
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
