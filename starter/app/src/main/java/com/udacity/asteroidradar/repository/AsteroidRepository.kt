package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.main.AsteroidApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {


    private var today = ""
    private var after7days = ""
    init {
        val dataFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val calendar = Calendar.getInstance()
        today = dataFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        after7days = dataFormat.format(calendar.time)
    }


    val status = MutableLiveData<AsteroidApiStatus>()

    /*
    * safe function to request Asteroid items from any thread including Main thread
     */
    suspend fun refreshAsteroid(){
        status.value = AsteroidApiStatus.LOADING
        withContext(Dispatchers.IO){
            val result = AsteroidApi.retrofitService.getAsteroids().await()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(result)).asDatabaseModel())
        }
        status.value = AsteroidApiStatus.DONE
    }

    suspend fun deleteAsteroidsFromPreviousDay(){
        val calendar = Calendar.getInstance()
        val dataFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = dataFormat.format(calendar.time)
        withContext(Dispatchers.IO){
            database.asteroidDao.deleteAsteroidsForDay(yesterday)
        }
    }

    val todayAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidsForDay(today)) {
        it.asDomainModel()
    }

    val weeklyAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidsForPeriod(today, after7days)) {
        it.asDomainModel()
    }

    val allSavedAsteroids = Transformations.map(database.asteroidDao.getAllAsteroids()){
        it.asDomainModel()
    }

}
