package com.example.openweathermap.Server

import com.example.rickandmorty.models.CharactersModelAPI
import com.example.rickandmorty.models.EpisodesModelAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("api/character/")
    fun getCharactersByName (
        @Query("name") appId: String,
    ): Call<CharactersModelAPI>

    @GET("api/episode")
    fun getEpisodes (): Call<EpisodesModelAPI>
}