package com.example.workoutapp.data.exercisedb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM Exercise ORDER BY name")
    fun getAll(): Flow<MutableList<Exercise>>

    @Query("SELECT * FROM Exercise WHERE EID IN (:exIds)")
    fun loadAllByIds(exIds: IntArray): Flow<MutableList<Exercise>>

    @Query("SELECT * FROM Exercise WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Exercise

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg exercises: Exercise)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)
}