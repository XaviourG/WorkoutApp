package com.example.workoutapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "log_table")
data class Log (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val exercise: String,
    val load: Int,
    val reps: Int,
    val date: String
)