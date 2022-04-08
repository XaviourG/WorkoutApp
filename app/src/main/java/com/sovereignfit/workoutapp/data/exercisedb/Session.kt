package com.sovereignfit.workoutapp.data.exercisedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    //There should only ever be one session, all session ID's are set to 1!
    @PrimaryKey val SID: Int,
    @ColumnInfo(name = "WID") val WID: Int,
    @ColumnInfo(name = "sets") val sets: String,
    @ColumnInfo(name = "startTime") val startTime: Int
)