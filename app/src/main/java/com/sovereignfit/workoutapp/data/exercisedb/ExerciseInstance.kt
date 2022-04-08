package com.sovereignfit.workoutapp.data.exercisedb

data class ExerciseInstance(
    val exercise: Exercise,
    var sets: Array<String> = arrayOf("::none")
)