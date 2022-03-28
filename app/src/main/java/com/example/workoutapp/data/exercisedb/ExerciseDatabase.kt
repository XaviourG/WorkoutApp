package com.example.workoutapp.data.exercisedb

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//add this to migrate autoMigrations = [AutoMigration(from = 1, to = 2)]
//just recreating the emulator also fixes this issue
@Database(entities = [Exercise::class, Workout::class, Log::class, Program::class], version=1)
@TypeConverters(Converters::class)
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
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Bench Press"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Back Squat (HighBar)"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Back Squat (LowBar)"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Front Squat"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Deadlift"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Overhead Press"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Barbell Row"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Barbell Row (Smith)"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Barbell Bicep Curl"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Hammer Curl"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Dip"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Chest Dip"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Lateral Raise"))

                }
            }
        }
    }
}