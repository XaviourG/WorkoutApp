package com.example.workoutapp.data.exercisedb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Exercise::class], version=1, exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    companion object {

        @Volatile
        private var INSTANCE : ExerciseDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope):ExerciseDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    "exercise_database"
                ).addCallback(ExerciseCallback(scope))
                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

    private class ExerciseCallback(val scope: CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { exerciseDatabase ->
                scope.launch {
                    // if you want to populate database
                    // when RoomDatabase is created
                    // populate here
                    exerciseDatabase.exerciseDao().insert(Exercise(name = "Bench Press"))
                    exerciseDatabase.exerciseDao().insert(Exercise(name = "Backsquat"))
                    exerciseDatabase.exerciseDao().insert(Exercise(name = "Deadlift"))
                }
            }
        }
    }
}