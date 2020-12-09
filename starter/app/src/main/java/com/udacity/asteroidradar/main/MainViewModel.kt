package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class AsteroidApiStatus { LOADING, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail : LiveData<Asteroid>
        get() = _navigateToDetail


    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status : LiveData<AsteroidApiStatus>
        get() = _status

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            asteroidRepository.refreshAsteroid()
        }
    }

    val asteroids = asteroidRepository.astroids

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
