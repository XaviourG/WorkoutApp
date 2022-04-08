package com.sovereignfit.workoutapp.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Log(
    @PrimaryKey(autoGenerate = true) val LID: Int? = null,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "exerciseID") val exerciseID: Int,
    @ColumnInfo(name = "performance") val performance: String
    //Where performance is stored load.reps.modifier|load.reps.modifier|...
)