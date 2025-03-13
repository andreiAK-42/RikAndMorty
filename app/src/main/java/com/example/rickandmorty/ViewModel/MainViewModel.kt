package com.example.rickandmorty.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.openweathermap.Server.ApiServices
import com.example.rickandmorty.models.CharactersModelAPI
import com.example.rickandmorty.models.EpisodesModelAPI
import com.example.rickandmorty.repository.MainRepository
import com.example.rickandmorty.server.ApiClient
import retrofit2.Call
import retrofit2.Response

class MainViewModel(val repository: MainRepository) : ViewModel() {
    constructor() : this(
        MainRepository(
            (ApiClient().getClient().create((ApiServices::class.java)))
        )
    )

    private lateinit var episodes: EpisodesModelAPI

    private val _searchName = MutableLiveData<String>()
    val SearchName: LiveData<String> get() = _searchName

    private val _characterList = MutableLiveData<List<CharactersModelAPI.Result>>()
    val CharacterList: LiveData<List<CharactersModelAPI.Result>> get() = _characterList

    fun loadCharacters(name: String) {
        Log.d("ЗАГРУЗКА", "")
        Log.d("ИМЯ", name)
        repository.getCharactersByName(name)
            .enqueue(object : retrofit2.Callback<CharactersModelAPI> {
                override fun onResponse(
                    call: Call<CharactersModelAPI>,
                    response: Response<CharactersModelAPI>
                ) {
                    if (response.isSuccessful) {
                        Log.d("УСПЕШНО", "")
                        val data = response.body()
                        data?.let { it ->
                            _characterList.value = it.results
                        }
                    }
                }

                override fun onFailure(p0: Call<CharactersModelAPI>, p1: Throwable) {
                    Log.e("Ошибка загрузки", p1.message.toString())
                }
            })
    }

    fun loadEpisodes() {
        repository.getEpisodes().enqueue(object : retrofit2.Callback<EpisodesModelAPI> {
            override fun onResponse(
                call: Call<EpisodesModelAPI>,
                response: Response<EpisodesModelAPI>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        episodes = it
                    }
                }
            }

            override fun onFailure(p0: Call<EpisodesModelAPI>, p1: Throwable) {
                Log.e("Ошибка загрузки эпизодов", p1.message.toString())
            }
        })
    }

    fun setSearchName(name: String) {
        _searchName.value = name
    }

}