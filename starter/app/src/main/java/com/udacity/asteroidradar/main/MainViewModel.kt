package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.getTodayFormattedDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.await
import java.lang.Exception

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail : LiveData<Asteroid>
        get() = _navigateToDetail


    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status : LiveData<AsteroidApiStatus>
        get() = _status

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    fun getAsteroid(filter: AsteroidApiFilter){
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                val result : String
                if(filter == AsteroidApiFilter.SHOW_TODAY_ASTEROIDS){
                    result = AsteroidApi.retrofitService.getAsteroids(getTodayFormattedDate(), getTodayFormattedDate()).await()
                } else {
                    result = AsteroidApi.retrofitService.getAsteroids().await()
                }
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(result))
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception){
                _status.value = AsteroidApiStatus.ERROR
                _asteroids.value = ArrayList()
            }
        }

    }

    fun updateFilter(filter: AsteroidApiFilter) {
        getAsteroid(filter)
    }

}