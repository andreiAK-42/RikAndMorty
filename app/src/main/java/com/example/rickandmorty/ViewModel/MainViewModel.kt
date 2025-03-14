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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        repository.getCharactersByName(name)
            .enqueue(object : retrofit2.Callback<CharactersModelAPI> {
                override fun onResponse(
                    call: Call<CharactersModelAPI>,
                    response: Response<CharactersModelAPI>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {

                            it.results?.forEach { n ->
                                val episodeUrl = n.episode[0]
                                if (episodeUrl != null) {
                                    val episodeId = episodeUrl.split("/").last().toIntOrNull()
                                    if (episodeId != null) {
                                        val episodeName =
                                            episodes.results?.find { it.id == episodeId }?.name
                                        if (episodeName != null) {
                                            n.episode[0] = episodeName
                                        } else {
                                            Log.e(
                                                "MainViewModel",
                                                "Episode not found for $episodeUrl" + "|" + episodes.results
                                            )
                                        }
                                    } else {
                                        Log.e(
                                            "MainViewModel",
                                            "Invalid episode ID in URL: $episodeUrl"
                                        )
                                    }
                                } else {
                                    Log.e("MainViewModel", "Episode URL is null")
                                }
                            }

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
        CoroutineScope(Dispatchers.Main).launch {
            for (n in 1..3) {
                getEpisodesFor(n)
                delay(200)
            }
        }
    }

    private fun getEpisodesFor(page: Int) {
        repository.getEpisodes(page).enqueue(object : retrofit2.Callback<EpisodesModelAPI> {
            override fun onResponse(
                call: Call<EpisodesModelAPI>,
                response: Response<EpisodesModelAPI>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.results != null) {
                        if (page > 1 && ::episodes.isInitialized) {
                            val newResults = mutableListOf<EpisodesModelAPI.Episode>()
                            newResults.addAll(episodes.results)
                            newResults.addAll(data.results)
                            episodes.results = newResults
                        } else {
                            episodes = data
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EpisodesModelAPI>, t: Throwable) {
                Log.e("EpisodesLoad", "Failed to load episodes: ${t.message}")
            }
        })
    }

    fun setSearchName(name: String) {
        _searchName.value = name
    }
}