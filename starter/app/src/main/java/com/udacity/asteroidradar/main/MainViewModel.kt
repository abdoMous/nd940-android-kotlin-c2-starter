package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail : LiveData<Asteroid>
        get() = _navigateToDetail

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    init {
        _asteroids.value = listOf(Asteroid(2465633, "465633 (2009 JR5)",
            "2015-09-08", 20.3, 0.23,
            8.12,0.30, true),
            Asteroid(222222, "222 (2 JR5)",
                "2002-02-02", 20.3, 0.23,
                8.12,0.30, false),
            Asteroid(33333, "3333 (33 JR5)",
                "2003-03-03", 20.3, 0.23,
                8.12,0.30, true))
    }
}