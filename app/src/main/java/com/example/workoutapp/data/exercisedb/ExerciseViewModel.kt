package com.example.workoutapp.data.exercisedb

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExerciseViewModel(private val exerciseRepository: ExerciseRepository) : ViewModel() {

    val allExercises : LiveData<MutableList<Exercise>> = exerciseRepository.allExercises.asLiveData()
    val allWorkouts : LiveData<MutableList<Workout>> = exerciseRepository.allWorkouts.asLiveData()
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
        println("WORKOUTS UPDATED!!!! TO : $workouts")
    }

    fun insertLog(log: Log) = viewModelScope.launch {
        exerciseRepository.insertLog(log)
    }

    fun delete(exercise: Exercise) = viewModelScope.launch {
        exerciseRepository.delete(exercise)
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