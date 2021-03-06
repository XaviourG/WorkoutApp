package com.sovereignfit.workoutapp.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val WID: Int? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "exercises") val exercises: List<ExerciseInstance>,
    @ColumnInfo(name = "supersets") val supersets: List<String>,
    @ColumnInfo(name = "notes") val notes: List<String>?
)