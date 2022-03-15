package com.example.workoutapp.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true) var EID: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "primeMover") val primeMover: String? = null,
    @ColumnInfo(name = "exType") val exType: String? = null
)