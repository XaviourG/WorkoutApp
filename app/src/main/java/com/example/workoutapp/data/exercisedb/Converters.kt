package com.example.workoutapp.data.exercisedb

import androidx.room.TypeConverter
import com.example.workoutapp.adapters.WorkoutBuildAdapter

class Converters {
    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split("|").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = "|")
    }

    @TypeConverter
    fun toExerciseList(listString: String): List<WorkoutBuildAdapter.ExerciseInstance> {
        var list = listString.split("|")
        var newList = mutableListOf<WorkoutBuildAdapter.ExerciseInstance>()
        for(s in list){
            var bits = s.split("-")
            var sets: Array<Int> = bits[1].split(".").map{it.toInt()}.toTypedArray()
            var eid = bits[0].split(".")[0].toInt()
            var name = bits[0].split(".")[1]
            var exercise = Exercise(EID = eid, name = name)
            var exerciseInstance = WorkoutBuildAdapter.ExerciseInstance(
                exercise = exercise,
                sets = sets
            )
            newList.add(exerciseInstance)
        }
        return newList
    }

    @TypeConverter
    fun fromExerciseList(exerciseInstanceList: List<WorkoutBuildAdapter.ExerciseInstance>)
    : String {
        var list = mutableListOf<String>()
        for(ex in exerciseInstanceList){
            var s: String = ex.exercise.EID.toString().plus(".").plus(ex.exercise.name)
                .plus("-").plus(ex.sets.joinToString(separator = "."))
            list.add(s)
        }
        return list.joinToString(separator = "|")
    }

}