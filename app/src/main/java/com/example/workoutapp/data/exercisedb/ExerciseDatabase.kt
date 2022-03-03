package com.example.workoutapp.data.exercisedb

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//add this to migrate autoMigrations = [AutoMigration(from = 1, to = 2)]
//just recreating the emulator also fixes this issue
@Database(entities = [Exercise::class, Workout::class, Log::class], version=1)
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
                        name = "Bench Press", primeMover = "Chest", exType = "Barbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Back Squat (HighBar)", primeMover = "Legs", exType = "Barbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Back Squat (LowBar)", primeMover = "Legs", exType = "Barbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Front Squat", primeMover = "Legs", exType = "Barbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Deadlift", primeMover = "Legs", exType = "Barbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Overhead Press", primeMover = "Shoulder", exType = "Barbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Barbell Row", primeMover = "Back", exType = "Barbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Barbell Row (Smith)", primeMover = "Back", exType = "Smith Machine"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Barbell Bicep Curl", primeMover = "Bicep", exType = "Barbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Hammer Curl", primeMover = "Bicep", exType = "Dumbbell"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Dip", primeMover = "Tricep", exType = "Calisthenics"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Chest Dip", primeMover = "Chest", exType = "Calisthenics"))
                    exerciseDatabase.exerciseDao().insertExercise(Exercise(
                        name = "Lateral Raise", primeMover = "Shoulder", exType = "Dumbbell"))

                }
            }
        }
    }
}