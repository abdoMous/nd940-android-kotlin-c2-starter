package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Query("select * from databaseasteroid")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AstroidDatabase: RoomDatabase() {
    abstract val astroidDao: AsteroidDao
}

private lateinit var INSTANCE: AstroidDatabase

fun getDatabase(context: Context): AstroidDatabase{
    synchronized(AstroidDatabase::class::java) {
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            AstroidDatabase::class.java,
            "asteroids").build()
        }
    }
    return INSTANCE
}