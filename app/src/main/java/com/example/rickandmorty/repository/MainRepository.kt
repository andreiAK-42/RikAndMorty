package com.example.rickandmorty.repository

import com.example.openweathermap.Server.ApiServices

class MainRepository(val api: ApiServices) {
    fun getCharactersByName(name: String) =
        api.getCharactersByName(name)

    fun getEpisodes() = api.getEpisodes()
}