package com.example.rickverse.character

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.rickverse.character.testData.CharactersData.CHARACTER
import com.example.rickverse.model.CharacterResponse
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

class CharacterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observerShowMessageLoading = Observer<Boolean> {}
    private val observerShowMessageError = Observer<Boolean> {}
    private val observerCharacterInfo = Observer<CharacterResponse> {}

    private val responseBody = mockk<ResponseBody>(relaxed = true)

    private val charactersService = mockk<CharactersService>(relaxed = true)

    private lateinit var characterViewModel: CharacterViewModel

    @Before
    fun setUp() {
        characterViewModel = CharacterViewModel(charactersService).apply {
            showMessageLoading.observeForever(observerShowMessageLoading)
            showMessageError.observeForever(observerShowMessageError)
            characterInfo.observeForever(observerCharacterInfo)
        }
    }

    @Test
    fun `verify if loadCharacter call getCharacterInfo at service`() {
        // Act
        characterViewModel.loadCharacter()

        // Assert
        verify(exactly = 1) { charactersService.getCharacterInfo(any()) }
        confirmVerified(charactersService)
    }

    @Test
    fun `verify if showMessageLoading is true when response is loading`() {
        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertEquals(true, characterViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response fail`() {
        // Arrange
        everyFailData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertEquals(false, characterViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertEquals(false, characterViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is successful`() {
        // Arrange
        everySuccessfulData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertEquals(false, characterViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify if showMessageError is true when response fail`() {
        // Arrange
        everyFailData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertEquals(true, characterViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageError is true when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertEquals(true, characterViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageError is false when response is successful`() {
        // Arrange
        everySuccessfulData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertEquals(false, characterViewModel.showMessageError.value)
    }

    @Test
    fun `verify if characterInfo is null when response fail`() {
        // Arrange
        everyFailData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertNull(characterViewModel.characterInfo.value)
    }

    @Test
    fun `verify if characterInfo is null when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertNull(characterViewModel.characterInfo.value)
    }

    @Test
    fun `verify if characterInfo is not null when response is successful and check value`() {
        // Arrange
        everySuccessfulData()

        // Act
        characterViewModel.loadCharacter()

        // Assert
        assertNotNull(characterViewModel.characterInfo.value)
        assertEquals(CHARACTER, characterViewModel.characterInfo.value)
    }

    @Test
    fun `verify if shouldAddToFavorites is true when character isFavorite is false and toggleFavorite`() {
        characterViewModel.run {
            // Arrange
            setIsFavoriteCharacter(false)

            // Act
            toggleFavorite()

            // Assert
            assertEquals(true, shouldAddToFavorites.value)
        }
    }

    @Test
    fun `verify if shouldAddToFavorites is null when character isFavorite and toggleFavorite`() {
        characterViewModel.run {
            // Arrange
            setIsFavoriteCharacter(true)

            // Act
            toggleFavorite()

            // Assert
            assertNull(shouldAddToFavorites.value)
        }
    }

    @Test
    fun `verify if shouldRemoveFromFavorites is null when character isFavorite is false and toggleFavorite`() {
        characterViewModel.run {
            // Arrange
            setIsFavoriteCharacter(false)

            // Act
            toggleFavorite()

            // Assert
            assertNull(shouldRemoveFromFavorites.value)
        }
    }

    @Test
    fun `verify if shouldRemoveFromFavorites is true when character isFavorite and toggleFavorite`() {
        characterViewModel.run {
            // Arrange
            setIsFavoriteCharacter(true)

            // Act
            toggleFavorite()

            // Assert
            assertEquals(true, shouldRemoveFromFavorites.value)
        }
    }

    private fun everyFailData() {
        every { charactersService.getCharacterInfo(any()) } returns Calls.failure(Throwable())
    }

    private fun everyNotSuccessfulData() {
        every { charactersService.getCharacterInfo(any()) } returns Calls.response(
            Response.error(
                404,
                responseBody
            )
        )
    }

    private fun everySuccessfulData() {
        every { charactersService.getCharacterInfo(any()) } returns Calls.response(
            CHARACTER
        )
    }

    @After
    fun tearDown() {
        characterViewModel.apply {
            showMessageLoading.removeObserver(observerShowMessageLoading)
            showMessageError.removeObserver(observerShowMessageError)
            characterInfo.removeObserver(observerCharacterInfo)
        }
    }
}