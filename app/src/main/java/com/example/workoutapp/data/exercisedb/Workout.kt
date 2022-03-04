package com.example.workoutapp.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workoutapp.adapters.WorkoutBuildAdapter

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val WID: Int? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "exercises") val exercises: List<WorkoutBuildAdapter.ExerciseInstance>,
    @ColumnInfo(name = "notes") val notes: String? = null
)