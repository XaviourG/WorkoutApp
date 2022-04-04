package com.example.workoutapp.data.exercisedb

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseViewModel(private val exerciseRepository: ExerciseRepository) : ViewModel() {

    val allExercises : LiveData<MutableList<Exercise>> = exerciseRepository.allExercises.asLiveData()
    val allWorkouts : LiveData<MutableList<Workout>> = exerciseRepository.allWorkouts.asLiveData()
    val allLogs : LiveData<MutableList<Log>> = exerciseRepository.allLogs.asLiveData()
    val allPrograms: LiveData<MutableList<Program>> = exerciseRepository.allPrograms.asLiveData()
    var workouts: MutableList<Workout> = mutableListOf<Workout>()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(exercise: Exercise) = viewModelScope.launch {
        exerciseRepository.insert(exercise)
    }

    fun insertWorkout(workout: Workout) = viewModelScope.launch {
        exerciseRepository.insertWorkout(workout)
    }

    fun updateWorkouts() = viewModelScope.launch {
        workouts = exerciseRepository.updateWorkouts()
        //println("WORKOUTS UPDATED!!!! TO : $workouts")
    }

    fun insertLog(log: Log) = viewModelScope.launch {
        exerciseRepository.insertLog(log)
    }

    fun delete(exercise: Exercise) = viewModelScope.launch {
        exerciseRepository.delete(exercise)
    }

    fun deleteWorkout(workout: Workout) = viewModelScope.launch {
        exerciseRepository.deleteWorkout(workout)
    }

    fun updateWorkout(workout: Workout) = viewModelScope.launch {
        exerciseRepository.updateWorkout(workout)
    }

    /*fun getLastLog(eid: Int) = viewModelScope.launch {
        suspend fun getLastLogg(eid: Int): Log = withContext(Dispatchers.IO) {
            exerciseRepository.getLastLog(eid)
        }
    } */

    /*fun getWorkoutByID(WID: Int): Workout {
        exerciseRepository.getWorkoutByID(WID)
    }*/

    //PROGRAM
    fun insertProgram(program: Program) = viewModelScope.launch {
        exerciseRepository.insertProgram(program)
    }
    fun deleteProgram(program: Program) = viewModelScope.launch {
        exerciseRepository.deleteProgram(program)
    }
    fun updateProgram(program: Program) = viewModelScope.launch {
        exerciseRepository.updateProgram(program)
    }




    class ExerciseViewModelFactory(private val exerciseRepository: ExerciseRepository)
        :ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ExerciseViewModel(exerciseRepository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }

    }
}