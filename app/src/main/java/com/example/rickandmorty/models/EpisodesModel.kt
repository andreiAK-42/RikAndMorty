package com.example.rickandmorty.models

data class EpisodesModelAPI(
    val info: Info,
    val episodes: List<Episode>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String,
        val prev: Any
    )

    data class Episode(
        val id: Int,
        val name: String,
        val air_date: String,
        val episode: String,
        val characters: List<String>,
        val url: String,
        val created: String
    )
}
