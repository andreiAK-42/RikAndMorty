package com.example.openweathermap.Server

import com.example.rickandmorty.models.CharactersModelAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("api/character/")
    fun getCharactersByName (
        @Query("name") appId: String,
    ): Call<CharactersModelAPI>

    @GET("data/2.5/forecast")
    fun getForecastWeather(
        @Query("appid") appId: String,
        @Query("q") city: String
    ): Call<ForecastResponseAPI>

    @GET("geo/1.0/direct")
    fun getCity(
        @Query("appid") appId: String,
        @Query("q") city: String,
        @Query("limit") limit: Int = 5
    ): Call<CityList>
}