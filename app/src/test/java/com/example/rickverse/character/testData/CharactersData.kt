package com.example.rickverse.character.testData

import com.example.rickverse.model.*

object CharactersData {

    private val INFO_WITH_NEXT_PAGE = Info(
        count = 100,
        pages = 5,
        next = "nextPage",
        prev = null
    )

    private val INFO_WITHOUT_NEXT_PAGE = Info(
        count = 1,
        pages = 1,
        next = null,
        prev = null
    )

    val CHARACTERS = listOf<CharacterPreview>()

    val CHARACTERS_RESPONSE_NEXT_PAGE = CharactersResponse(
        info = INFO_WITH_NEXT_PAGE,
        characters = CHARACTERS
    )

    val CHARACTERS_RESPONSE_WITHOUT_NEXT_PAGE = CharactersResponse(
        info = INFO_WITHOUT_NEXT_PAGE,
        characters = CHARACTERS
    )

    val CHARACTER = CharacterResponse(
        id = 1,
        name = "Rick Sanchez",
        status = Status.ALIVE,
        species = "Human",
        gender = Gender.MALE,
        origin = Origin(name = "Earth (C-137)", url = "url"),
        image = "imageUrl"
    )

}
