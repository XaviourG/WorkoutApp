package com.example.workoutapp.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Log(
    @PrimaryKey(autoGenerate = true) val LID: Int? = null,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "exerciseID") val exerciseID: Int,
    @ColumnInfo(name = "modifier") val modifier: String? = null,
    @ColumnInfo(name = "load") val load: Int,
    @ColumnInfo(name = "reps") val reps: Int
)