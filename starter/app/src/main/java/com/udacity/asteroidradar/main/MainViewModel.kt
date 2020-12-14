package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApiFilter
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

    val status = asteroidRepository.status

    private val pictureOfDayApi = PictureOfDayApi.retrofitService

    private val asteroidFilter = MutableLiveData(AsteroidApiFilter.SHOW_WEEK_ASTEROIDS)

    val asteroids = Transformations.switchMap(asteroidFilter){
        when(it!!){
            AsteroidApiFilter.SHOW_TODAY_ASTEROIDS -> asteroidRepository.todayAsteroids
            AsteroidApiFilter.SHOW_WEEK_ASTEROIDS -> asteroidRepository.weeklyAsteroids
            AsteroidApiFilter.SHOW_SAVED_ASTEROIDS -> asteroidRepository.allSavedAsteroids
        }
    }

    init {
        viewModelScope.launch {
            try{
                asteroidRepository.refreshAsteroid()
                setupPictureOfDay()

            } catch (e: Exception){
                Log.e("MainViewModel", e.message!!)
                status.value = AsteroidApiStatus.DONE
            }
        }
    }

    private suspend fun setupPictureOfDay() {
        val result = pictureOfDayApi.getPictureOfDay().await()
        if(result.mediaType == "image") {
            _pictureOfDay.value = result
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }

    fun updateFilter(filter: AsteroidApiFilter) {
        asteroidFilter.value = filter
    }

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
