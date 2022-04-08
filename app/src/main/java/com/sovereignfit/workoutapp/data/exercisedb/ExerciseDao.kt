package com.sovereignfit.workoutapp.data.exercisedb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    //Methods for Exercise Entities
    @Query("SELECT * FROM Exercise ORDER BY name")
    fun getAllExercises(): Flow<MutableList<Exercise>>

    @Query("SELECT * FROM Exercise WHERE EID IN (:exIds)")
    fun loadAllExercisesByIds(exIds: IntArray): Flow<MutableList<Exercise>>

    @Query("SELECT * FROM Exercise WHERE name LIKE :name LIMIT 1")
    fun findExerciseByName(name: String): Exercise

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise)

    @Delete
    fun deleteExercise(exercise: Exercise)


    //Methods for Workout Entities
    @Query("SELECT * FROM Workout ORDER BY title")
    fun getAllWorkouts(): Flow<MutableList<Workout>>

    @Query("SELECT * FROM Workout ORDER BY title")
    suspend fun updateWorkouts(): MutableList<Workout>

    @Query("SELECT * FROM Workout WHERE title LIKE :title LIMIT 1")
    fun findWorkoutByTitle(title: String): Workout

    @Query("SELECT * FROM Workout WHERE WID LIKE :WID LIMIT 1")
    fun getWorkoutByID(WID: Int): Workout

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)
    @Update
    suspend fun updateWorkout(workout: Workout)


    //Methods for Log Entities
    //@Query("SELECT * FROM Log ORDER BY date")
    //fun getAllLogs(): Flow<MutableList<Exercise>>

    @Query("SELECT * FROM Log WHERE exerciseID LIKE :exerciseID LIMIT 1")
    fun findLastLogByExerciseID(exerciseID: Int): Log

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLog(log: Log)

    @Delete
    fun deleteLog(log: Log)

    @Query("SELECT * FROM Log ORDER BY date DESC")
    fun getAllLogs(): Flow<MutableList<Log>>


    //PROGRAMS
    @Query("SELECT * FROM Program ORDER BY PID DESC")
    fun getAllPrograms(): Flow<MutableList<Program>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProgram(program: Program)

    @Delete
    suspend fun deleteProgram(program: Program)

    @Update
    suspend fun updateProgram(program: Program)
}
