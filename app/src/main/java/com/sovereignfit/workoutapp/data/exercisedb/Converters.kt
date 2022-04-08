package com.sovereignfit.workoutapp.data.exercisedb

import androidx.room.TypeConverter

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
        if(listString == "") {
            return mutableListOf<ExerciseInstance>()
        }
        var list = listString.split("|")
        var newList = mutableListOf<ExerciseInstance>()
        for(s in list){
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
            var s: String = ex.exercise.EID.toString().plus(".").plus(ex.exercise.name)
                .plus("!").plus(ex.sets.joinToString(separator = "_")).plus("!")
                .plus(ex.exercise.unit)
            list.add(s)
        }
        return list.joinToString(separator = "|")
    }

}

@TypeConverter
fun fromSets(s: String): List<MutableList<String>?>? {
    var res: List<MutableList<String>?>? = null
    if (s == "null"){// do nothing just return the null
    } else {
        res = mutableListOf<MutableList<String>?>()
        for(subString in s.split("|")) {
            if(subString == "null"){
                res.add(null)
            } else {
                res.add(subString.split("_").toMutableList())
            }
        }
    }
    return res
}

@TypeConverter
fun toSets(c: List<MutableList<String>?>?): String {
    var res = ""
    if (c == null) {
        res = "null"
    } else {
        var list = mutableListOf<String>()
        for (subList in c) {
            if(subList == null){
                list.add("null")
            } else {
                list.add(subList.joinToString(separator = "_"))
            }
        }
        res = list.joinToString(separator = "|")
    }
    return res
}