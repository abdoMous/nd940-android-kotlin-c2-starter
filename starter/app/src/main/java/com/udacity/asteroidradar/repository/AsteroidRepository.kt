package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AstroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class AsteroidRepository(private val database: AstroidDatabase) {


    /*
    * List of astroid that can be shown
     */
    val astroids: LiveData<List<Asteroid>> = Transformations.map(database.astroidDao.getAsteroids()){
        it.asDomainModel()
    }

    /*
    * safe function to request Astroid items from any thread including Main thread
     */
    suspend fun refreshAsteroid(){
        withContext(Dispatchers.IO){
            val result = AsteroidApi.retrofitService.getAsteroids().await()
            database.astroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(result)).asDatabaseModel())
        }
    }
}
