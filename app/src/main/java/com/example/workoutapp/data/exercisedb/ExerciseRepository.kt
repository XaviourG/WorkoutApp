package com.example.workoutapp.data.exercisedb

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val allExercises:Flow<MutableList<Exercise>> = exerciseDao.getAllExercises()
    val allWorkouts:Flow<MutableList<Workout>> = exerciseDao.getAllWorkouts()
    lateinit var workoutList: MutableList<Workout>

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(exercise: Exercise){
        exerciseDao.insertExercise(exercise)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(exercise: Exercise){
        exerciseDao.deleteExercise(exercise)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertWorkout(workout: Workout){
        exerciseDao.insertWorkout(workout)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateWorkouts(): MutableList<Workout> {
        workoutList = exerciseDao.updateWorkouts()
        return workoutList
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteWorkout(workout: Workout){
        exerciseDao.deleteWorkout(workout)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertLog(log: Log){
        exerciseDao.insertLog(log)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getWorkoutByID(WID: Int): Workout{
        return exerciseDao.getWorkoutByID(WID)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateWorkout(workout: Workout) {
        exerciseDao.updateWorkout(workout)
    }

}