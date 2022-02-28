package com.example.workoutapp.data

import androidx.lifecycle.LiveData

class LogRepository(private val logDao: LogDao) {

    val readAllData: LiveData<List<Log>> = logDao.readAllData()

    suspend fun addLog(log: Log) {
        logDao.addLog(log)
    }
}