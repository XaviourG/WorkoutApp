package com.example.workoutapp.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Program(
    @PrimaryKey(autoGenerate = true) val PID: Int? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "workouts") val workoutIDs: List<Int>,
    @ColumnInfo(name = "position") val position: Int = 0,
    @ColumnInfo(name = "active") val active: Boolean = false
)