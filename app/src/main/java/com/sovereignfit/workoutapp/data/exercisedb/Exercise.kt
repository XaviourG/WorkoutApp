package com.sovereignfit.workoutapp.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true) var EID: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "unit") val unit: Int = 0, //0=KG, 1=LBS
)