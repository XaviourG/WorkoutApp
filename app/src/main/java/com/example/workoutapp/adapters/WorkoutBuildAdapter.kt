package com.example.workoutapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.BuildWorkoutActivity
import com.example.workoutapp.data.exercisedb.Exercise
import com.example.workoutapp.data.exercisedb.ExerciseInstance
import com.example.workoutapp.data.exercisedb.ExerciseViewModel
import com.example.workoutapp.data.exercisedb.Workout
import com.example.workoutapp.databinding.ExerciseListingBinding
import com.example.workoutapp.databinding.FragmentExerciseBinding
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.coroutineContext

class WorkoutBuildAdapter(private val context : AppCompatActivity,
                          private val exerciseViewModel: ExerciseViewModel)
    : RecyclerView.Adapter<WorkoutBuildAdapter.WorkoutBuildViewHolder>() {

    data class Inst(
        val EI: ExerciseInstance,
        var adapter: SetBuildAdapter? = null,
        var notes: String? = "",
        var holder: WorkoutBuildViewHolder? = null
    )

    private var supersets = mutableListOf<String>()
    var list = mutableListOf<Inst>()

    class WorkoutBuildViewHolder(val binding: ExerciseListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viwType: Int): WorkoutBuildViewHolder {
        val binding = ExerciseListingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutBuildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutBuildAdapter.WorkoutBuildViewHolder, position: Int) {
        list[position].holder = holder

        //Show notes
        holder.binding.etNotes.setText(list[position].notes)

        //Hide SS Features
        //holder.binding.btnSSAbove.visibility = View.INVISIBLE
        //holder.binding.btnSSBelow.visibility = View.INVISIBLE
        holder.binding.tvName.text = list[position].EI.exercise.name
        if(supersets[position] == "none"){ // do nothing
        } else {
            holder.binding.tvName.setText(list[position].EI.exercise.name + "    -[${supersets[position]}]")
        }


        holder.binding.btnDeleteExercise.setOnClickListener {
            removeExerciseByPos(position)
        }
        var setAdapter = SetBuildAdapter(list[position].EI.exercise.unit)
        holder.binding.rvSets.adapter = setAdapter
        holder.binding.rvSets.layoutManager = LinearLayoutManager(context)
        for(set in list[position].EI.sets) {
            setAdapter.addSet(set)
        }
        holder.binding.btnAddSet.setOnClickListener {
            setAdapter.addSet()
        }

        holder.binding.btnMoveUp.setOnClickListener {
            if(position == 0) { //Can't move upwards do nothing
            } else { //Swap this with the entity above it and notify.
                //val setsTop = list[position-1].adapter!!.getSets()
                //val setsBot = list[position].adapter!!.getSets()
                val tmp = list[position-1]
                list[position-1] = list[position]
                list[position] = tmp
                notifyDataSetChanged()
                //list[position].adapter!!.setSets(setsTop)
                //list[position-1].adapter!!.setSets(setsBot)
                //notifyDataSetChanged()
            }
        }
        holder.binding.btnMoveDown.setOnClickListener {
            if(position == list.size - 1) { //Can't move downwards do nothing
            } else { //Swap this with the entity below it and notify.
                val tmp = list[position+1]
                list[position+1] = list[position]
                list[position] = tmp
                notifyDataSetChanged()
            }
        }

        /* SS Functionality
        holder.binding.btnSuperset.setOnClickListener {
            //show options
            holder.binding.btnSuperset.visibility = View.INVISIBLE
            holder.binding.btnSSAbove.visibility = View.VISIBLE
            holder.binding.btnSSBelow.visibility = View.VISIBLE
        }
        holder.binding.btnSSAbove.setOnClickListener {
            //Restore View
            holder.binding.btnSSAbove.visibility = View.INVISIBLE
            holder.binding.btnSSBelow.visibility = View.INVISIBLE
            holder.binding.btnSuperset.visibility = View.VISIBLE
            //Add superset
            if(position == 0 ) { //nothing above exists do nothing
            } else {
                if((supersets[position] == "none") or (supersets[position] == "up")) {
                    supersets[position] = "up"
                } else {
                    supersets[position] = "mid"
                }

                if((supersets[position] == "none") or (supersets[position] == "down")) {
                    supersets[position - 1] = "down"
                } else { //already part of superset, make a mid point
                    supersets[position - 1] = "mid"
                }
                notifyDataSetChanged()
            }
        }
        holder.binding.btnSSBelow.setOnClickListener {
            //Restore View
            holder.binding.btnSSAbove.visibility = View.INVISIBLE
            holder.binding.btnSSBelow.visibility = View.INVISIBLE
            holder.binding.btnSuperset.visibility = View.VISIBLE
            //Add superset
            if(position == list.size - 1 ) { //nothing below exists do nothing
            } else {
                if((supersets[position] == "none") or (supersets[position] == "down")) {
                    supersets[position] = "down"
                } else {
                    supersets[position] = "mid"
                }

                if((supersets[position] == "none") or (supersets[position] == "up")) {
                    supersets[position + 1] = "up"
                } else { //already part of superset, make a mid point
                    supersets[position + 1] = "mid"
                }
                notifyDataSetChanged()
            }
        }*/

        list[position].adapter = setAdapter
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addExercise(ex: Exercise){
        if(ex.EID == null){
            exerciseViewModel.allExercises.observe(context, {l ->
                l.let {
                    for (exercise in it) {
                        if (exercise.name == ex.name) {
                            ex.EID = exercise.EID
                            var inst = Inst(ExerciseInstance(ex))
                            list.add(inst)
                            supersets.add("none")
                            notifyItemInserted(list.size - 1)
                            break
                        }
                    }
                }
            })
        }
        else{
            var inst = Inst(ExerciseInstance(ex))
            list.add(inst)
            supersets.add("none") //ADD SUPERSET PRE HANDLING HERE
            notifyItemInserted(list.size - 1)
        }

    }

    fun removeExerciseByPos(position: Int){
        updateSets()
        list.removeAt(position)
        supersets.removeAt(position)
        //notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun updateSets(){
        for(inst in list){
            inst.EI.sets = inst.adapter!!.getSets()
        }
    }

    fun getExerciseList(): List<ExerciseInstance> {
        return list.map {it.EI}
    }

    fun setWorkout(workout: Workout){

        var newList = mutableListOf<Inst>()
        var i = 0
        while(i < workout.exercises.size){
            var newInst = Inst(workout.exercises[i])
            newInst.notes = workout.notes!![i]
            newList.add(newInst)
            i++
        }
        list = newList

        var newSS = mutableListOf<String>()
        for(SS in workout.supersets) {
            newSS.add(SS)
        }
        supersets = newSS

        notifyDataSetChanged()
    }

    fun getSupersets(): List<String> {
        return supersets.toList()
    }

    fun getNotes(): List<String> {
        for(inst in list){
            inst.notes = inst.holder!!.binding.etNotes.text.toString()
        }

        return list.map {it.notes!!}
    }
}