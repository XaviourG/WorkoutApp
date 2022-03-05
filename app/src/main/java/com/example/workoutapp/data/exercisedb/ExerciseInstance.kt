package com.example.workoutapp.data.exercisedb

data class ExerciseInstance(
    val exercise: Exercise,
    var sets: Array<Int> = arrayOf(1)
)