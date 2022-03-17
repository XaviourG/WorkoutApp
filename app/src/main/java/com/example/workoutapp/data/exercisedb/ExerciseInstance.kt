package com.example.workoutapp.data.exercisedb

data class ExerciseInstance(
    val exercise: Exercise,
    var sets: Array<String> = arrayOf("0:0:none")
)