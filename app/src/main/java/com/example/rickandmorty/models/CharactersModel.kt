package com.example.rickandmorty.models

data class CharactersModelAPI(
    val info: Info,
    val results: List<Result>
) {
    data class Info(
        val count: Long,
        val pages: Long,
        val next: String,
        val prev: Any?,
    )

    data class Result(
        val id: Long,
        val name: String,
        val status: String,
        val species: String,
        val type: String,
        val gender: String,
        val origin: Origin,
        val location: Location,
        val image: String,
        val episode: MutableList<String>,
        val url: String,
        val created: String,
    )

    data class Origin(
        val name: String,
        val url: String,
    )

    data class Location(
        val name: String,
        val url: String,
    )
}