package com.sovereignfit.workoutapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovereignfit.workoutapp.WorkoutPlayer
import com.sovereignfit.workoutapp.data.exercisedb.*
import com.sovereignfit.workoutapp.databinding.ExercisePlayerListingBinding
import java.time.LocalDateTime

class WorkoutPlayerAdapter(private val context : AppCompatActivity, private val exerciseViewModel: ExerciseViewModel, private val parent: WorkoutPlayer) : RecyclerView.Adapter<WorkoutPlayerAdapter.WorkoutPlayerViewHolder>() {

    data class ModifiedInst(
        val EI: ExerciseInstance,
        var adapter: PlayerSetAdapter? = null,
        var note: String = "",
        var consistencyLog: MutableList<String>? = null
    )

    var list = mutableListOf<ModifiedInst>()

    class WorkoutPlayerViewHolder(val binding: ExercisePlayerListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutPlayerViewHolder {
        val binding = ExercisePlayerListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutPlayerAdapter.WorkoutPlayerViewHolder, position: Int) {
        println("!! CREATING ${list[position]} in Recycler view!")
        holder.binding.tvName.text = list[position].EI.exercise.name
        holder.binding.tvNotes.text = list[position].note

        var setAdapter = PlayerSetAdapter(context, list[position].EI.exercise.unit, this)

        val eid = list[position].EI.exercise.EID
        var previousExecution: Log? = null

        //consistency log functionality
        if(list[position].consistencyLog == null) { //Then this is first generation
            //set consistency log to all blanks for all sets
                println("Generating consistency log ###")
            var blankCL = mutableListOf<String>()
            for(set in list[position].EI.sets) {
                blankCL.add("::none")
            }
            list[position].consistencyLog = blankCL
        }

        holder.binding.rvSets.adapter = setAdapter
        holder.binding.rvSets.layoutManager = LinearLayoutManager(context)
        for(set in list[position].EI.sets) {
            setAdapter.addSet(set)
        }

        list[position].adapter = setAdapter

        exerciseViewModel.allLogs.observe(context, {list ->
            list.let {
                for(l in it) {
                    if (l.exerciseID == eid) {
                        previousExecution = l
                        println("Found previous execution: $previousExecution")
                        setAdapter.updatePrev(previousExecution!!)
                        break
                    }
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getExerciseList(): List<ExerciseInstance> {
        return list.map {it.EI}
    }
    fun setWorkout(workout: Workout){
        var newList = mutableListOf<ModifiedInst>()
        var i = 0
        while(i < workout.exercises.size){
            var newInst = ModifiedInst(workout.exercises[i])
            newInst.note = workout.notes!![i]
            newList.add(newInst)
            i++
        }
        list = newList
        notifyDataSetChanged()
    }

    fun getLogs(): MutableList<Log> {
        println("$list")
        var logs = mutableListOf<Log>()
        for(ex in list){
            println("$$$$ Iterating through: $ex")
            if(ex.adapter == null){ //skip
            } else {
                val setLog = ex.adapter!!.getLog()
                if (setLog == null || setLog == "") {
                } else {
                    val l = Log(
                        date = LocalDateTime.now().toString(),
                        exerciseID = ex.EI.exercise.EID!!,
                        performance = setLog
                    )
                    logs.add(l)
                }
            }
        }
        return logs

        //to make this work we need a new set build adapter with ticks to lock in logs!
    }

    fun getConsistencyLog(playerSetAdapter: PlayerSetAdapter): MutableList<String> {
        //update from parent
        if(parent.consistencyExists()) {
            val cLog = parent.getConsistency()
            var i = 0
            while (i < list.size) {
                if(cLog[i] != null) {
                    list[i].consistencyLog = cLog[i]
                }
                i++
            }
        } else { //consistency log not initalised, lets do that
            parent.updateConsistency(list.map {it.consistencyLog})
        }
        for(inst in list){
            if(inst.adapter == playerSetAdapter){
                return inst.consistencyLog!!
            }
        }
        //It should never make it here
        println("SOMETHING HAS GONE WAY FUCKING WRONG AT WorkoutPlayerAdapter")
        return arrayListOf<String>()
    }

    fun updateConsistencyLog(newLog: MutableList<String>, playerSetAdapter: PlayerSetAdapter) {
        for(inst in list){
            if(inst.adapter == playerSetAdapter){
                inst.consistencyLog = newLog
            }
        }
        parent.updateConsistency(list.map {it.consistencyLog})
    }

}