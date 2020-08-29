package com.example.rickverse.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.rickverse.character.testData.CharactersData
import com.example.rickverse.model.CharacterPreview
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

class FavoritesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observerShowMessageLoading = Observer<Boolean> {}
    private val observerShowMessageError = Observer<Boolean> {}
    private val observerCharacters = Observer<List<CharacterPreview>> {}

    private val responseBody = mockk<ResponseBody>(relaxed = true)

    private val charactersService = mockk<CharactersService>(relaxed = true)

    private lateinit var favoritesViewModel: FavoritesViewModel

    @Before
    fun setUp() {
        favoritesViewModel = FavoritesViewModel(charactersService).apply {
            setFavorites(listOf(1, 2, 3))
        }
    }

    @Test
    fun `verify if loadFavorites call getFavorites at service`() {
        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        verify(exactly = 1) { charactersService.getFavorites(any()) }
        confirmVerified(charactersService)
    }

    @Test
    fun `verify if loadFavorites do not call getFavorites at service when does not have favorites added`() {
        // Arrange
        favoritesViewModel.setFavorites(listOf())

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        verify(inverse = true) { charactersService.getFavorites(any()) }
        confirmVerified(charactersService)
    }

    @Test
    fun `verify if showMessageLoading is true when response is loading`() {
        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(true, favoritesViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response fail`() {
        // Arrange
        everyFailData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(false, favoritesViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(false, favoritesViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is false when response is successful`() {
        // Arrange
        everySuccessfulData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(false, favoritesViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify is showMessageLoading is null when does not have favorites added`() {
        // Arrange
        favoritesViewModel.setFavorites(listOf())

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(null, favoritesViewModel.showMessageLoading.value)
    }

    @Test
    fun `verify if showMessageError is true when response fail`() {
        // Arrange
        everyFailData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(true, favoritesViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageError is true when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(true, favoritesViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageError is false when response is successful`() {
        // Arrange
        everySuccessfulData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(false, favoritesViewModel.showMessageError.value)
    }

    @Test
    fun `verify is showMessageError is null when does not have favorites added`() {
        // Arrange
        favoritesViewModel.setFavorites(listOf())

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(null, favoritesViewModel.showMessageError.value)
    }

    @Test
    fun `verify if showMessageNoFavorites is true when does not have favorites added`() {
        // Arrange
        favoritesViewModel.setFavorites(listOf())

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(true, favoritesViewModel.showMessageNoFavorites.value)
    }

    @Test
    fun `verify if showMessageNoFavorites is false when does not have favorites added`() {
        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertEquals(false, favoritesViewModel.showMessageNoFavorites.value)
    }

    @Test
    fun `verify if characters is null when response fail`() {
        // Arrange
        everyFailData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertNull(favoritesViewModel.characters.value)
    }

    @Test
    fun `verify if characters is null when response is not successful`() {
        // Arrange
        everyNotSuccessfulData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertNull(favoritesViewModel.characters.value)
    }

    @Test
    fun `verify if characters is not null when response is successful and check value`() {
        // Arrange
        everySuccessfulData()

        // Act
        favoritesViewModel.loadFavorites()

        // Assert
        assertNotNull(favoritesViewModel.characters.value)
        assertEquals(CharactersData.CHARACTERS, favoritesViewModel.characters.value)
    }

    @Test
    fun `verify if setFavorites set list of id to future request `() {
        // Arrange
        val favorites = listOf(1, 2, 3, 4, 5, 6)
        favoritesViewModel.setFavorites(favorites)

        // Act
        favoritesViewModel.loadFavorites()

        //Assert
        verify(exactly = 1) { charactersService.getFavorites(favorites) }
        confirmVerified(charactersService)
    }

    private fun everyFailData() {
        every { charactersService.getFavorites(any()) } returns Calls.failure(Throwable())
    }

    private fun everyNotSuccessfulData() {
        every { charactersService.getFavorites(any()) } returns Calls.response(
            Response.error(
                404,
                responseBody
            )
        )
    }

    private fun everySuccessfulData() {
        every { charactersService.getFavorites(any()) } returns Calls.response(
            CharactersData.CHARACTERS
        )
    }

    @After
    fun tearDown() {
        favoritesViewModel.apply {
            showMessageLoading.removeObserver(observerShowMessageLoading)
            showMessageError.removeObserver(observerShowMessageError)
            characters.removeObserver(observerCharacters)
        }
    }

}
