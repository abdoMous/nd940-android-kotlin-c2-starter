package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.PictureOfDayApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import retrofit2.await

enum class AsteroidApiStatus { LOADING, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail : LiveData<Asteroid>
        get() = _navigateToDetail


    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val pictureOfDayApi = PictureOfDayApi.retrofitService

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroid()

            val result = pictureOfDayApi.getPictureOfDay().await()
            _pictureOfDay.value = result
        }
    }

    val asteroids = asteroidRepository.asteroids
    val status = asteroidRepository.status

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }

//    fun updateFilter(filter: AsteroidApiFilter) {
//        getAsteroid(filter)
//    }

    class Factory(private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
