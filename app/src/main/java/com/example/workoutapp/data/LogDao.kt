package com.example.workoutapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    fun addLog(log: Log)

    @Delete
    fun delete(log: Log)

    @Query("SELECT * FROM log_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Log>>
}