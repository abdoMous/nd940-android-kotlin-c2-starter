package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Query("select * from databaseasteroid order by closeApproachDate")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate = :date order by closeApproachDate")
    fun getAsteroidsForDay(date: String): LiveData<List<DatabaseAsteroid>>
    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate BETWEEN :startDate AND :endDate order by closeApproachDate")
    fun getAsteroidsForPeriod(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)

    @Query("delete from databaseasteroid where closeApproachDate = :day")
    fun deleteAsteroidsForDay(day: String)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase{
    synchronized(AsteroidDatabase::class::java) {
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            AsteroidDatabase::class.java,
            "asteroids").build()
        }
    }
    return INSTANCE
}