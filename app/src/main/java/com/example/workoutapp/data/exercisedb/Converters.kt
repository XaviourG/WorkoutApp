package com.example.workoutapp.data.exercisedb

import androidx.room.TypeConverter
import com.example.workoutapp.data.exercisedb.ExerciseInstance

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
    fun toIntList(string: String): List<Int> {
        return string.split("|").map { it.toInt() }
    }

    @TypeConverter
    fun fromIntList(intList: List<Int>): String {
        return intList.map{ it.toString() }.joinToString(separator = "|")
    }

    @TypeConverter
    fun toExerciseList(listString: String): List<ExerciseInstance> {
        var list = listString.split("|")
        var newList = mutableListOf<ExerciseInstance>()
        for(s in list){
            println("Is this the sabatour? $s")
            var bits = s.split("!")
            var unit: Int = bits[2].toInt()
            var sets: Array<String> = bits[1].split("_").toTypedArray<String>()
            var eid = bits[0].split(".")[0].toInt()
            var name = bits[0].split(".")[1]
            var exercise = Exercise(EID = eid, name = name, unit = unit)
            var exerciseInstance = ExerciseInstance(
                exercise = exercise,
                sets = sets,
            )
            newList.add(exerciseInstance)
        }
        return newList
    }

    @TypeConverter
    fun fromExerciseList(exerciseInstanceList: List<ExerciseInstance>)
    : String {
        var list = mutableListOf<String>()
        for(ex in exerciseInstanceList){
            println("Yeah nah the EID is = ${ex.exercise.EID}")
            var s: String = ex.exercise.EID.toString().plus(".").plus(ex.exercise.name)
                .plus("!").plus(ex.sets.joinToString(separator = "_")).plus("!")
                .plus(ex.exercise.unit)
            println("Saving new exercise as $s")
            list.add(s)
        }
        return list.joinToString(separator = "|")
    }

}