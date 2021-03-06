package com.sovereignfit.workoutapp.data.exercisedb

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val allExercises:Flow<MutableList<Exercise>> = exerciseDao.getAllExercises()
    val allWorkouts:Flow<MutableList<Workout>> = exerciseDao.getAllWorkouts()
    val allLogs:Flow<MutableList<Log>> = exerciseDao.getAllLogs()
    val allPrograms:Flow<MutableList<Program>> = exerciseDao.getAllPrograms()
    val allSessions:Flow<MutableList<Session>> = exerciseDao.getAllSessions()
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

    @WorkerThread
    suspend fun getLastLog(eid: Int): Log {
        return exerciseDao.findLastLogByExerciseID(eid)
    }

    //PROGRAMS
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertProgram(program: Program){
        exerciseDao.insertProgram(program)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteProgram(program: Program){
        exerciseDao.deleteProgram(program)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateProgram(program: Program){
        exerciseDao.updateProgram(program)
    }

    //SESSIONS
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSession(session: Session){
        exerciseDao.insertSession(session)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteSession(session: Session){
        exerciseDao.deleteSession(session)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateSession(session: Session){
        exerciseDao.updateSession(session)
    }

}