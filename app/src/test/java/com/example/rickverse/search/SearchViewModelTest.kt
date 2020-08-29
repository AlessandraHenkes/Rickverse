package com.example.rickverse.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observerShowErrorName = Observer<Boolean> {}
    private val observerShowErrorSpecies = Observer<Boolean> {}
    private val observerIsButtonEnabled = Observer<Boolean> {}

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setUp() {
        searchViewModel = SearchViewModel().apply {
            showErrorName.observeForever(observerShowErrorName)
            showErrorSpecies.observeForever(observerShowErrorSpecies)
            isButtonEnabled.observeForever(observerIsButtonEnabled)
        }
    }

    @Test
    fun `verify if showErrorName is false when name is empty and lostFocus is false`() {
        // Arrange
        searchViewModel.verifyName("")

        // Assert
        assertEquals(false, searchViewModel.showErrorName.value)
    }

    @Test
    fun `verify if showErrorName is false when name is empty and lostFocus is true`() {
        // Arrange
        searchViewModel.verifyName(name = "", lostFocus = true)

        // Assert
        assertEquals(false, searchViewModel.showErrorName.value)
    }

    @Test
    fun `verify if showErrorName is false when name has less then 3 characters and lostFocus is false`() {
        // Arrange
        searchViewModel.verifyName("ab")

        // Assert
        assertEquals(false, searchViewModel.showErrorName.value)
    }

    @Test
    fun `verify if showErrorName is true when name has less then 3 characters and lostFocus is true`() {
        // Arrange
        searchViewModel.verifyName(name = "ab", lostFocus = true)

        // Assert
        assertEquals(true, searchViewModel.showErrorName.value)
    }

    @Test
    fun `verify if showErrorName is false when name has 3 characters and lostFocus is false`() {
        // Arrange
        searchViewModel.verifyName("abc")

        // Assert
        assertEquals(false, searchViewModel.showErrorName.value)
    }

    @Test
    fun `verify if showErrorName is false when name has 3 characters and lostFocus is true`() {
        // Arrange
        searchViewModel.verifyName(name = "abc", lostFocus = true)

        // Assert
        assertEquals(false, searchViewModel.showErrorName.value)
    }

    @Test
    fun `verify if showErrorName is false when name has more then 3 characters and lostFocus is false`() {
        // Arrange
        searchViewModel.verifyName("abcd")

        // Assert
        assertEquals(false, searchViewModel.showErrorName.value)
    }

    @Test
    fun `verify if showErrorName is false when name has more then 3 characters and lostFocus is true`() {
        // Arrange
        searchViewModel.verifyName(name = "abcd", lostFocus = true)

        // Assert
        assertEquals(false, searchViewModel.showErrorName.value)
    }

    @Test
    fun `verify if showErrorSpecies is false when name is empty and lostFocus is false`() {
        // Arrange
        searchViewModel.verifySpecies("")

        // Assert
        assertEquals(false, searchViewModel.showErrorSpecies.value)
    }

    @Test
    fun `verify if showErrorSpecies is false when name is empty and lostFocus is true`() {
        // Arrange
        searchViewModel.verifySpecies(species = "", lostFocus = true)

        // Assert
        assertEquals(false, searchViewModel.showErrorSpecies.value)
    }

    @Test
    fun `verify if showErrorSpecies is false when name has less then 3 characters and lostFocus is false`() {
        // Arrange
        searchViewModel.verifySpecies("ab")

        // Assert
        assertEquals(false, searchViewModel.showErrorSpecies.value)
    }

    @Test
    fun `verify if showErrorSpecies is true when name has less then 3 characters and lostFocus is true`() {
        // Arrange
        searchViewModel.verifySpecies(species = "ab", lostFocus = true)

        // Assert
        assertEquals(true, searchViewModel.showErrorSpecies.value)
    }

    @Test
    fun `verify if showErrorSpecies is false when name has 3 characters and lostFocus is false`() {
        // Arrange
        searchViewModel.verifySpecies("abc")

        // Assert
        assertEquals(false, searchViewModel.showErrorSpecies.value)
    }

    @Test
    fun `verify if showErrorSpecies is false when name has 3 characters and lostFocus is true`() {
        // Arrange
        searchViewModel.verifySpecies(species = "abc", lostFocus = true)

        // Assert
        assertEquals(false, searchViewModel.showErrorSpecies.value)
    }

    @Test
    fun `verify if showErrorSpecies is false when name has more then 3 characters and lostFocus is false`() {
        // Arrange
        searchViewModel.verifySpecies("abcd")

        // Assert
        assertEquals(false, searchViewModel.showErrorSpecies.value)
    }

    @Test
    fun `verify if showErrorSpecies is false when name has more then 3 characters and lostFocus is true`() {
        // Arrange
        searchViewModel.verifySpecies(species = "abcd", lostFocus = true)

        // Assert
        assertEquals(false, searchViewModel.showErrorSpecies.value)
    }

    @Test
    fun `verify if isButtonEnabled starts false`() {
        assertEquals(false, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is false when name is invalid`() {
        // Arrange
        searchViewModel.verifyName("")

        // Assert
        assertEquals(false, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is false when species is invalid`() {
        // Arrange
        searchViewModel.verifySpecies("")

        // Assert
        assertEquals(false, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is false when status is invalid`() {
        // Arrange
        searchViewModel.verifyStatus(-1)

        // Assert
        assertEquals(false, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is true when only name is valid`() {
        // Arrange
        searchViewModel.verifyName("abc")
        searchViewModel.verifySpecies("")
        searchViewModel.verifyStatus(-1)

        // Assert
        assertEquals(true, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is true when only species is valid`() {
        // Arrange
        searchViewModel.verifyName("")
        searchViewModel.verifySpecies("abc")
        searchViewModel.verifyStatus(-1)

        // Assert
        assertEquals(true, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is true when only status is valid`() {
        // Arrange
        searchViewModel.verifyName("")
        searchViewModel.verifySpecies("")
        searchViewModel.verifyStatus(1)

        // Assert
        assertEquals(true, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is true when name and species is valid`() {
        // Arrange
        searchViewModel.verifyName("abc")
        searchViewModel.verifySpecies("abc")
        searchViewModel.verifyStatus(-1)

        // Assert
        assertEquals(true, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is true when name and status is valid`() {
        // Arrange
        searchViewModel.verifyName("abc")
        searchViewModel.verifySpecies("")
        searchViewModel.verifyStatus(-1)

        // Assert
        assertEquals(true, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is true when species and status is valid`() {
        // Arrange
        searchViewModel.verifyName("")
        searchViewModel.verifySpecies("abc")
        searchViewModel.verifyStatus(1)

        // Assert
        assertEquals(true, searchViewModel.isButtonEnabled.value)
    }

    @Test
    fun `verify if isButtonEnabled is true when name, species and status is valid`() {
        // Arrange
        searchViewModel.verifyName("abc")
        searchViewModel.verifySpecies("abc")
        searchViewModel.verifyStatus(1)

        // Assert
        assertEquals(true, searchViewModel.isButtonEnabled.value)
    }

    @After
    fun tearDown() {
        searchViewModel.apply {
            showErrorName.removeObserver(observerShowErrorName)
            showErrorSpecies.removeObserver(observerShowErrorSpecies)
            isButtonEnabled.removeObserver(observerIsButtonEnabled)
        }
    }

}
